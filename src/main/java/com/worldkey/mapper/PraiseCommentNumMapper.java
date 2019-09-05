package com.worldkey.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.worldkey.entity.Praise;
import com.worldkey.entity.PraiseCommentNum;
import com.worldkey.entity.PraiseNum;

public interface PraiseCommentNumMapper {
	
    @Select("select praise_num_id as praiseNumId from praise_comment_num as praiseNum where comment=#{commentId} ")
    Long selectPKByinfo(@Param("commentId") Integer commentId);
    
    @Select("select * from praise_comment_num where praise_num_id=#{praiseNumId}")
    PraiseCommentNum selectByPrimaryKey(Long praiseNumId);
    
    @Insert("insert into praise_comment_num (praise_num_id,comment,praise_num) VALUES (null,#{comment,jdbcType=INTEGER},#{praiseNum,jdbcType=INTEGER})")
    int insert(PraiseCommentNum record);
    
    @Select("select praise_num from praise_comment_num where comment=#{commentId}")
    int selectPraiseNum(Integer commentId);

    @Delete("delete from praise_comment_num where comment=#{commentId}")
    int deleteComment(Integer commentId);
}
