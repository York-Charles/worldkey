package com.worldkey.service;

import com.baomidou.mybatisplus.service.IService;
import com.worldkey.entity.GiftBag;

/**
 * @author wu
 */
public interface GiftBagService extends IService<GiftBag> {

    Integer buyGiftBag(String token, Integer giftBagId);
}
