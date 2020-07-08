package com.xxxx.manager.service;

import com.xxxx.manager.pojo.GoodsCategory;
import com.xxxx.manager.pojo.GoodsCategoryVo;

import java.util.List;

/**
 * 商品分类
 *
 * @author zhoubin
 * @since 1.0.0
 */
public interface GoodsCategoryService {

	/**
	 * 查询顶级分类
	 *
	 * @return
	 */
	List<GoodsCategory> selectTopList();

	/**
	 * 根据父id查询子分类
	 *
	 * @param parentId
	 * @return
	 */
	List<GoodsCategory> selectListByParentId(Short parentId);

	/**
	 * 商品分类-保存
	 *
	 * @param goodsCategory
	 * @return
	 */
	int SaveGoodsCategory(GoodsCategory goodsCategory);


	/**
	 * 商品分类-查询所有分类
	 *
	 * @return
	 */
	List<GoodsCategoryVo> selectListForView();

	/**
	 * 查询所有分类
	 *
	 * @return
	 */
	List<GoodsCategory> selectAllList();
}
