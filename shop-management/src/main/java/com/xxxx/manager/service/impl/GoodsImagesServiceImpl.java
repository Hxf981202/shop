package com.xxxx.manager.service.impl;

import com.xxxx.common.result.BaseResult;
import com.xxxx.manager.mapper.GoodsImagesMapper;
import com.xxxx.manager.pojo.GoodsImages;
import com.xxxx.manager.service.GoodsImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service
public class GoodsImagesServiceImpl implements GoodsImagesService {

	@Autowired
	private GoodsImagesMapper goodsImagesMapper;

	/**
	 * 保存商品相册
	 *
	 * @param goodsImages
	 * @return
	 */
	@Override
	public BaseResult saveGoodsImages(GoodsImages goodsImages) {
		int result = goodsImagesMapper.insertSelective(goodsImages);
		return 1 == result ? BaseResult.success() : BaseResult.error();
	}
}