package com.worldkey;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(value={"com.worldkey.mapper"})
@EnableAsync
@EnableScheduling
@ServletComponentScan
public class WorldkeyApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(WorldkeyApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WorldkeyApplication.class);
	}
	
	@Bean("errorPageFilter")
	public ErrorPageFilter errorPageFilter() {
	    return new ErrorPageFilter();
	}

	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(@Qualifier("errorPageFilter") ErrorPageFilter filter) {
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false);
	    return filterRegistrationBean;
	}
}
