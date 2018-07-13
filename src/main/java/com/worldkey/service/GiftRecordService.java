package com.worldkey.service;

import com.baomidou.mybatisplus.service.IService;
import com.worldkey.entity.GiftRecord;
import com.worldkey.entity.GiftRecordApp;
import com.worldkey.entity.GiftShow;

import java.util.List;
import java.util.Map;

public interface GiftRecordService extends IService<GiftRecord> {
    List<Map<String,Object>> findGiftRecordByToInformation(Long id);
    
    
    //5.18根据ID查找所有文章的所有操作
    List<GiftRecordApp> gift(Long userId);
    
    List<Map<String,Object>> findNum(Long userId);
}
