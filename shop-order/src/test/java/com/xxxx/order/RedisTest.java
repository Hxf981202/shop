package com.xxxx.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@SpringBootTest
public class RedisTest {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;


	/**
	 * redis自增key
	 */
	@Test
	public void testIncrement(){
		RedisAtomicLong redisAtomicLong = new RedisAtomicLong("redis:increment", redisTemplate.getConnectionFactory());
		System.out.println(redisAtomicLong.getAndIncrement());
	}

}