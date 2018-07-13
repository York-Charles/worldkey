package com.worldkey.listener;

import com.worldkey.entity.SystemConfig;
import com.worldkey.mapper.SystemConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author HP
 */
@Component
public class SystemConfigListener implements ApplicationListener<ContextRefreshedEvent> {
	@Resource
	private SystemConfigMapper sMapper;
	private static final Logger log=LoggerFactory.getLogger(SystemConfigListener.class);
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		SystemConfig sConfig=sMapper.find();
		log.info("应用启动完成");
		log.info(sConfig.toString());
	}
}
