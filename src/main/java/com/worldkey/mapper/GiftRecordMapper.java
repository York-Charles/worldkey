package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.GiftRecord;
import com.worldkey.entity.GiftRecordApp;
import com.worldkey.entity.GiftShow;
import com.worldkey.entity.Praise;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GiftRecordMapper extends BaseMapper<GiftRecord> {
    @Select("SELECT gr.gift_id AS giftId,COUNT(*) AS num,g.gift_img AS imgUrl" +
            " FROM" +
            " gift_record AS gr LEFT JOIN gift AS g ON gr.gift_id=g.gift_id" +
            " WHERE gr.to_information = #{toInformation}" +
            " GROUP BY gr.gift_id")
    List<Map<String,Object>> selectGiftRecordByToInformation(@Param(value = "toInformation") Long id);
    
    
    
    
  //5.18
    @Select("SELECT gift_record.to_information,gift_record.create_time,gift_record.gift_id "+
    		"from gift_record "+
    		"INNER JOIN information_all on gift_record.to_information = information_all.id  "+
    		"INNER JOIN users ON users.id = information_all.users "+
    		"WHERE users.id=#{userId} order by gift_record.create_time desc")
    List<GiftRecord> Gift(Long userId);
    

    
    @Select("select gift_record.users from gift_record "+
    		"INNER JOIN information_all on gift_record.to_information = information_all.id "+
    		"where information_all.users=#{userId} order by gift_record.create_time desc")
    List<Long> chooseUsers(Long userId);
    
    @Select("select count(*) as giftNum from gift_record where to_information IN ( select i.id from  information_all as i where users=#{userId} )")	
    Integer selectGiftNum(Integer userId);
    
    @Select("select gift_record.gift_id AS giftId,gift.name AS giftName,gift.gift_img AS giftImg,count(gift_record.gift_id) AS giftNum,gift.jd_price as jd,gift.zs_price as zs from gift_record " +
    		"INNER JOIN information_all on gift_record.to_information=information_all.id " +
    		"INNER JOIN users on users.id=information_all.users " +
    		"INNER JOIN gift on gift.gift_id=gift_record.gift_id " +
    		"where users.id=#{userId} group by gift_record.gift_id ")
    List<GiftShow> findNum(Long userId);
    
	    @Select("select g.gift_id from gift_record as g "+
				"INNER JOIN information_all as i on i.id=g.to_information "+
				"INNER JOIN users as u on u.id=i.users where u.id=#{id} and g.users=#{userId}")
    List<Integer> getAllGiftsByPresentors(@Param("id")Long id,@Param("userId")Long userId);

}
