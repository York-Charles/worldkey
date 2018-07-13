package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.JdDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JdDetailMapper extends BaseMapper<JdDetail> {
    @Select("SELECT * FROM jd_detail WHERE users=#{users} ORDER BY jd_id DESC")
    List<JdDetail> findByUid(@Param("users") Long users);
    
    //<---以下方法---薛秉承--2018.4.11写入-->
    @Select("SELECT * FROM jd_detail WHERE users=#{users} and jd_num>0 ORDER BY jd_id DESC")
    List<JdDetail> findByjust(@Param("users") Long users);
}
