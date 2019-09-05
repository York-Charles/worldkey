package com.worldkey.controller;

import com.worldkey.check.user.UserReg;
import com.worldkey.entity.Users;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.CaptService;
import com.worldkey.service.UsersService;
import com.worldkey.service.impl.UserAddService;
import com.worldkey.util.ResultUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

/**
 * 用户信息修改类
 * 手机号绑定
 * 修改密码
 *
 * @author DB161
 */
@Slf4j
@RestController
public class UserAddController {
    @Resource
    private UserAddService userAddService;
    @Resource
    private UsersService usersService;
    @Resource
    private CaptService captService;
    @Resource
    private UsersMapper uMapper;


    /**
     * 绑定手机号
     */
    @RequestMapping("/addtel/{token}")
    public ResultUtil addTel(@PathVariable String token, String phone, String code) throws Exception {

        Users users = usersService.findByToken(token);
        if (users == null) {
            return new ResultUtil(100, "no", "请先登录");
        }
        Users a = userAddService.addTel(users, token, code, phone);

        if (a == null) {
            return new ResultUtil(406, "no", "绑定失败");
        }
        return new ResultUtil(200, "ok", "绑定成功");
    }

//    /**
//     * 6.15绑定手机号（不需要token）
//     */
//    @RequestMapping("addtel1")
//    public ResultUtil addTel1(String phone, String code) throws Exception{
//    	Users user = new Users();
//    	user.setTelNum(phone);
//    	Users u = userAddService.addTel1(code, phone);
//    	 if (u == null) {
//             return new ResultUtil(406, "no", "绑定失败");
//         }
//         return new ResultUtil(200, "ok", u.getId()+"");
//    }
    /**
     * 12.11绑定手机号（不需要token）
     */
    @RequestMapping("bindTel")
    public ResultUtil addTel1(String phone, String code) throws Exception{
    	Users user = new Users();
    	user.setTelNum(phone);
    	
    	 Users users1 = uMapper.selectBytelNum(phone);
     	String codeCo = captService.getCapt(phone);
        if (!Objects.equals(code, codeCo)) {
        	return new ResultUtil(200, "no", "验证码错误");
//            throw new Exception("code error");
        }
         if (users1 != null) {
        	 return new ResultUtil(200, "no", "已被绑定");
//             throw new Exception("telNum is exist");
         }
         Users u = userAddService.addTel1(code, phone);
    	 if (u == null) {
             return new ResultUtil(406, "no", "绑定失败");
         }
    	 
         return new ResultUtil(200, "ok", u.getId()+"");
    }
//    /**
//     * 更换手机号
//     */
//    @RequestMapping("/updatetel/{token}")
//    public ResultUtil updateTel(@PathVariable String token, String phone, String code) throws Exception {
//        Users users = usersService.findByToken(token);
//        if (users == null) {
//            return new ResultUtil(100, "no", "请先登录");
//        }
//        Users a = userAddService.updateTel(users, token, code, phone);
//
//        if (a == null) {
//            return new ResultUtil(406, "no", "更换失败");
//        }
//        return new ResultUtil(200, "ok", "更换成功");
//    }
    /**
     * 更换手机号
     */
    @SuppressWarnings("unused")
	@RequestMapping("/updatetel")
    public ResultUtil updateTel(String token, String phone, String code){
        Users users = usersService.findByToken(token);
        if (users == null) {
            return new ResultUtil(100, "no", "请先登录");
        }
        Users a;
		try {
			a = userAddService.updateTel(users, token, code, phone);
		} catch (Exception e) {
			if(e.getMessage().equals("验证码")){
				return new ResultUtil(300, "error", "验证码输入错误！");
			}
			else{
				return new ResultUtil(400, "error", "手机号已存在");
			}
		}

//        if (a == null) {
//            return new ResultUtil(406, "no", "更换失败");
//        }
        return new ResultUtil(200, "ok", "更换成功");
    }

    /**
     * 核对用户
     * 传入用户名,手机号,验证码,
     */

    @RequestMapping("/checkpwd")
    public ResultUtil checkPwd(@Validated Users u, BindingResult errors, String code) throws Exception {
        if (errors.hasFieldErrors()) {
            return new ResultUtil(406, "no", "修正参数");
        }
        //2019.1.13修改，取消serviceimpl抛异常，改为返回值
        String codeCo = captService.getCapt(u.getTelNum());
        Users u1 = this.uMapper.selectBytelNum(u.getTelNum());
        if(u1==null){
        	return new ResultUtil(300,"no","用户不存在，或未绑定该手机号");
        }
        if (Objects.isNull(codeCo) || !Objects.equals(codeCo, code)) {
            return new ResultUtil(400,"验证码错误",code);
        }//
        
        
        
        String temToken;
//        if(u.getLoginName()==null){
        	 temToken = this.userAddService.checkPwd1(u.getTelNum(), code, 300);
        	
//        }else{
//        	 temToken = this.userAddService.checkPwd(u.getLoginName(), u.getTelNum(), code, 300);
//        }
        if (temToken == null) {
            return new ResultUtil(406, "no", "校对错误");
        }
        return new ResultUtil(200, "ok", temToken);
    }

    /**
     * 传入校验临时token即:temToken
     * 与缓存的相比较,
     * 然后修改密码
     */
    @SuppressWarnings("unused")
	@RequestMapping("/updatepwd")
    public ResultUtil updatePwd(@Validated({UserReg.class}) Users u, BindingResult result, String temToken,String telNum) {
//        Users user=usersService.findByToken(token);
    	Users user=usersService.userTelNum(telNum);
        if (result.hasFieldErrors()) {
            return new ResultUtil(406, "no", "修正参数");
        }
        Users a = this.userAddService.updatePwd(u, temToken);
        Map<String,String> map=new HashMap<String,String>();
        map.put("oldPassword", user.getPassword());
//        map.put("newPassword", a.getPassword());
        log.info("1111++++++++++"+a.getPassword());
        map.put("newPassword", u.getPassword());
        if (a == null) {
            return new ResultUtil(406, "no", "参数错误");
        }
        return new ResultUtil(200, "修改成功 ", map);

    }
}
