package com.worldkey.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ConfigInterceptor extends WebMvcConfigurerAdapter {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/*
		 * 用户登录拦截器
		 */
		InterceptorRegistration usersLogin=registry.addInterceptor(new UsersManageInterceptor());
		usersLogin.addPathPatterns("/users/information/**");
		
			super.addInterceptors(registry);
	}
	
	
	
	
}
