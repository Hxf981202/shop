package com.xxxx.order.Interceptor;

import com.xxxx.common.pojo.Admin;
import com.xxxx.common.untils.CookieUtil;
import com.xxxx.common.untils.JsonUtil;
import com.xxxx.sso.service.SSOService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 后台登录拦截器
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Component
public class OrderLoginInterceptor implements HandlerInterceptor {

	@Reference(version = "1.0")
	private SSOService ssoService;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Value("${user.ticket}")
	private String userTicket;
	@Value("${shop.portal.url}")
	private String portalUrl;

	/**
	 * 前置方法
	 *
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//获取票据
		String ticket = CookieUtil.getCookieValue(request, "userTicket");
		//判断票据是否存在
		if (!StringUtils.isEmpty(ticket)) {
			Admin admin = ssoService.validate(ticket);
			if (null != admin) {
				//存入session，方便页面显示
				request.getSession().setAttribute("user", admin);
				//重新设置缓存失效时间
				redisTemplate.opsForValue().set(userTicket + ":" + ticket, JsonUtil.object2JsonStr(admin), 30,
						TimeUnit.MINUTES);
				return true;
			} else {
				request.getSession().removeAttribute("user");
			}
		}
		//重定向登录页面
		response.sendRedirect(portalUrl + "login?redirectUrl="+request.getRequestURL());
		return false;
	}


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
	                       ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
	                            Exception ex) throws Exception {

	}
}