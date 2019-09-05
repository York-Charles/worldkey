package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Users;
import com.worldkey.entity.ZsDetail;
import com.worldkey.service.UsersService;
import com.worldkey.service.ZsDetailService;
import com.worldkey.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("zs")
public class ZsDetailController {
    @Resource
    private UsersService usersService;
    @Resource
    private ZsDetailService zsDetailService;

    /**
     * 钻石明细接口
     */
    @RequestMapping("/listByUsers/{token}")
    public ResultUtil listByUsers(@PathVariable String token,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "20") Integer pageSize) {
    	PageHelper.startPage(page, pageSize, true);
        Users byToken = usersService.findByToken(token);
        if (byToken == null) {
            return new ResultUtil(406, "no", "未登录");
        }
        PageInfo<ZsDetail> zsDetails = zsDetailService.findByUid(page, pageSize, byToken.getId());
        return new ResultUtil(200, "ok", zsDetails);




        /*
        此分页方法有问题，PageNum=1时显示正常，当pageNUm=2以后。查出的数据顺序混乱
        EntityWrapper<ZsDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("users", byToken.getId()).orderBy("zs_id",false);
        Page<ZsDetail> zsDetailPage = zsDetailService.selectPage(new Page<>(page, pageSize),entityWrapper);
        return new ResultUtil(200, "ok", zsDetailPage);
        */
    }
    
    //<---以下方法---薛秉臣--2018.4.11写入-->
    @RequestMapping("/ZsjustByUsers/{token}")
    public ResultUtil listByjust(@PathVariable String token,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "20") Integer pageSize) {
        Users byToken = usersService.findByToken(token);
        if (byToken == null) {
            return new ResultUtil(406, "no", "未登录");
        }
        PageInfo<ZsDetail> zsDetails = zsDetailService.findByZsjust(page, pageSize, byToken.getId());
        return new ResultUtil(200, "ok", zsDetails);
    }
    
}
