package com.worldkey.interceptor;

import com.worldkey.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsersManageInterceptor  implements HandlerInterceptor {

	private Logger log=LoggerFactory.getLogger(UsersManageInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Users users=(Users) request.getSession().getAttribute("users");
		if (users==null) {
			//System.out.println("请先登录");
			log.debug("未登录  -》"+"\tip:"+request.getHeader("host")+"\turl:"+request.getRequestURL());
			//response.sendRedirect("/users/login");
			request.getRequestDispatcher("manage/login");//.forward(request,response);
			return false;
		}
		log.debug("users:"+users.getLoginName()+"\tip:"+request.getHeader("host")+"\turl:"+request.getRequestURL());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
