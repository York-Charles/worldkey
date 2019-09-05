package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.KbDetail;
import com.worldkey.entity.Users;
import com.worldkey.service.KbDetailService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("kb")
public class KbDetailController {

    @Resource
    private UsersService usersService;
    @Resource
    private KbDetailService kbDetailService;

    @RequestMapping("listByUsers/{token}")
    public ResultUtil page(@PathVariable String token,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "20") Integer pageSize) {
    	PageHelper.startPage(page, pageSize, true);
        Users byToken = usersService.findByToken(token);
        if (byToken == null) {
            return new ResultUtil(406, "no", "未登录");
        }
        PageInfo<KbDetail> kbDetails = kbDetailService.findByUid(page, pageSize, byToken.getId());
        return new ResultUtil(200, "ok", kbDetails);


        /*
        此分页方法有问题，PageNum=1时显示正常，当pageNUm=2以后。查出的数据顺序混乱
        EntityWrapper<KbDetail> kbWrapper = new EntityWrapper<>();
        kbWrapper.eq("users",byToken.getId());
        kbWrapper.orderBy("kb_id",false);
        return new ResultUtil(200, "ok", kbDetailService.selectPage(new Page<>(page, pageSize),kbWrapper));
        */
    }


}
