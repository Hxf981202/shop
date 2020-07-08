package com.xxxx.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
public class PageController {

	/**
	 * 页面跳转
	 *
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String welcome(@PathVariable String page) {
		return page;
	}


	/**
	 * 页面跳转
	 *
	 * @return
	 */
	@RequestMapping("/")
	public String welcome() {
		return "index";
	}


	/**
	 * 页面跳转
	 *
	 * @return
	 */
	@RequestMapping("/login")
	public String login(String redirectUrl, Model model) {
		model.addAttribute("redirectUrl",redirectUrl);
		return "login";
	}

}