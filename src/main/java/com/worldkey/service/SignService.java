package com.worldkey.service;

import com.baomidou.mybatisplus.service.IService;
import com.worldkey.entity.Sign;

/**
 * @author wu
 */
public interface SignService extends IService<Sign> {
    /**
     * 每日签到
     *
     * @param token
     * @return
     */
    Integer dailySign(String token);
}
