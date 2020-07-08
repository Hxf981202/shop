package com.xxxx.portal.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Configuration
public class RabbitMQConfig {

	@Bean
	public Queue queue(){
		return new Queue("smsQueue");
	}

	@Bean
	public TopicExchange topicExchange(){
		return new TopicExchange("smsExchange");
	}


	@Bean
	public Binding binding(){
		return BindingBuilder.bind(queue()).to(topicExchange()).with("sms.*");
	}

}