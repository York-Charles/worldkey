package com.worldkey.scheduled;

import com.worldkey.controller.GiftBagController;
import com.worldkey.mapper.GiftBagRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author wu
 */
@Component
@Slf4j
public class GiftBagScheduler {
    @Resource
    private GiftBagRecordMapper giftBagRecordMapper;

    /**
     * 周礼包定时删除
     * 每周日 23:59:59 删除
     */
    @Scheduled(cron = "59 59 23 ? * SUN")
    public void deleteWeekGB() {
        giftBagRecordMapper.deleteByGBId(2);
    }

    /**
     * 活动礼包 3月1日0:0:0 活动结束
     */
    @Scheduled(cron = "0 0 0 1 3 ?")
    public void festivalGiftBag() {
        GiftBagController.festivalGB = false;

    }
}
