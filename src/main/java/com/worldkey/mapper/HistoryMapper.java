package com.worldkey.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.History;

@Repository
public interface HistoryMapper {
	@Insert("insert into history (id,create_time,group_name,user_id,user_name,group_img,pet_name,classify,group_id) values (null,now(),#{groupName, jdbcType=VARCHAR},#{userId,jdbcType=BIGINT},#{userName,jdbcType=VARCHAR},#{groupImg,jdbcType=VARCHAR},#{petName,jdbcType=VARCHAR},#{classify,jdbcType=INTEGER},#{groupId,jdbcType=INTEGER}) ")
	Integer aaa(History h);
	
	@Select("select * from history where user_id=#{id} order by create_time desc ")
	List<History> selectHistory(Long id);

}
