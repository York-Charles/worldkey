package com.worldkey.controller;

import com.worldkey.check.user.UserReg;
import com.worldkey.entity.Users;
import com.worldkey.service.UsersService;
import com.worldkey.service.impl.UserAddService;
import com.worldkey.util.ResultUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户信息修改类
 * 手机号绑定
 * 修改密码
 *
 * @author DB161
 */
@RestController
public class UserAddController {
    @Resource
    private UserAddService userAddService;
    @Resource
    private UsersService usersService;


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

    /**
     * 6.15绑定手机号（不需要token）
     */
    @RequestMapping("addtel1")
    public ResultUtil addTel1(String phone, String code) throws Exception{
    	Users user = new Users();
    	user.setTelNum(phone);
    	Users u = userAddService.addTel1(code, phone);
    	 if (u == null) {
             return new ResultUtil(406, "no", "绑定失败");
         }
         return new ResultUtil(200, "ok", u.getId()+"");
    }
    /**
     * 更换手机号
     */
    @RequestMapping("/updatetel/{token}")
    public ResultUtil updateTel(@PathVariable String token, String phone, String code) throws Exception {
        Users users = usersService.findByToken(token);
        if (users == null) {
            return new ResultUtil(100, "no", "请先登录");
        }
        Users a = userAddService.updateTel(users, token, code, phone);

        if (a == null) {
            return new ResultUtil(406, "no", "更换失败");
        }
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
    @RequestMapping("/updatepwd")
    public ResultUtil updatePwd(@Validated({UserReg.class}) Users u, BindingResult result, String temToken) {
        if (result.hasFieldErrors()) {
            return new ResultUtil(406, "no", "修正参数");
        }
        Users a = this.userAddService.updatePwd(u, temToken);
        if (a == null) {
            return new ResultUtil(406, "no", "参数错误");
        }
        return new ResultUtil(200, "ok", "修改成功");

    }
}
