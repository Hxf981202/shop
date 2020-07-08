package com.xxxx.manager.service.impl;

import com.xxxx.common.untils.CookieUtil;
import com.xxxx.manager.service.CookieService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service
public class CookieServiceImpl implements CookieService {

	/**
	 * 存入cookie
	 *
	 * @param request
	 * @param response
	 * @param ticket
	 */
	@Override
	public boolean setCookie(HttpServletRequest request, HttpServletResponse response, String ticket) {
		try {
			CookieUtil.setCookie(request,response,"userTicket",ticket);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取票据
	 * @param request
	 * @return
	 */
	@Override
	public String getCookie(HttpServletRequest request) {
		String ticket = CookieUtil.getCookieValue(request, "userTicket");
		if (StringUtils.isEmpty(ticket)){
			return null;
		}
		return ticket;
	}

	/**
	 * 清除cookie
	 * @param request
	 */
	@Override
	public void deleteCookie(HttpServletRequest request,HttpServletResponse response) {
		CookieUtil.deleteCookie(request,response,"userTicket");
	}
}