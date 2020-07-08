package com.xxxx.portal.controller;

import com.xxxx.common.result.ShopPageInfo;
import com.xxxx.rpc.pojo.GoodsVo;
import com.xxxx.rpc.service.SearchService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 搜索
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("search")
public class SearchController {

	@Reference(version = "1.0")
	private SearchService searchService;


	/**
	 * 页面跳转-搜索页
	 *
	 * @param searchStr
	 * @param model
	 * @return
	 */
	@RequestMapping("index")
	public String index(String searchStr, Model model) {
		model.addAttribute("searchStr", searchStr);
		return "search/doSearch";
	}


	/**
	 * 搜索
	 * @param searchStr
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("searchGoods")
	@ResponseBody
	public ShopPageInfo<GoodsVo> doSearch(String searchStr, Integer pageNum, Integer pageSize) {
		return searchService.doSearch(searchStr, pageNum, pageSize);
	}

}