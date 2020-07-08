package com.xxxx.order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.xxxx.common.pojo.Admin;
import com.xxxx.common.result.BaseResult;
import com.xxxx.order.config.AlipayConfig;
import com.xxxx.order.pojo.Order;
import com.xxxx.order.service.OrderService;
import com.xxxx.rpc.result.CartResult;
import com.xxxx.rpc.service.CartService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("order")
public class OrderController {

	@Reference(version = "1.0")
	private CartService cartService;
	@Autowired
	private OrderService orderService;

	/**
	 * 跳转到预订单页面
	 *
	 * @return
	 */
	@RequestMapping("preOrder")
	public String preOrder(Model model, HttpServletRequest request) {
		Admin admin = (Admin) request.getSession().getAttribute("user");
		model.addAttribute("cartResult", cartService.selectCartList(admin));
		return "order/preOrder";
	}


	/**
	 * 提交订单
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("submitOrder")
	public String submitOrder(HttpServletRequest request, Model model) {
		Admin admin = (Admin) request.getSession().getAttribute("user");
		CartResult cartResult = cartService.selectCartList(admin);
		//保存订单
		BaseResult baseResult = orderService.saveOrder(admin, cartResult);
		//清除购物车
		cartService.clearCart(admin);
		//页面跳转
		model.addAttribute("totalPrice", cartResult.getTotalPrice());
		model.addAttribute("orderSn", baseResult.getMessage());
		return "order/submitOrder";
	}


	/**
	 * 支付
	 * @param orderSn
	 * @param model
	 * @return
	 */
	@RequestMapping("payment")
	public String payment(String orderSn, Model model) {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
				AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
				AlipayConfig.sign_type);

		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		Order order = orderService.selectOrderByOrderSn(orderSn);
		if (null == order) {
			return null;
		}
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = orderSn;
		//付款金额，必填
		String total_amount = String.valueOf(order.getTotalAmount());
		//订单名称，必填
		String subject = "用户" + order.getUserId() + "的订单";
		//商品描述，可空
		String body = "";

		alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
				+ "\"total_amount\":\"" + total_amount + "\","
				+ "\"subject\":\"" + subject + "\","
				+ "\"body\":\"" + body + "\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
		//		+ "\"total_amount\":\""+ total_amount +"\","
		//		+ "\"subject\":\""+ subject +"\","
		//		+ "\"body\":\""+ body +"\","
		//		+ "\"timeout_express\":\"10m\","
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

		//请求
		String result = null;
		try {
			result = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		//输出
		model.addAttribute("result", result);
		return "order/payment";
	}


}