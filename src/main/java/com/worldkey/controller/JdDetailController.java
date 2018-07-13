package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.JdDetail;
import com.worldkey.entity.Users;
import com.worldkey.service.JdDetailService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 金豆收支表对应的Controller
 */
@Slf4j
@RestController
@RequestMapping("jd")
public class JdDetailController {

    @Resource
    private UsersService usersService;
    @Resource
    private JdDetailService jdDetailService;

    /**
     * 获取用户的金豆收支明细
     */
    @PostMapping("listByUsers/{token}")
    public ResultUtil jdDetailList(@PathVariable String token,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "20") Integer pageSize) {
    	PageHelper.startPage(page, pageSize, true);
        Users byToken = usersService.findByToken(token);
        if (byToken == null) {
            return new ResultUtil(406, "no", "未登录");
        }
        PageInfo<JdDetail> jdDetails = jdDetailService.findByUid(page, pageSize, byToken.getId());
        return new ResultUtil(200, "ok", jdDetails);



        /*
        此分页方法有问题，PageNum=1时显示正常，当pageNUm=2以后。查出的数据顺序混乱
        EntityWrapper<JdDetail> jdWrapper = new EntityWrapper<>();
        jdWrapper.eq("users",byToken.getId());
        jdWrapper.orderBy("jd_id",false);
        Page<JdDetail> jdDetailPage = jdDetailService.selectPage(new Page<>(page, pageSize), jdWrapper);
        jdDetailPage.setTotal(jdDetailService.findByUsers(byToken.getId()).size());
        return new ResultUtil(200, "ok", jdDetailPage);
        */
    }
    
    //<---以下方法---薛秉承--2018.4.11写入-->
    @PostMapping("justByUsers/{token}")
    public ResultUtil jdDetail(@PathVariable String token,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "20") Integer pageSize) {
        Users byToken = usersService.findByToken(token);
        if (byToken == null) {
            return new ResultUtil(406, "no", "未登录");
        }
        PageInfo<JdDetail> jdDetails = jdDetailService.findByjust(page, pageSize, byToken.getId());
        return new ResultUtil(200, "ok", jdDetails);

    }
}
