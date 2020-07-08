package com.xxxx.manager.service;

import com.xxxx.manager.pojo.Brand;

import java.util.List;

/**
 * 品牌
 *
 * @author zhoubin
 * @since 1.0.0
 */
public interface BrandService {
	/**
	 * 查询所有品牌
	 * @return
	 */
	List<Brand> selectAllList();
}
