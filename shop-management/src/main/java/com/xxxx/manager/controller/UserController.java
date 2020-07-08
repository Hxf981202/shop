package com.xxxx.manager.controller;

import com.xxxx.common.enums.BaseResultEnum;
import com.xxxx.common.pojo.Admin;
import com.xxxx.common.result.BaseResult;
import com.xxxx.manager.service.CookieService;
import com.xxxx.sso.service.SSOService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Reference(version = "1.0")
	private SSOService ssoService;
	@Autowired
	private CookieService cookieService;


	/**
	 * 登录
	 *
	 * @param admin
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public BaseResult login(Admin admin, HttpServletRequest request, HttpServletResponse response, String verify) {
		BaseResult baseResult = new BaseResult();
		String pictureVerifyKey = (String) request.getSession().getAttribute("pictureVerifyKey");
		if (StringUtils.isEmpty(verify) || !verify.equals(pictureVerifyKey)) {
			baseResult.setCode(BaseResultEnum.PASS_ERROR_03.getCode());
			baseResult.setMessage(BaseResultEnum.PASS_ERROR_03.getMessage());
			return baseResult;
		}
		//验证用户返回票据
		String ticket = ssoService.login(admin);
		//判票据是否存在
		if (StringUtils.isEmpty(ticket)) {
			return BaseResult.error();
		}
		//将票据存入cookie
		boolean result = cookieService.setCookie(request, response, ticket);
		if (result) {
			//方便页面返显
			request.getSession().setAttribute("user", admin);
			return BaseResult.success();
		}
		return BaseResult.error();
	}


	/**
	 * 退出
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		//获取ticket
		String ticket = cookieService.getCookie(request);
		//判断ticket是否存在
		if (!StringUtils.isEmpty(ticket)) {
			//清除redis
			ssoService.logout(ticket);
			//清除session
			request.getSession().removeAttribute("user");
			//清除cookie
			cookieService.deleteCookie(request, response);
		}
		//跳转登录页面
		return "login";
	}
}