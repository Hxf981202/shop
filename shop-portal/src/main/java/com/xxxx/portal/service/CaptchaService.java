package com.xxxx.portal.service;

import com.xxxx.common.result.BaseResult;

/**
 * @author zhoubin
 * @since 1.0.0
 */
public interface CaptchaService {
	/**
	 * 验证校验码
	 * @param ticket
	 * @param randStr
	 * @return
	 */
	BaseResult captcha(String ticket,String randStr);
}
