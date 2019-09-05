package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.Sign;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author wu
 */
@Repository
public interface SignMapper extends BaseMapper<Sign> {
    /**
     * 通过用户ID获取签到对象
     */
    @Select("select sign_id from sign where users =#{uId}")
    Integer selectByUid(@Param("uId") Long uId);

    /**
     * 清空签到表
     */
    @Delete("DELETE FROM sign  ")
    int deleteSign();
}
