package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.worldkey.config.SystemProperties;
import com.worldkey.entity.JdDetail;
import com.worldkey.entity.Sign;
import com.worldkey.entity.Users;
import com.worldkey.enumpackage.JdRewardType;
import com.worldkey.mapper.JdDetailMapper;
import com.worldkey.mapper.SignMapper;
import com.worldkey.service.SignService;
import com.worldkey.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
/**
 * @author wu
 */
@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements SignService {

    @Resource
    private UsersService usersService;
    @Resource
    private SignMapper signMapper;
    @Resource
    private SystemProperties systemProperties;
    @Resource
    private JdDetailMapper jdDetailMapper;

    @Override
    public Integer dailySign(String token) {
        //通过token，获取用户对象,从而获取用户id
        Users users = usersService.findByToken(token);
        Long uId = users.getId();

        //获取签到表对象
        Integer signId = this.signMapper.selectByUid(uId);
        Sign sign = this.signMapper.selectById(signId);

        //未曾签到，则执行签到
        if (sign == null) {
            //创建新的签到对象
            Sign s0 = new Sign();
            s0.setUsers(uId);
            s0.setCreateTime(new Date());
            //增加用户金豆数量
            usersService.addJd(systemProperties.getDailySignJd(), uId);
            this.signMapper.insert(s0);
            //插入金豆收支明细
            JdDetail jdDetailIn = new JdDetail(new Date(), systemProperties.getDailySignJd(), JdRewardType.signReward.getIndex(), users.getId());
            jdDetailIn.setMsg("签到获得" + systemProperties.getDailySignJd() + "金豆");
            this.jdDetailMapper.insert(jdDetailIn);
            //签完 返回1
            return 1;
        }
        return 0;
    }
}
