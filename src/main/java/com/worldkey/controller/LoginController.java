package com.worldkey.controller;

import com.worldkey.check.user.Login;
import com.worldkey.entity.Users;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
	@Resource
	private UsersService uService;

	private Logger log=LoggerFactory.getLogger(LoginController.class);
	/**
	 * 判定token
	 * 1.token为空，显示no,返回0
	 * 2.匹配正确，显示OK，返回token
	 */
	@PostMapping("/login")
	public ResultUtil login(@Validated(Login.class) Users u, BindingResult result){
		if (result.hasErrors()) {
			Map<String,String>map=new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				map.put(error.getField(), error.getDefaultMessage());
			}
			return new ResultUtil(406, "no", map);
		}

		log.debug("user:"+u.toString());
		String  token=this.uService.login(u.getLoginName(), u.getPassword());

		log.info("userLogin:"+token);
		if (token==null) {
			return new ResultUtil(406, "no", "0") ;
		}
		if ("1".equals(token)) {
			return new ResultUtil(406, "no", "1") ;
		}
		return new ResultUtil(200, "ok", token);
	}
	@RequestMapping(value="/logout")
	public ResultUtil logout(@RequestParam(value="token")String token){
		this.uService.logout(token);
		return new ResultUtil(200, "ok", "1") ;
	}

}
