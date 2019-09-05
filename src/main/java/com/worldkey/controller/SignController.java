package com.worldkey.controller;

import com.worldkey.entity.Sign;
import com.worldkey.entity.Users;
import com.worldkey.mapper.SignMapper;
import com.worldkey.service.SignService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wu
 */
@Slf4j
@RestController
@RequestMapping("sign")
public class SignController {
    @Resource
    private SignService signService;
    @Resource
    UsersService usersService;

    @Resource
    private SignMapper signMapper;

//    @RequestMapping("dailySign/{token}")
//    public ResultUtil sign(@PathVariable String token) {
//
//        //判定签到用户登录
//        Users users = this.usersService.findByToken(token);
//        if (users == null) {
//            return new ResultUtil(200, "no", "user null");
//        }
//
//        Integer i = this.signService.dailySign(token);
//
//        if (i == 1) {
//            return new ResultUtil(200, "ok", "Sign success");
//
//        } else if (i == 0) {
//        }
//        return new ResultUtil(200, "ok", "Sign fail");
//    }
    @RequestMapping("dailySign")
    public ResultUtil sign(String token) {

        //判定签到用户登录
        Users users = this.usersService.findByToken(token);
        if (users == null) {
            return new ResultUtil(200, "no", "user null");
        }

        Integer i = this.signService.dailySign(token);

        if (i == 1) {
            return new ResultUtil(200, "ok", "Sign success");

        } else if (i == 0) {
        }
        return new ResultUtil(200, "ok", "Sign fail");
    }

//    @RequestMapping("SignStatus/{token}")
//    public ResultUtil signStatus(@PathVariable String token) {
//        //判定签到用户登录
//        Users users = this.usersService.findByToken(token);
//        if (users == null) {
//            return new ResultUtil(200, "no", "user null");
//        }
//        Integer signId = this.signMapper.selectByUid(users.getId());
//        Sign sign = this.signMapper.selectById(signId);
//        if (sign == null) {
//            return new ResultUtil(200, "ok", "not sign");
//        }
//        return new ResultUtil(200, "ok", "had sign");
//    }
    @RequestMapping("SignStatus")
    public ResultUtil signStatus(String token) {
        //判定签到用户登录
        Users users = this.usersService.findByToken(token);
        if (users == null) {
            return new ResultUtil(200, "no", "user null");
        }
        Integer signId = this.signMapper.selectByUid(users.getId());
        Sign sign = this.signMapper.selectById(signId);
        if (sign == null) {
            return new ResultUtil(200, "ok", "not sign");
        }
        return new ResultUtil(200, "ok", "had sign");
    }
}
