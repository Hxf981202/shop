package com.xxxx.manager.service;

import com.xxxx.common.result.BaseResult;
import com.xxxx.manager.pojo.GoodsImages;

/**
 * 商品相册
 * @author zhoubin
 * @since 1.0.0
 */
public interface GoodsImagesService {
	/**
	 * 保存商品相册
	 * @param goodsImages
	 * @return
	 */
	BaseResult saveGoodsImages(GoodsImages goodsImages);
}
