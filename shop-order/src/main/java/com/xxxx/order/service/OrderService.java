package com.xxxx.order.service;

import com.xxxx.common.pojo.Admin;
import com.xxxx.common.result.BaseResult;
import com.xxxx.order.pojo.Order;
import com.xxxx.rpc.result.CartResult;

/**
 * @author zhoubin
 * @since 1.0.0
 */
public interface OrderService {

	/**
	 * 保存订单
	 * @param admin
	 * @param cartResult
	 * @return
	 */
	BaseResult saveOrder(Admin admin, CartResult cartResult);


	/**
	 * 根据订单编号查询订单
	 * @param orderSn
	 * @return
	 */
	Order selectOrderByOrderSn(String orderSn);
}
