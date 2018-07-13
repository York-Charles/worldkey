package com.worldkey.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.worldkey.config.SystemProperties;
import com.worldkey.entity.Prize;
import com.worldkey.entity.PrizeRecord;
import com.worldkey.entity.Users;
import com.worldkey.service.PrizeRecordService;
import com.worldkey.service.PrizeService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("prizeDraw")
@Slf4j
@Data
public class PrizeController {
    @Resource
    private PrizeService prizeService;
    @Resource
    private UsersService usersService;
    @Resource
    private SystemProperties systemProperties;
    @Resource
    private PrizeRecordService recordService;
    public static Boolean canUse=false;

    @PostMapping("prizes")
    public ResultUtil all() {
        List<Prize> list = prizeService.selectAll();
        return new ResultUtil(200, "ok", list);
    }

    @PostMapping("do/{token}")
    public ResultUtil do1(@PathVariable String token) {
        if (!canUse) {
            return new ResultUtil(200,"no","活动已结束");
        }
        Users byToken = usersService.findByToken(token);
        if (byToken == null) {
            return new ResultUtil(406, "no", "未登录");
        }
        Users jdAndZsAndKb = usersService.getJdAndZsAndKb(byToken.getId());
        if (jdAndZsAndKb.getZs() < systemProperties.getPrizeDraw()) {
            return new ResultUtil(500, "no", "余额不足,请充值");
        }
        Prize prize = this.prizeService.prizeDraw(jdAndZsAndKb);
        return new ResultUtil(200, "ok", prize);
    }

    @PostMapping("prizeRecord/{token}")
    public ResultUtil selectPrizeRecord(@PathVariable String token,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "20") Integer pageSize) {
        Users byToken = usersService.findByToken(token);
        if (byToken == null) {
            return new ResultUtil(406, "no", "未登录");
        }
        EntityWrapper<PrizeRecord> prizeRecordEntityWrapper = new EntityWrapper<>();
        prizeRecordEntityWrapper.orderBy("prize_record_id",false);
        prizeRecordEntityWrapper.eq("users",byToken.getId());
        Page<PrizeRecord> prizeRecordPage = recordService.selectPage(new Page<>(page, pageSize),prizeRecordEntityWrapper);
        return new ResultUtil(200, "ok", prizeRecordPage);
    }

}
