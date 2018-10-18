package com.worldkey.shiro.config;

import com.worldkey.shiro.CredentialsMatcher;
import com.worldkey.shiro.realm.MyRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;


/**
  * Shiro 配置
  *
 Apache Shiro 核心通过 Filter 来实现，就好像SpringMvc 通过DispachServlet 来主控制一样。
 既然是使用 Filter 一般也就能猜到，是通过URL规则来进行过滤和权限校验，所以我们需要定义一系列关于URL的规则和访问权限。
  */
@Configuration
public class ShiroConfig {
	/**
	      * ShiroFilterFactoryBean 处理拦截资源文件问题。
	      * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
	      * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
	      *
	         Filter Chain定义说明
	        1、一个URL可以配置多个Filter，使用逗号分隔
	        2、当设置多个过滤器时，全部验证通过，才视为通过
	        3、部分过滤器可指定参数，如perms，roles
	      *
	      */
	private Logger log=LoggerFactory.getLogger(ShiroConfig.class);
	@Bean
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
		log.info("--->开始加载ShiroConfig.shiroFilter");
		ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
		//设置 securityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		//设置拦截器   我知道这样写有坑  但是 少年  加油吧
		//必须为LinkedHashMap ,以为需要有序
		Map<String, String> filterChainDefinitionMap=new LinkedHashMap<String,String>();
		//配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/admin/login*", "anon"); //表示可以匿名访问
		filterChainDefinitionMap.put("/admin/toLogin*", "anon"); //表示可以匿名访问
		filterChainDefinitionMap.put("/diet/find*", "anon");//表示可以匿名访问
		filterChainDefinitionMap.put("/hotel/find*", "anon");//表示可以匿名访问
		filterChainDefinitionMap.put("/system/tongZhi*", "anon");//表示可以匿名访问
		filterChainDefinitionMap.put("/informationall/find/**", "anon");//表示可以匿名访问
		filterChainDefinitionMap.put("/informationall/giftRecord/**", "anon");//表示可以匿名访问
		filterChainDefinitionMap.put("/informationall/info/**", "anon");//表示可以匿名访问
		filterChainDefinitionMap.put("/informationall/find/**", "anon");//表示可以匿名访问
		filterChainDefinitionMap.put("/informationall/find1/**", "anon");//表示可以匿名访问
		filterChainDefinitionMap.put("/informationall/tuijian", "anon");//表示可以匿名访问
		filterChainDefinitionMap.put("/informationall/findStick/**", "anon");
		filterChainDefinitionMap.put("/admin/logout**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/type/one/del/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/type/one/update/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/type/one/add*", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/type/two/del/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/type/two/add*", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/type/two/update/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/admin/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/action/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/diet/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/hotel/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/power/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/informationall/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/manage**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/*", "anon");
		filterChainDefinitionMap.put("/**", "anon");
		filterChainDefinitionMap.put("/*.*", "anon");
		
		
		//设置过滤器链
		shiroFilterFactoryBean.setLoginUrl("/users/login");
		shiroFilterFactoryBean.setSuccessUrl("/manage");
		// shiroFilterFactoryBean.setUnauthorizedUrl("");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;

	}
	//配置核心安全事务管理器
	@Bean(name="securityManager")
	public SecurityManager securityManager(@Qualifier("authRealm")MyRealm realm) {
		DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
		securityManager.setRealm(realm);
		return securityManager;
	}

	//配置自定义的权限登录器
	@Bean(name="authRealm")
	public MyRealm authRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher) {
		MyRealm authRealm=new MyRealm();
		authRealm.setCredentialsMatcher(matcher);
		return authRealm;
	}
	//配置自定义的密码比较器
	@Bean(name="credentialsMatcher")
	public CredentialsMatcher credentialsMatcher() {
		return new CredentialsMatcher();
	}
	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
		AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(manager);
		return advisor;
	}
	
}
