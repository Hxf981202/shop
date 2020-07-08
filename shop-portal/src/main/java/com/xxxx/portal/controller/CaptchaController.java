package com.xxxx.portal.controller;

import com.xxxx.common.result.BaseResult;
import com.xxxx.portal.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
public class CaptchaController {
	@Autowired
	private CaptchaService captchaService;

	/**
	 * 验证校验码
	 *
	 * @param ticket
	 * @param randStr
	 * @return
	 */
	@RequestMapping("getCaptcha")
	@ResponseBody
	public BaseResult getCaptcha(String ticket, String randStr) {
		return captchaService.captcha(ticket, randStr);
	}

}