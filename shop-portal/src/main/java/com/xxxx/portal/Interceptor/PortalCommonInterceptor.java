package com.xxxx.portal.Interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Component
public class PortalCommonInterceptor implements HandlerInterceptor {

	@Value("${shop.order.url}")
	private String orderUrl;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		ServletContext context = request.getSession().getServletContext();
		//取值
		String url = (String) context.getAttribute("orderUrl");
		//判断值是否存在，不存在就存值
		if (StringUtils.isEmpty(url)){
			//存值
			context.setAttribute("orderUrl", this.orderUrl);
		}
		return true;
	}
}