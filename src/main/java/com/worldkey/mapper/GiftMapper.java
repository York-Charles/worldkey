package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.Gift;
import com.worldkey.entity.GiftRecordApp;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftMapper extends BaseMapper<Gift> {
	
    @Select("SELECT gift.name "+
    		"from gift "+
    		"INNER JOIN gift_record on gift.gift_id = gift_record.gift_id "+
    		"where gift_record.gift_id=#{id} ")
    List<String> GiftName(Integer id);
    
    @Select("select gift_id,name,zs_price,jd_price from gift")
    List<Gift> getAllGifts();

    @Select("select gift_id,zs_price,jd_price from gift where gift_id=#{id}")
    Gift getPriceById(Integer id);
}
