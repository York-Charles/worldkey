package com.worldkey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GuoWei {

    @RequestMapping("aa")
    @ResponseBody
    public Integer aaa() {
        double ceil = Math.ceil(Math.random() * 8);
        /*Integer.parseInt(ceil+"");*/
        return (int) ceil+1;
    }

}
