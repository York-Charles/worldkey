package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.GiftBagRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author wu
 */
@Repository
public interface GiftBagRecordMapper extends BaseMapper<GiftBagRecord> {
    /**
     * 根据用户和礼包id查找礼包记录
     */
    @Select("SELECT gift_bag_record_id FROM gift_bag_record WHERE users=#{users} AND gift_bag_id=#{giftBagId}")
    Integer findByUidAndGBid(@Param("users") Long users, @Param("giftBagId") Integer giftBagId);

    /**
     * 周礼包 根据礼包id删除
     */
    @Delete("DELETE  FROM gift_bag_record WHERE gift_bag_id=#{giftBagId}")
    int deleteByGBId(@Param("giftBagId") Integer giftBagId);

}
