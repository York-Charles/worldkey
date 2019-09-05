package com.worldkey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.UserGroup;

/**
 * 用户与小组
 * @author 蔡亚坤
 *
 */
@Repository
public interface UserGroupMapper {
	
	/**
	 * 用户加入小组
	 * @param userGroup
	 * @return
	 */
	int insertUG(UserGroup userGroup);
	
	@Select("select group_id from user_group where user_id=#{id}")
	List<Integer> SelectExistence(Long id);
	
	
	@Select("select group_id,user_id from user_group where user_id=#{userId} and group_id=#{groupId}")
	List<UserGroup> SelectpanDuan(UserGroup userGroup);
	
	@Delete("delete from user_group where user_id=#{userId} and group_id=#{groupId}")
	Integer delMember(UserGroup userGroup);
	
	@Select("SELECT sum(user_id=#{userId}) FROM three_type")
	Integer threeNumber(Integer userId);
	
	@Select("SELECT sum(user_id=#{userId}) FROM user_group")
	Integer number(Integer userId);
	

}
