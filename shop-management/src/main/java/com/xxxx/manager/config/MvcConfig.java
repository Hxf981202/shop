package com.xxxx.manager.config;

import com.xxxx.manager.Interceptor.ManagerLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	@Autowired
	private ManagerLoginInterceptor loginInterceptor;

	/**
	 * 添加拦截器
	 * addInterceptor：添加拦截器
	 * addPathPatterns：拦截路径
	 *          /**:拦截所有
	 *  excludePathPatterns：放行路径
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/login/**")
				.excludePathPatterns("/image/**")
				.excludePathPatterns("/user/login/**")
				.excludePathPatterns("/user/logout/**")
				.excludePathPatterns("/static/**");
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}
}