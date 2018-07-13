package com.worldkey.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.ZsDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZsDetailMapper extends BaseMapper<ZsDetail> {
    @Select("SELECT * FROM zs_detail WHERE users=#{users} ORDER BY zs_id DESC")
    List<ZsDetail> fingByUid(@Param("users") Long users);
    
    //<---以下方法---薛秉承--2018.4.11写入-->
    @Select("SELECT * FROM zs_detail WHERE users=#{users} and zs_num>0 ORDER BY zs_id DESC")
    List<ZsDetail> fingByZsjust(@Param("users") Long users);
}
