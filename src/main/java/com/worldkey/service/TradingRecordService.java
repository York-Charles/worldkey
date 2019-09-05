package com.worldkey.service;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.TradingRecord;

public interface TradingRecordService {

    int add(TradingRecord record);
    PageInfo<TradingRecord>findByAuthor(Long author, Integer pageNum, Integer pageSize);

}
