package com.xxxx.portal.service.impl;

import com.xxxx.common.pojo.AdminWithBLOBs;
import com.xxxx.common.result.BaseResult;
import com.xxxx.common.untils.Md5Util;
import com.xxxx.common.untils.RandomUtil;
import com.xxxx.portal.mapper.AdminMapper;
import com.xxxx.portal.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private AdminMapper adminMapper;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 注册
	 *
	 * @param admin
	 * @return
	 */
	@Override
	public BaseResult saveUser(AdminWithBLOBs admin) {
		String salt = RandomUtil.getRandom1();
		String md5Password = Md5Util.getMd5WithSalt(admin.getPassword(), salt);
		admin.setEcSalt(salt);
		admin.setPassword(md5Password);
		int result = adminMapper.insertSelective(admin);
		if (1==result){
			//通过RabbitMQ发送消息
			rabbitTemplate.convertAndSend("smsExchange","sms.register",admin.getEmail());
			return BaseResult.success();
		}
		return BaseResult.error();
	}
}