package com.worldkey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.TradingRecord;
import com.worldkey.mapper.TradingRecordMapper;
import com.worldkey.service.TradingRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TradingRecordServiceImpl implements TradingRecordService {

    @Resource
    private TradingRecordMapper tradingRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public int add(TradingRecord record) {
        return tradingRecordMapper.add(record);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public PageInfo<TradingRecord> findByAuthor(Long author, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize,true);
        return new PageInfo<>(this.tradingRecordMapper.findByAuthor(author));
    }
}
