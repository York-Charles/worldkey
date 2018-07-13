package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.KbDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KbDetailMapper extends BaseMapper<KbDetail> {
    @Select("SELECT * FROM kb_detail WHERE users=#{users} ORDER BY kb_id DESC")
    List<KbDetail> findByUid(@Param("users") Long users);
}
