package com.xxxx.portal.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoubin
 * @since 1.0.0
 */
public interface CookieService {

	/**
	 * 存入cookie
	 * @param request
	 * @param response
	 * @param ticket
	 */
	boolean setCookie(HttpServletRequest request, HttpServletResponse response, String ticket);

	/**
	 * 获取票据
	 * @param request
	 * @return
	 */
	String getCookie(HttpServletRequest request);

	/**
	 * 清除cookie
	 * @param request
	 */
	void deleteCookie(HttpServletRequest request, HttpServletResponse response);
}
