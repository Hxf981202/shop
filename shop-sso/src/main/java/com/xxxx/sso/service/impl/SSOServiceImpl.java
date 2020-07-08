package com.xxxx.sso.service.impl;

import com.xxxx.common.pojo.Admin;
import com.xxxx.common.pojo.AdminExample;
import com.xxxx.common.untils.JsonUtil;
import com.xxxx.common.untils.Md5Util;
import com.xxxx.common.untils.UUIDUtil;
import com.xxxx.sso.mapper.AdminMapper;
import com.xxxx.sso.service.SSOService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service(version = "1.0")
public class SSOServiceImpl implements SSOService {

	@Autowired
	private AdminMapper adminMapper;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Value("${user.ticket}")
	private String userTicket;

	/**
	 * 验证用户返回票据
	 *
	 * @param admin
	 * @return
	 */
	@Override
	public String login(Admin admin) {
		//判断用户名
		if (StringUtils.isEmpty(admin.getUserName().trim())) {
			System.out.println("用户名为空！");
			return null;
		}
		//判断密码
		if (StringUtils.isEmpty(admin.getPassword().trim())) {
			System.out.println("密码为空！");
			return null;
		}
		//查询用户
		AdminExample example = new AdminExample();
		example.createCriteria().andUserNameEqualTo(admin.getUserName());
		List<Admin> list = adminMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list) || list.size() > 1) {
			System.out.println("用户名非法！");
			return null;
		}
		Admin a = list.get(0);
		//校验密码
		if (!a.getPassword().equals(Md5Util.getMd5WithSalt(admin.getPassword(), a.getEcSalt()))) {
			System.out.println("密码不正确！");
			return null;
		}
		//生成票据
		String ticket = UUIDUtil.getUUID();
		//存入redis，失效时间30分钟
		redisTemplate.opsForValue().set(userTicket + ":" + ticket, JsonUtil.object2JsonStr(a), 30, TimeUnit.MINUTES);
		return ticket;
	}


	/**
	 * 验证票据返回用户
	 *
	 * @param ticket
	 * @return
	 */
	@Override
	public Admin validate(String ticket) {
		if (StringUtils.isEmpty(ticket)) {
			System.out.println("票据不存在！");
			return null;
		}
		//从redis获取用户信息
		String adminJson = redisTemplate.opsForValue().get(userTicket + ":" + ticket);
		//判断是否存在
		if (StringUtils.isEmpty(adminJson)) {
			System.out.println("用户信息不存在！");
			return null;
		}
		//如果存在直接返回
		return JsonUtil.jsonStr2Object(adminJson, Admin.class);
	}

	/**
	 * 清除redis
	 *
	 * @param ticket
	 */
	@Override
	public void logout(String ticket) {
		redisTemplate.delete(userTicket + ":" + ticket);
	}
}