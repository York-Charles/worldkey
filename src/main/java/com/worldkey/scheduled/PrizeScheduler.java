package com.worldkey.scheduled;

import com.worldkey.controller.PrizeController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PrizeScheduler {
    @Scheduled(cron = "59 59 23 28 2 ?")
    public void canUse(){
        PrizeController.canUse=false;
        log.info("抽奖系统关闭了:"+PrizeController.canUse);
    }

}
