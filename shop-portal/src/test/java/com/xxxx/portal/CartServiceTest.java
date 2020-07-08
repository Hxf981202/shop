package com.xxxx.portal;
import com.xxxx.common.pojo.Admin;
import com.xxxx.rpc.pojo.CartVo;
import com.xxxx.rpc.service.CartService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@SpringBootTest
public class CartServiceTest {

	@Reference(version = "1.0")
	private CartService cartService;

	/**
	 * 测试添加购物车
	 */
	@Test
	public void testAddCart(){
		Admin admin = new Admin();
		admin.setAdminId((short) 1);
		CartVo cartVo = new CartVo();
		cartVo.setGoodsId(23456);
		cartVo.setGoodsName("JAVA核心技术卷");
		cartVo.setMarketPrice(new BigDecimal("300"));
		cartVo.setGoodsNum(20);
		cartVo.setAddTime(new Date());
		cartService.addCart(cartVo,admin);
	}

	/**
	 * 测试购物车数量
	 */
	@Test
	public void testGetCartNum(){
		Admin admin = new Admin();
		admin.setAdminId((short) 1);
		Integer cartNum = cartService.getCartNum(admin);
		System.out.println(cartNum);
	}

}