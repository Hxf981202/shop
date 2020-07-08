package com.xxxx.portal.controller;

import com.xxxx.rpc.pojo.GoodsCategoryVo;
import com.xxxx.rpc.service.GoodsCategoryService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品分类
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("goodsCategory")
public class GoodsCategoryController {

	@Reference(version = "1.0")
	private GoodsCategoryService goodsCategoryService;


	/**
	 * 商品分类列表
	 * @return
	 */
	@RequestMapping("list")
	@ResponseBody
	public List<GoodsCategoryVo> selectList() {
		return goodsCategoryService.selectListForView();
	}

}