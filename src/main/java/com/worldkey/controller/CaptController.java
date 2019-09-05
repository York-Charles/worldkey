package com.worldkey.controller;


import com.worldkey.entity.Users;
import com.worldkey.service.CaptService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 验证码控制器
 * @author DB161
 *
 */


@RestController
@RequestMapping("/captcha")
@SuppressWarnings("unused")
public class CaptController {
	private static final Logger log=LoggerFactory.getLogger(CaptController.class);

	@Resource
	private CaptService captService;
	@Resource 
	private UsersService uSevice;
	/***
	 * 校验验证码
	 * @param phone
	 * @param code
	 * @return
	 */
	@RequestMapping("/checkcapt")
	public ResultUtil checkCapt(String phone ,String code){
		String codeCo = captService.getCapt(phone);
		System.out.println("code :" + code);
		System.out.println("codeCo :" + codeCo);
		if(codeCo.equals(code)){
			log.info(phone+": 校验成功");
			return new ResultUtil(200 ,"ok" , 1);
		}
		return new ResultUtil(406 , "no" , 0);
	}
	/**
	 * 获取验证码
	 * @param phone
	 * @return
	 */
	@RequestMapping("/getCapt")
	public ResultUtil getCapt(String phone){
		try{
			String code = captService.setCapt(phone, 300);
			return  new ResultUtil(200 , "ok" ,code);
		}catch(Exception e){
			e.printStackTrace();
			return new ResultUtil(300 , "no" , 0);
		}
	}
	
	@RequestMapping("/getcapt1")
	public ResultUtil getCapt1(String phone){
		Users u = this.uSevice.findByTelNum(phone);
		if(u==null){
			return new ResultUtil(500,"error","用户不存在");
		}
		try{
			String code = captService.setCapt(phone, 300);
			return  new ResultUtil(200 , "ok" ,code);
		}catch(Exception e){
			e.printStackTrace();
			return new ResultUtil(300 , "no" , 0);
		}
	}
}
