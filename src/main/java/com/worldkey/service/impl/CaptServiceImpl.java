package com.worldkey.service.impl;

import com.worldkey.service.CaptService;
import com.worldkey.util.SendMsgUtil;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 验证码存
 * @author DB161
 *
 */
@Service
@Slf4j
public class CaptServiceImpl implements CaptService{
	@Resource
	private RedisTemplate<String, String> redisTemplate;


	/**
	 * 获取验证码
	 */
	@Override
	@Cacheable(value="getCapt",key="#phone")
	public String getCapt(String phone){
		return null;
	}
	/**
	 * 发送验证码并将其存入redis中，
	 * @param phone   手机号
	 * @param timeout 过期时间
	 */
	@Override
	public String setCapt(String phone , Integer timeout){
		ValueOperations<String, String> vp = redisTemplate.opsForValue();

		String code=SendMsgUtil.getRandom();
		log.info(code + "-----------------sss");
		SendMsgUtil.SMS(code, phone, SendMsgUtil.alicode); //统一模板

		vp.set(phone, code, timeout, TimeUnit.SECONDS);

		return code;
	}
	@Override
	@Cacheable(key="#temToken",value="getTelNumAndCodeMD5")
	public String getTelNumAndCodeMD5(String temToken) {
		return null;
	}

}
