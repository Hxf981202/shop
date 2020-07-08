package com.xxxx.order.service.impl;

import com.xxxx.common.enums.OrderStatus;
import com.xxxx.common.enums.PayStatus;
import com.xxxx.common.enums.PromTypeStatus;
import com.xxxx.common.enums.SendStatus;
import com.xxxx.common.enums.ShipStatus;
import com.xxxx.common.pojo.Admin;
import com.xxxx.common.result.BaseResult;
import com.xxxx.order.mapper.OrderGoodsMapper;
import com.xxxx.order.mapper.OrderMapper;
import com.xxxx.order.pojo.Order;
import com.xxxx.order.pojo.OrderExample;
import com.xxxx.order.pojo.OrderGoods;
import com.xxxx.order.service.OrderService;
import com.xxxx.rpc.pojo.CartVo;
import com.xxxx.rpc.result.CartResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderGoodsMapper orderGoodsMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Value("${redis.order.increment}")
	private String redisOrderIncrement;

	/**
	 * 保存订单
	 *
	 * @param admin
	 * @param cartResult
	 * @return
	 */
	@Override
	public BaseResult saveOrder(Admin admin, CartResult cartResult) {
		Order order = new Order();
		//订单编号
		String orderSn =
				"shop_" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()) + getIncrement(redisOrderIncrement);
		order.setOrderSn(orderSn);
		//用户id
		order.setUserId(Integer.valueOf(admin.getAdminId()));
		//状态
		order.setOrderStatus(OrderStatus.no_confirm.getStatus());
		order.setShippingStatus(ShipStatus.no_send.getStatus());
		order.setPayStatus(PayStatus.no_pay.getStatus());
		//金额
		order.setTotalAmount(cartResult.getTotalPrice());
		order.setOrderAmount(cartResult.getTotalPrice());
		order.setGoodsPrice(cartResult.getTotalPrice());
		//下单时间
		Long addTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
		order.setAddTime(addTime.intValue());
		//存订单
		int result = orderMapper.insertSelective(order);
		//订单插入成功
		if (1 == result) {
			List<CartVo> cartList = cartResult.getCartList();
			List<OrderGoods> list = new ArrayList<>();
			for (CartVo cartVo : cartList) {
				OrderGoods orderGoods = new OrderGoods();
				orderGoods.setOrderId(order.getOrderId());
				orderGoods.setGoodsId(cartVo.getGoodsId());
				orderGoods.setGoodsName(cartVo.getGoodsName());
				orderGoods.setGoodsPrice(cartVo.getMarketPrice());
				orderGoods.setGoodsNum((Short.valueOf(String.valueOf(cartVo.getGoodsNum()))));
				orderGoods.setPromType(PromTypeStatus.normal.getStatus());
				orderGoods.setIsSend(SendStatus.no_pay.getStatus());
				list.add(orderGoods);
			}
			result = orderGoodsMapper.insertOrderGoodsBatch(list);
			if (result == list.size()) {
				BaseResult baseResult = BaseResult.success();
				baseResult.setMessage(orderSn);
				return baseResult;
			}
		}
		return BaseResult.error();
	}

	/**
	 * 根据订单编号查询订单
	 *
	 * @param orderSn
	 * @return
	 */
	@Override
	public Order selectOrderByOrderSn(String orderSn) {
		OrderExample example = new OrderExample();
		example.createCriteria().andOrderSnEqualTo(orderSn);
		List<Order> list = orderMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list) || list.size() > 1) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * redis自增key
	 *
	 * @param key
	 * @return
	 */
	private Long getIncrement(String key) {
		RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		return redisAtomicLong.getAndIncrement();

	}
}