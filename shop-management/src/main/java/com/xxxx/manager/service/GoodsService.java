package com.xxxx.manager.service;

import com.xxxx.common.result.BaseResult;
import com.xxxx.manager.pojo.Goods;

/**
 * @author zhoubin
 * @since 1.0.0
 */
public interface GoodsService {
	/**
	 * 保存商品
	 * @param goods
	 * @return
	 */
	BaseResult saveGoods(Goods goods);

	/**
	 * 商品列表-分页查询
	 * @param goods
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	BaseResult selectGoodsListByPage(Goods goods,Integer pageNum,Integer pageSize);
}
