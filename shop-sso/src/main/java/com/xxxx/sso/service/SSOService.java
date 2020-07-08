package com.xxxx.sso.service;

import com.xxxx.common.pojo.Admin;

/**
 * @author zhoubin
 * @since 1.0.0
 */
public interface SSOService {

	/**
	 * 验证用户返回票据
	 * @param admin
	 * @return
	 */
	String login(Admin admin);


	/**
	 * 验证票据返回用户
	 * @param ticket
	 * @return
	 */
	Admin validate(String ticket);

	/**
	 * 清除redis
	 * @param ticket
	 */
	void logout(String ticket);
}
