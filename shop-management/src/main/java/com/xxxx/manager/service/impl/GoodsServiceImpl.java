package com.xxxx.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.common.result.BaseResult;
import com.xxxx.common.untils.JsonUtil;
import com.xxxx.manager.mapper.GoodsMapper;
import com.xxxx.manager.pojo.Goods;
import com.xxxx.manager.pojo.GoodsExample;
import com.xxxx.manager.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 保存商品
	 *
	 * @param goods
	 * @return
	 */
	@Override
	public BaseResult saveGoods(Goods goods) {
		if (null == goods.getGoodsId()) {
			if (!StringUtils.isEmpty(goods.getGoodsContent())) {
				//转义
				String htmlEscape = HtmlUtils.htmlEscape(goods.getGoodsContent(), "UTF-8");
				goods.setGoodsContent(htmlEscape);
			}
			int result = goodsMapper.insertSelective(goods);
			if (1 == result) {
				//添加数据时清空redis
				redisTemplate.delete(redisTemplate.keys("goods:list*"));
				BaseResult baseResult = BaseResult.success();
				baseResult.setMessage(String.valueOf(goods.getGoodsId()));
				return baseResult;
			}
		}
		return BaseResult.error();
	}

	/**
	 * 商品列表-分页查询
	 *
	 * @param goods
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public BaseResult selectGoodsListByPage(Goods goods, Integer pageNum, Integer pageSize) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		//开启分页
		PageHelper.startPage(pageNum, pageSize);
		//查询对象
		GoodsExample example = new GoodsExample();
		//                   goods:list:pageNum_?:pageSize_?:
		//分类                goods:list:pageNum_?:pageSize_?:catId_?:
		//品牌                goods:list:pageNum_?:pageSize_?:brandId_?:
		//关键词              goods:list:pageNum_?:pageSize_?:goodsName_?:
		//分类+品牌           goods:list:pageNum_?:pageSize_?:catId_?:brandId_?:
		//分类+关键词         goods:list:pageNum_?:pageSize_?:catId_?:goodsName_?:
		//品牌+关键词         goods:list:pageNum_?:pageSize_?:brandId_?:goodsName_?:
		//分类+品牌+关键词     goods:list:pageNum_?:pageSize_?:catId_?:brandId_?:goodsName_?:
		//最终               goods:list:pageNum_:pageSize_:catId_:brandId_?:goodsName_?
		String[] arry = new String[]{
				"goods:list:",
				"pageNum_" + pageNum + ":",
				"pageSize_" + pageSize + ":",
				"catId_:",
				"brandId_:",
				"goodsName_:"
		};
		//设置条件
		GoodsExample.Criteria criteria = example.createCriteria();
		//分类条件
		if (null != goods.getCatId()) {
			criteria.andCatIdEqualTo(goods.getCatId());
			arry[3] = "catId_" + goods.getCatId() + ":";
		}
		//品牌条件
		if (null != goods.getBrandId()) {
			criteria.andBrandIdEqualTo(goods.getBrandId());
			arry[4] = "brandId_" + goods.getBrandId() + ":";
		}
		//关键词条件
		if (!StringUtils.isEmpty(goods.getGoodsName())) {
			criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
			arry[5] = "goodsName_" + goods.getGoodsName() + ":";
		}
		//完整的redis的key
		String redisKey = Arrays.asList(arry).stream().collect(Collectors.joining());
		//从redis获取数据，有就直接返回，没有查询数据库
		String pageInfoJson = valueOperations.get(redisKey);
		if (!StringUtils.isEmpty(pageInfoJson)) {
			return BaseResult.success(JsonUtil.jsonStr2Object(pageInfoJson, PageInfo.class));
		}
		//===============错误写法=====================
		// String listJson = valueOperations.get(redisKey);
		// if (!StringUtils.isEmpty(listJson)){
		// 	List<Goods> goodsList = JsonUtil.jsonToList(listJson, Goods.class);
		// 	PageInfo<Goods> pageInfo1 = new PageInfo<>(goodsList);
		// 	return BaseResult.success(pageInfo1);
		// }
		//===============错误写法=====================
		//查询商品列表
		List<Goods> list = goodsMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(list)) {
			//把商品列表放入分页对象
			PageInfo<Goods> pageInfo = new PageInfo<>(list);
			//存入redis
			valueOperations.set(redisKey, JsonUtil.object2JsonStr(pageInfo));

			//===============错误写法=====================
			// valueOperations.set(redisKey, JsonUtil.object2JsonStr(list));
			//===============错误写法=====================
			return BaseResult.success(pageInfo);
		}
		return BaseResult.error();
	}
}