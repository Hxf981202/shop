package com.xxxx.rpc.service;

import com.xxxx.common.pojo.Admin;
import com.xxxx.common.result.BaseResult;
import com.xxxx.rpc.pojo.CartVo;
import com.xxxx.rpc.result.CartResult;

/**
 * 购物车
 *
 * @author zhoubin
 * @since 1.0.0
 */
public interface CartService {

	/**
	 * 加入购物车
	 *
	 * @param cartVo
	 * @param admin
	 * @return
	 */
	BaseResult addCart(CartVo cartVo, Admin admin);


	/**
	 * 查询购物车数量
	 *
	 * @param admin
	 * @return
	 */
	Integer getCartNum(Admin admin);


	/**
	 * 获取购物车列表
	 *
	 * @param admin
	 * @return
	 */
	CartResult selectCartList(Admin admin);

	/**
	 * 清除购物车
	 *
	 * @param admin
	 * @return
	 */
	BaseResult clearCart(Admin admin);

}
