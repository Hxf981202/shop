package com.xxxx.rpc.service;

import com.xxxx.common.result.BaseResult;

/**
 * @author zhoubin
 * @since 1.0.0
 */
public interface SendMessageService {

	/**
	 * 发送短信
	 * @param phone
	 * @return
	 */
	BaseResult sendMessage(String phone);
}
