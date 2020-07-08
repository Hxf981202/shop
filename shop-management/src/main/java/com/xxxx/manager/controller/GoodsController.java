package com.xxxx.manager.controller;

import com.xxxx.common.result.BaseResult;
import com.xxxx.common.result.FileResult;
import com.xxxx.manager.pojo.Goods;
import com.xxxx.manager.pojo.GoodsCategory;
import com.xxxx.manager.pojo.GoodsImages;
import com.xxxx.manager.service.BrandService;
import com.xxxx.manager.service.GoodsCategoryService;
import com.xxxx.manager.service.GoodsImagesService;
import com.xxxx.manager.service.GoodsService;
import com.xxxx.manager.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 商品管理
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private GoodsCategoryService goodsCategoryService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private UploadService uploadService;
	@Autowired
	private GoodsImagesService goodsImagesService;


	/**
	 * 页面跳转-商品分类-列表
	 *
	 * @return
	 */
	@RequestMapping("/category/list")
	public String CategroyList(Model model) {
		model.addAttribute("gcvList", goodsCategoryService.selectListForView());
		return "goods/category/category-list";
	}


	/**
	 * 页面跳转-商品分类-列表
	 *
	 * @return
	 */
	@RequestMapping("/category/add")
	public String CategoryAdd(Model model) {
		model.addAttribute("gcList", goodsCategoryService.selectTopList());
		return "goods/category/category-add";
	}


	/**
	 * 根据父id查询子分类
	 *
	 * @param parentId
	 * @return
	 */
	@RequestMapping("category/{parentId}")
	@ResponseBody
	public List<GoodsCategory> selectListByParentId(@PathVariable Short parentId) {
		return goodsCategoryService.selectListByParentId(parentId);
	}

	/**
	 * 商品分类-保存
	 *
	 * @param goodsCategory
	 * @return
	 */
	@RequestMapping("category/save")
	@ResponseBody
	public BaseResult saveGoodsCategory(GoodsCategory goodsCategory) {
		int result = goodsCategoryService.SaveGoodsCategory(goodsCategory);
		return result == 1 ? BaseResult.success() : BaseResult.error();
	}


	/**
	 * 页面跳转-商品-列表
	 *
	 * @return
	 */
	@RequestMapping("list")
	public String goodsList(Model model) {
		model.addAttribute("gcList", goodsCategoryService.selectAllList());
		model.addAttribute("brandList", brandService.selectAllList());
		return "goods/goods-list";
	}


	/**
	 * 页面跳转-商品-新增
	 *
	 * @return
	 */
	@RequestMapping("add")
	public String goodsAdd(Model model) {
		model.addAttribute("gcList", goodsCategoryService.selectTopList());
		model.addAttribute("brandList", brandService.selectAllList());
		return "goods/goods-add";
	}


	/**
	 * 商品-保存
	 *
	 * @param goods
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public BaseResult saveGoods(Goods goods) {
		return goodsService.saveGoods(goods);
	}

	/**
	 * 商品相册-保存
	 *
	 * @return
	 */
	@RequestMapping("/images/save")
	@ResponseBody
	public BaseResult saveGoodsImages(MultipartFile file, Integer goodsId) {
		//上传图片
		String filename = file.getOriginalFilename();
		String date = DateTimeFormatter.ofPattern("yyyy/MM/dd/").format(LocalDate.now());
		filename = date + System.currentTimeMillis() + filename.substring(filename.lastIndexOf("."));
		try {
			FileResult fileResult = uploadService.upload(file.getInputStream(), filename);
			//上传成功,保存商品相册
			if ("success".equals(fileResult.getSuccess())) {
				GoodsImages goodsImages = new GoodsImages();
				goodsImages.setGoodsId(goodsId);
				goodsImages.setImageUrl(fileResult.getFileUrl());
				return goodsImagesService.saveGoodsImages(goodsImages);
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	@RequestMapping("listForPage")
	@ResponseBody
	public BaseResult listForPage(Goods goods, Integer pageNum, Integer pageSize) {
		return goodsService.selectGoodsListByPage(goods, pageNum, pageSize);
	}
}