package com.xxxx.rpc.service.impl;

import com.xxxx.common.untils.JsonUtil;
import com.xxxx.rpc.mapper.GoodsCategoryMapper;
import com.xxxx.rpc.pojo.GoodsCategory;
import com.xxxx.rpc.pojo.GoodsCategoryExample;
import com.xxxx.rpc.pojo.GoodsCategoryVo;
import com.xxxx.rpc.service.GoodsCategoryService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service(version = "1.0")
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Value("${goods.category.list.key}")
	private String goodsCategoryKey;


	/**
	 * 商品分类-查询所有分类
	 *
	 * @return
	 */
	@Override
	public List<GoodsCategoryVo> selectListForView() {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		//1.从redis查询数据，有就直接返回
		String gcListJson = valueOperations.get(goodsCategoryKey);
		if (!StringUtils.isEmpty(gcListJson)) {
			return JsonUtil.jsonToList(gcListJson, GoodsCategoryVo.class);
		}
		//如果没有，去数据库查询
		// //查询顶级分类
		// GoodsCategoryExample example = new GoodsCategoryExample();
		// example.createCriteria().andParentIdEqualTo((short) 0).andLevelEqualTo((byte) 1);
		// List<GoodsCategory> gcList01 = goodsCategoryMapper.selectByExample(example);
		// List<GoodsCategoryVo> gcvList01 = new ArrayList<>();
		// for (GoodsCategory gc01 : gcList01) {
		// 	GoodsCategoryVo gcv01 = new GoodsCategoryVo();
		// 	BeanUtils.copyProperties(gc01, gcv01);
		// 	//清空查询条件
		// 	example.clear();
		// 	//查询二级分类
		// 	example.createCriteria().andParentIdEqualTo(gc01.getId()).andLevelEqualTo((byte) 2);
		// 	List<GoodsCategory> gcList02 = goodsCategoryMapper.selectByExample(example);
		// 	List<GoodsCategoryVo> gcvList02 = new ArrayList<>();
		// 	for (GoodsCategory gc02 : gcList02) {
		// 		GoodsCategoryVo gcv02 = new GoodsCategoryVo();
		// 		BeanUtils.copyProperties(gc02, gcv02);
		// 		//查询三级分类
		// 		example.clear();
		// 		example.createCriteria().andParentIdEqualTo(gc02.getId()).andLevelEqualTo((byte) 3);
		// 		List<GoodsCategory> gcList03 = goodsCategoryMapper.selectByExample(example);
		// 		List<GoodsCategoryVo> gcvList03 = new ArrayList<>();
		// 		for (GoodsCategory gc03 : gcList03) {
		// 			GoodsCategoryVo gcv03 = new GoodsCategoryVo();
		// 			BeanUtils.copyProperties(gc03, gcv03);
		// 			gcvList03.add(gcv03);
		// 		}
		// 		//把三级分类放入二级分类的children
		// 		gcv02.setChildren(gcvList03);
		// 		gcvList02.add(gcv02);
		// 	}
		// 	//把二级分类放入一级分类的children
		// 	gcv01.setChildren(gcvList02);
		// 	gcvList01.add(gcv01);
		// }
		//====================JDK8新特性==============================
		GoodsCategoryExample example = new GoodsCategoryExample();
		List<GoodsCategory> gcList = goodsCategoryMapper.selectByExample(example);
		//List<GoodsCategory>转成List<GoodsCategoryVo>
		List<GoodsCategoryVo> gcvList = gcList.stream().map(gc -> {
			GoodsCategoryVo gcv = new GoodsCategoryVo();
			BeanUtils.copyProperties(gc, gcv);
			return gcv;
		}).collect(Collectors.toList());
		/**
		 * 将List<GoodsCategoryVo>转成Map<parentId,List<GoodsCategoryVo>>
		 * 按parentId分组，key为parentId，value为parentId对应的List
		 */
		Map<Short, List<GoodsCategoryVo>> map =
				gcvList.stream().collect(Collectors.groupingBy(GoodsCategoryVo::getParentId));
		/**
		 * 循环，将children赋值
		 */
		gcvList.forEach(e -> e.setChildren(map.get(e.getId())));
		/**
		 * 拦截器，返回Level为1的List，也就是顶级分类
		 */
		List<GoodsCategoryVo> gcvList01 = gcvList.stream().filter(t -> t.getLevel() == 1).collect(Collectors.toList());
		//将数据存入redis
		valueOperations.set(goodsCategoryKey,JsonUtil.object2JsonStr(gcvList01));
		//====================JDK8新特性==============================
		return gcvList01;
	}
}