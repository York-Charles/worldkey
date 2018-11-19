package com.worldkey.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.History;

@Repository
public interface HistoryMapper {
	@Insert("insert into history (id,create_time,group_name,user_id,user_name,group_img,pet_name,classify,group_id,comment_info,to_comment_id,a_comment_info,to_userid,head_img,web_url,title_img,information,status,praise_num,to_pet_name,comment_create_time) "
			+ " values (null,now(),#{groupName, jdbcType=VARCHAR},#{userId,jdbcType=BIGINT},#{userName,jdbcType=VARCHAR},#{groupImg,jdbcType=VARCHAR},#{petName,jdbcType=VARCHAR},#{classify,jdbcType=INTEGER},#{groupId,jdbcType=INTEGER},#{commentInfo,jdbcType=VARCHAR},#{toCommentId,jdbcType=INTEGER},#{aCommentInfo,jdbcType=VARCHAR},#{toUserId,jdbcType=BIGINT},#{headImg,jdbcType=VARCHAR},#{webUrl,jdbcType=VARCHAR},#{titleImg,jdbcType=VARCHAR},#{information,jdbcType=BIGINT},#{status,jdbcType=INTEGER},#{praiseNum,jdbcType=INTEGER},#{toPetName,jdbcType=VARCHAR},#{commentCreateTime,jdbcType=VARCHAR}) ")
	Integer aaa(History h);
	
	@Select("select * from history where user_id=#{id} order by create_time desc ")
	List<History> selectHistory(Long id);
	
	@Delete("delete from history where information=#{id} and (classify=7 or classify=8)")
	int deleteInformation(Long id);

}
