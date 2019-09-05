package com.worldkey.mapper;
import com.worldkey.entity.Praise;
import com.worldkey.entity.PraiseNum;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author w
 */
@Repository

public interface PraiseNumMapper {
    int deleteByPrimaryKey(Long praiseNumId);
    int insert(PraiseNum record);
    int insertSelective(PraiseNum record);
    PraiseNum selectByPrimaryKey(Long praiseNumId);
    int updateByPrimaryKeySelective(PraiseNum record);
    int updateByPrimaryKey(PraiseNum record);

    /**
     * 根据文章ID获取赞数表主键ID
     * @param informationId
     * @return
     */
    @Select("select praise_num_id as praiseNumId from praise_num as praiseNum where information=#{informationId} ")
    Long selectPKByinfo(@Param("informationId") Integer informationId);
    
    
    @Select("select praise_num from praise_num where information = #{information}")
    Integer p(Integer information);
    
    @Delete("delete from praise_num where information=#{id}")
    int deleteInformaiton(Integer id);

}