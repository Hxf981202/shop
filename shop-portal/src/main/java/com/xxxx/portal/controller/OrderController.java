package com.xxxx.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("order")
public class OrderController {

	/**
	 * 跳转到订单系统
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("toPreOrder")
	public String toPreOrder(HttpServletRequest request) {
		String orderUrl = (String) request.getSession().getServletContext().getAttribute("orderUrl");
		return "redirect:" + orderUrl + "order/preOrder";
	}

	/**
	 * 异步通知回调
	 *
	 * @return
	 */
	@RequestMapping("/callback")
	public String callback() {
		System.out.println("支付成功！");
		return "callback";
	}

}