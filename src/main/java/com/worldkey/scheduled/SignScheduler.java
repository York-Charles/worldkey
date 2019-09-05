package com.worldkey.scheduled;

import com.worldkey.mapper.SignMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author wu
 * 签到记录定时每日删除
 * 23:59:59
 */
@Component
@Slf4j
public class SignScheduler {
    @Resource
    private SignMapper signMapper;

    @Scheduled(cron = "59 59 23 * * ?")
    public void deleteSign() {
        signMapper.deleteSign();
    }
}
