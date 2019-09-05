package com.worldkey.controller;

import com.worldkey.entity.Users;
import com.worldkey.exception.Z406Exception;
import com.worldkey.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class InvitationController {
    @Resource private UsersService usersService;
    @RequestMapping("invitation/{loginName}")
    public String invitation(@PathVariable String loginName, Model model) throws Z406Exception {
        Users byToken = usersService.findByLoginName(loginName);
        if (byToken==null){
            throw new Z406Exception("未登录");
        }
        model.addAttribute("petName",byToken.getPetName());
        model.addAttribute("loginName",byToken.getLoginName());
        return "users/invitation";
    }


}
