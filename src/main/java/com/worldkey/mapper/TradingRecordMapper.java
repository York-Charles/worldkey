package com.worldkey.mapper;

import com.worldkey.entity.TradingRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingRecordMapper {
    @Insert("insert into trading_record(" +
            "`user`, itemID, payType, sellerEmail, `subject`, buyerId, appId, sellerId, totalAmount," +
            "tradeNo, buyerLogonId, author,createTime,gmtCreate)  "+
            " values (" +
            "#{user}, #{itemID}, #{payType}, #{sellerEmail}, #{subject}, #{buyerId}, #{appId}, #{sellerId}," +
            "#{totalAmount}, #{tradeNo}, #{buyerLogonId}, #{author},#{createTime},#{gmtCreate})")
    int add(TradingRecord record);
     @Select("select * from trading_record where author=#{author}")
     List<TradingRecord>findByAuthor(Long author);
}
