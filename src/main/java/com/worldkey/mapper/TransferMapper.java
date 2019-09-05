package com.worldkey.mapper;

import com.worldkey.entity.Transfer;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferMapper {
    int deleteByPrimaryKey(String outBizNo);

    int insert(Transfer record);

    int insertSelective(Transfer record);

    Transfer selectByPrimaryKey(String outBizNo);

    int updateByPrimaryKeySelective(Transfer record);

    int updateByPrimaryKey(Transfer record);
}