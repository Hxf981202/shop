package com.xxxx.rpc.service;

import com.xxxx.rpc.pojo.GoodsCategoryVo;

import java.util.List;

/**
 * 商品分类
 *
 * @author zhoubin
 * @since 1.0.0
 */
public interface GoodsCategoryService {
	/**
	 * 商品分类-查询所有分类
	 *
	 * @return
	 */
	List<GoodsCategoryVo> selectListForView();
}
