package com.worldkey.mapper;

import com.worldkey.entity.Friend;
import com.worldkey.entity.FriendExample;
import com.worldkey.entity.Record;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendMapper {
	int insert(Friend record);

	int insertSelective(Friend record);

	@Select("SELECT DISTINCT "+
    		"u.pet_name AS usersname,  "+
    		"f.note as note, "+
    		"u.head_img as headimg,  "+
    		"u.tel_num as telNum,  "+
    		"u.birthday as birthday,  "+
    		"u.email as email,  "+
    		"u.sex as sex,  "+
    		"f.friend as usersId  "+
    		"FROM "+  
    		"users AS u ,friend as f "+
    		"WHERE " +	
    		"u.login_name IN (SELECT e.friend FROM friend as e WHERE e.users=#{loginName})"
    		+ " AND u.login_name=f.friend")
	List<FriendExample> selectByUsersId(String loginName);
	@Select("SELECT " +
			"count(*)"+
			"FROM " +
			"friend AS f " +
			"WHERE " +
			"f.users = #{users} AND " +
			"f.friend = #{friend} ")
	int checkFriend(Friend f);
	@Delete("DELETE FROM friend where friend=#{friend} and users=#{users} ")
    int delByUserAndFriend(Friend friend);
	
	@Insert("insert into record (id,to_user_id,user_id,message,create_time,type) values (null,#{toUserId,jdbcType=VARCHAR},#{userId,jdbcType=VARCHAR},#{message,jdbcType=VARCHAR},NOW(),0)")
	int insertNotify(@Param("userId")String userId,@Param("toUserId")String toUserId, @Param("message")String message);
	
	
	@Select("select record.id,record.to_user_id,record.user_id,record.message,record.create_time,record.type,users.pet_name AS petName,users.login_name AS loginName,users.head_img AS headImg from record left join users on users.login_name = record.user_id where record.to_user_id=#{toUserId} order by record.create_time desc")
	List<Record> selectRecord(String toUserId);
	
	@Update("update record set type=1 where to_user_id=#{toUserId} and user_id=#{userId}")
	int updateType(@Param("userId")String userId,@Param("toUserId")String toUserId);
}