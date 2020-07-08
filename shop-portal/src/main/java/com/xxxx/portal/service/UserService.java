package com.xxxx.portal.service;

import com.xxxx.common.pojo.AdminWithBLOBs;
import com.xxxx.common.result.BaseResult;

/**
 * @author zhoubin
 * @since 1.0.0
 */
public interface UserService {
	/**
	 * 注册
	 * @param admin
	 * @return
	 */
	BaseResult saveUser(AdminWithBLOBs admin);

}