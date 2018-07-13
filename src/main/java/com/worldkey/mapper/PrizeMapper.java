package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.Prize;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PrizeMapper extends BaseMapper<Prize> {
    @Select("SELECT " +
            "prize.prize_id AS prizeId," +
            "prize.`describe`, " +
            "prize.jd,  " +
            "prize.zs, " +
            "prize.msg, " +
            "prize.create_time AS createTime," +
            "prize.chance " +
            "FROM " +
            "prize")
    List<Prize> selectAll();
}
