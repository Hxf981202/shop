package com.xxxx.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("/user")
public class UserController {


	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		return "redirect:" + request.getSession().getServletContext().getAttribute("portalUrl") + "user/logout";
	}

}