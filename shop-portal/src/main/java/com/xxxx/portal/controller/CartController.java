package com.xxxx.portal.controller;

import com.xxxx.common.pojo.Admin;
import com.xxxx.common.result.BaseResult;
import com.xxxx.rpc.pojo.CartVo;
import com.xxxx.rpc.service.CartService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 购物车
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("cart")
public class CartController {


	@Reference(version = "1.0")
	private CartService cartService;


	/**
	 * 添加购物车
	 *
	 * @param cartVo
	 * @return
	 */
	@RequestMapping("addCart")
	@ResponseBody
	public BaseResult addCart(CartVo cartVo, HttpServletRequest request) {
		Admin admin = (Admin) request.getSession().getAttribute("user");
		cartVo.setAddTime(new Date());
		return cartService.addCart(cartVo, admin);
	}


	/**
	 * 查询购物车数量
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("getCartNum")
	@ResponseBody
	public Integer getCartNum(HttpServletRequest request) {
		Admin admin = (Admin) request.getSession().getAttribute("user");
		return cartService.getCartNum(admin);
	}

	/**
	 * 获取购物车列表
	 *
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("getCartList")
	public String getCartList(HttpServletRequest request, Model model) {
		Admin admin = (Admin) request.getSession().getAttribute("user");
		model.addAttribute("cartResult", cartService.selectCartList(admin));
		return "cart/list";
	}

}