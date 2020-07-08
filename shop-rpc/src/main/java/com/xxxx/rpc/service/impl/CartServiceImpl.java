package com.xxxx.rpc.service.impl;

import com.xxxx.common.pojo.Admin;
import com.xxxx.common.result.BaseResult;
import com.xxxx.common.untils.JsonUtil;
import com.xxxx.rpc.pojo.CartVo;
import com.xxxx.rpc.result.CartResult;
import com.xxxx.rpc.service.CartService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service(version = "1.0")
public class CartServiceImpl implements CartService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Value("${user.cart}")
	private String userCart;

	private HashOperations<String, String, String> hashOperations = null;

	/**
	 * 加入购物车
	 *
	 * @param cartVo
	 * @param admin
	 * @return
	 */
	@Override
	public BaseResult addCart(CartVo cartVo, Admin admin) {
		//判断用户是否存在
		if (null == admin || null == admin.getAdminId()) {
			return BaseResult.error();
		}
		hashOperations = redisTemplate.opsForHash();
		//查询用户的购物车信息
		Map<String, String> map = hashOperations.entries(userCart + ":" + admin.getAdminId());
		//如果不为空
		if (!CollectionUtils.isEmpty(map)) {
			//判断购物车里是否已经存在加入的商品信息
			String cartJson = map.get(String.valueOf(cartVo.getGoodsId()));
			if (!StringUtils.isEmpty(cartJson)) {
				CartVo cart = JsonUtil.jsonStr2Object(cartJson, CartVo.class);
				//更新购物车商品数量和价格
				cart.setGoodsNum(cart.getGoodsNum() + cartVo.getGoodsNum());
				cart.setMarketPrice(cartVo.getMarketPrice());
				cart.setAddTime(new Date());
				map.put(String.valueOf(cartVo.getGoodsId()), JsonUtil.object2JsonStr(cart));
			} else {
				//购物车里不存在加入的商品，将商品加入购物车
				map.put(String.valueOf(cartVo.getGoodsId()), JsonUtil.object2JsonStr(cartVo));
			}
		} else {
			//将商品加入购物车
			map.put(String.valueOf(cartVo.getGoodsId()), JsonUtil.object2JsonStr(cartVo));
		}
		//将购物车信息存入redis
		hashOperations.putAll(userCart + ":" + admin.getAdminId(), map);
		return BaseResult.success();
	}

	/**
	 * 查询购物车数量
	 *
	 * @param admin
	 * @return
	 */
	@Override
	public Integer getCartNum(Admin admin) {
		Integer result = 0;
		if (null == admin || null == admin.getAdminId()) {
			return result;
		}
		hashOperations = redisTemplate.opsForHash();
		Map<String, String> map = hashOperations.entries(userCart + ":" + admin.getAdminId());
		if (!CollectionUtils.isEmpty(map)) {
			//累加数量
			result = map.values().stream().mapToInt(e -> {
				CartVo cartVo = JsonUtil.jsonStr2Object(e, CartVo.class);
				return cartVo.getGoodsNum();
			}).sum();
		}
		return result;
	}


	/**
	 * 获取购物车列表
	 *
	 * @param admin
	 * @return
	 */
	@Override
	public CartResult selectCartList(Admin admin) {
		CartResult cartResult = null;
		//判断用户是否存在
		if (null == admin || null == admin.getAdminId()) {
			return cartResult;
		}
		hashOperations = redisTemplate.opsForHash();
		Map<String, String> map = hashOperations.entries(userCart + ":" + admin.getAdminId());
		//判断购物车信息是否为空
		if (!CollectionUtils.isEmpty(map)) {
			cartResult = new CartResult();
			//获取列表
			List<CartVo> list = map.values().stream().map(e -> {
				CartVo cartVo = JsonUtil.jsonStr2Object(e, CartVo.class);
				return cartVo;
			}).collect(Collectors.toList());
			//获取总价
			BigDecimal totalPrice =
					list.stream().map(e -> e.getMarketPrice().multiply(new BigDecimal(String.valueOf(e.getGoodsNum())))).reduce(BigDecimal.ZERO, BigDecimal::add);
			//保留2位小数，四舍五入
			totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
			cartResult.setCartList(list);
			cartResult.setTotalPrice(totalPrice);
			return cartResult;
		}
		return cartResult;
	}


	/**
	 * 清除购物车
	 *
	 * @param admin
	 * @return
	 */
	@Override
	public BaseResult clearCart(Admin admin) {
		if (null == admin || null == admin.getAdminId()) {
			return BaseResult.error();
		}
		hashOperations = redisTemplate.opsForHash();
		Map<String, String> map = hashOperations.entries(userCart + ":" + admin.getAdminId());
		if (CollectionUtils.isEmpty(map)) {
			return BaseResult.error();
		}
		redisTemplate.delete(userCart + ":" + admin.getAdminId());
		return BaseResult.success();
	}
}