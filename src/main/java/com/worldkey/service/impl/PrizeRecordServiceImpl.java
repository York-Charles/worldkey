package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.worldkey.entity.PrizeRecord;
import com.worldkey.mapper.PrizeRecordMapper;
import com.worldkey.service.PrizeRecordService;
import org.springframework.stereotype.Service;

@Service
public class PrizeRecordServiceImpl extends ServiceImpl<PrizeRecordMapper,PrizeRecord> implements PrizeRecordService {
}
