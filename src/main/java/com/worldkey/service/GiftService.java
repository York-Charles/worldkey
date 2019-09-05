package com.worldkey.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Gift;
import com.worldkey.entity.GiftRecordApp;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.Users;

import java.util.List;

public interface GiftService extends IService<Gift> {
    Integer giveGift(Users users, Integer giftId, Long toInformation);
    
    Integer giftUsers(String token,Integer giftId,Long userId);
    
    List<Gift> getUrl();
}
