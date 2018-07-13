package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.worldkey.entity.GiftBagRecord;
import com.worldkey.mapper.GiftBagRecordMapper;
import com.worldkey.service.GiftBagRecordService;
import org.springframework.stereotype.Service;

/**
 * @author wu
 * 购买礼包记录的实现
 */
@Service
public class GiftBagRecordServiceImpl extends ServiceImpl<GiftBagRecordMapper, GiftBagRecord> implements GiftBagRecordService {
}
