package com.xxxx.portal.consumer;

import com.xxxx.rpc.service.SendMessageService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Component
@RabbitListener(queues = "smsQueue")
public class SmsConsumer {
	@Reference(version = "1.0")
	private SendMessageService sendMessageService;

	@RabbitHandler
	public void sendMessage(String message){
		sendMessageService.sendMessage(message);
	}

}