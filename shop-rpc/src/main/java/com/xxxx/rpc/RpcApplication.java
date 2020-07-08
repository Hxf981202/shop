package com.xxxx.rpc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.xxxx.rpc.mapper")
public class RpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpcApplication.class, args);
	}

}