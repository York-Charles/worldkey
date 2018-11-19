package com.worldkey.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.Praise;
import com.worldkey.entity.PraiseComment;

@Repository
public interface PraiseCommentMapper {
	
	@Select("select * from praise_comment where id=#{praiseId}")
	PraiseComment selectByPrimaryKey(Long praiseId);
	
	@Select ( "SELECT "
            + "id as praiseId "
            + "FROM "
            + "praise_comment "
            + "WHERE "
            + "(users = #{users} ANd comment = #{comment})")
    Long selectByUserAndInfo(@Param("users") Long users, @Param("comment") Integer comment);
	
	@Insert("insert into praise_comment (id,users,comment,status,create_time) VALUES (null,#{users.id,jdbcType=INTEGER},#{comment,jdbcType=INTEGER},#{status,jdbcType=INTEGER},NOW())")
	int insert(PraiseComment record);
	
	 @Update("UPDATE praise_comment_num SET " +
	            "praise_num=praise_num+1 " +
	            "WHERE comment=#{comment}")
	    int  addPraiseNum(@Param("comment")
	                              Integer comment);
	 
	 @Update("UPDATE praise_comment_num SET " +
	            "praise_num=praise_num-1 " +
	            "WHERE comment=#{comment}")
	    int  lessenPraiseNum(@Param("comment") Integer comment);

	    @Update("update praise_comment set status = #{status} where id=#{id} ")
	    int updateByPrimaryKey(@Param("status") Integer status ,@Param("id")Long id);
	    
	    
	    @Delete("delete from praise_comment where comment = #{b}")
	    int deleteC(Long b);
		

}
