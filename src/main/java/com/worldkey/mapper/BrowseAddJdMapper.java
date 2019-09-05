package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.BrowseAddJd;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrowseAddJdMapper extends BaseMapper<BrowseAddJd> {

    @Select("SELECT * FROM browse_addjd WHERE users=#{userId}")
    List<BrowseAddJd> findByUid(@Param("userId") Long userId);

    @Delete("DELETE FROM browse_addjd")
    int deleteBrowseRecord();

    /**
     * 同一用户，同一文章，不能重复浏览加金豆。
     */
    @Select("SELECT * FROM browse_addjd WHERE users=#{userId} AND information=#{informationId}")
    List<BrowseAddJd> findByUidAndInfoId(@Param("userId") Long userId, @Param("informationId") Long informationId);
}
