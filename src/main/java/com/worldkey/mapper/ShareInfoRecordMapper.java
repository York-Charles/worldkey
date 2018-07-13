package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.JdDetail;
import com.worldkey.entity.Praise;
import com.worldkey.entity.ShareInfoRecord;
import com.worldkey.entity.ShareInfoRecordApp;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wu
 */
@Repository
public interface ShareInfoRecordMapper extends BaseMapper<ShareInfoRecord> {

    @Select("SELECT * FROM share_info_record WHERE users=#{userId} ")
    List<ShareInfoRecord> findByuId(@Param("userId") Long userId);

    @Select("SELECT * FROM share_info_record WHERE users=#{userId} AND information=#{informationId}")
    List<ShareInfoRecord> findByUidANDInfoId(@Param("userId") Long userId, @Param("informationId") Long informationId);

    @Delete("DELETE FROM share_info_record")
    int deleteRecord();

    /**
     * 从金豆明细表获取
     * 发布文章次数查询  条件是  用户  类型为发布3 时间是当天时间
     * TO_DAYS(create_time)=TO_DAYS(NOW())   查询的是字段 create_time 当天时间内发生的事
     */
    @Select("SELECT * FROM jd_detail WHERE users=#{userId} AND type=3 AND TO_DAYS(create_time)=TO_DAYS(NOW()) ")
    List<JdDetail> findByuIdANDTime(@Param("userId") Long userId);
    
    
    
    //5.18
    @Select("SELECT share_info_record.information,share_info_record.create_time "+
    		"from share_info_record "+
    		"INNER JOIN information_all on share_info_record.information = information_all.id  "+
    		"INNER JOIN users ON users.id = information_all.users "+
    		"WHERE users.id=#{userId} order by share_info_record.create_time desc")
    List<ShareInfoRecord> Share(Long userId);
    
    @Select("select share_info_record.users from share_info_record "+
    		"INNER JOIN information_all on share_info_record.information = information_all.id "+
    		"where information_all.users=#{userId} order by share_info_record.create_time desc")
    List<Long> chooseUsers(Long userId);

}
