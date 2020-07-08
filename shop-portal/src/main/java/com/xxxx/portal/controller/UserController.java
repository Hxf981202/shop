package com.xxxx.portal.controller;

import com.xxxx.common.pojo.Admin;
import com.xxxx.common.pojo.AdminWithBLOBs;
import com.xxxx.common.result.BaseResult;
import com.xxxx.portal.service.CookieService;
import com.xxxx.portal.service.UserService;
import com.xxxx.rpc.service.SendMessageService;
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
	@Autowired
	private UserService userService;
	@Reference(version = "1.0")
	private SendMessageService sendMessageService;


	/**
	 * 登录
	 *
	 * @param admin
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public BaseResult login(Admin admin, HttpServletRequest request, HttpServletResponse response) {
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


	/**
	 * 注册
	 *
	 * @param admin
	 * @return
	 */
	@RequestMapping("register")
	@ResponseBody
	public BaseResult saveUser(AdminWithBLOBs admin) {
		BaseResult baseResult = userService.saveUser(admin);
		if (200==baseResult.getCode()){
			// baseResult = sendMessageService.sendMessage(admin.getEmail());
			return baseResult;
		}
		return BaseResult.error();
	}
}