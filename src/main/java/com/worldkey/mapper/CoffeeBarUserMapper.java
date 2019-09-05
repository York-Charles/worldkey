package com.worldkey.mapper;


import java.util.List;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.CoffeeBarUser;

@Repository
public interface CoffeeBarUserMapper {
	
	@Insert("insert into coffee_bar_user values (null,#{userId,jdbcType=INTEGER}"
			+ ",#{coffeeBarId,jdbcType=INTEGER},"
			+ "#{seat,jdbcType=INTEGER},#{label,jdbcType=INTEGER},0,0)")
	Integer matchingState(@Param("userId")Integer userId,@Param("coffeeBarId")Integer coffeeBarId
			,@Param("seat")Integer seat,@Param("label")String label);

	@Update("update coffee_bar_user set coffee_bar_id=#{coffeeBarId},seat=#{seat}," +
			"label=#{label} where user_id=#{userId}")
	Integer Changing(@Param("userId")Integer userId,@Param("coffeeBarId")Integer coffeeBarId
			,@Param("seat")Integer seat,@Param("label")String label);

	@Select("select user_id,coffee_bar_id from coffee_bar_user where ifelo=0")
	List<CoffeeBarUser> getAll();
	
	@Delete("delete from coffee_bar_user where user_id=#{userId}")
	Integer leaving(Integer userId);
	
	@Update("update coffee_bar_user set "
			+ "coffee_bar_id=#{coffeeBarId}, "
			+ "label=#{label},"
			+ "seat=#{seat} "
			+ "where user_id=#{userId}")
	Integer changingRoom(@Param("userId")Integer userId,@Param("coffeeBarId")Integer coffeeBarId
			,@Param("seat")Integer seat,@Param("label")String label);
	
	@Select("select user_id,seat,label,sex from coffee_bar_user,users where "
			+ "coffee_bar_id=#{Barid} and user_id=users.id and ifelo=0")
	List<CoffeeBarUser> selectByBarId(Integer barId);
	
	@Select("select user_id  from coffee_bar_user"
			+ " where seat=#{seat} and coffee_bar_id=#{barId} and ifelo=0")
	Long selectUserIdBySeat(@Param("seat")Integer seat,@Param("barId")Integer barId);
	
	@Select("select label  from coffee_bar_user"
			+ " where user_id=#{userId} and ifelo=0")
	String selectLabelByUserId(String userId);

	@Select("select label  from coffee_bar_user where user_id=#{userId}")
	String selectLabelByUserId1(Integer userId);
	
	@Select("select user_id,coffee_bar_id  as bar_id,seat,label,sex from coffee_bar_user,users where "
			+ "user_id=#{userId} and user_id=users.id")
	CoffeeBarUser selectByUserId(Integer userId);
	
	@Select("select user_id,coffee_bar_id  as bar_id,seat,label,sex from coffee_bar_user,users where "
			+ "user_id=#{userId} and user_id=users.id and ifelo=1")
	CoffeeBarUser selectByUserId2(Integer userId);
	
	@Select("select user_id,seat,label from coffee_bar_user where "
			+ "user_id=#{userId} and ifelo=0")
	CoffeeBarUser selectByUserId1(Long userId);
	
	@Select("select seat from coffee_bar_user where coffee_bar_id=#{cid} and ifelo=0")
	List<Integer> selectSeated(Integer cid);
	
	@Select("select label from coffee_bar_user where coffee_bar_id=#{cid} and ifelo=0")
	List<String> selectedUsed(Integer cid);
	
	@Select("select id from coffee_bar_user where user_id=#{userId} and ifelo=0")
	Integer selectExist(Integer userId);
	
	@Select("select coffee_bar_id from coffee_bar_user where user_id=#{userId} and ifelo=0")
	Integer selectBarIdByUserId(Integer userId);

	@Select("select coffee_bar_id from coffee_bar_user where user_id=#{userId}")
	Integer selectBarIdByUserId1(Integer userId);
	
	@Select("select seat from coffee_bar_user where user_id=#{userId} and ifelo=0")
	Integer selectSeatByUserId(Long userId);
	
	@Select("select seat from coffee_bar_user where user_id=#{userId}")
	Integer selectSeatByUserId1(Long userId);
	
	@Select("select icon from icon inner join coffee_bar_user as c on c.label=icon.name where "
			+ "c.user_id=#{userId}")
	String selectIcon1(Long userId);
	
	@Select("select yellow from icon inner join coffee_bar_user as c on c.label=icon.name where "
			+ "c.user_id=#{userId}")
	String selectIcon4(Long userId);
	
	@Select("select icon1 from icon inner join coffee_bar_user as c on c.label=icon.name where "
			+ "c.user_id=#{userId}")
	String selectIcon2(Integer userId);

	@Select("select icon2 from icon inner join coffee_bar_user as c on c.label=icon.name where "
			+ "c.user_id=#{userId}")
	String selectIcon3(Integer userId);
	
	@Select("select count(user_id) from coffee_bar_user where coffee_bar_id=#{cid} and ifelo=0")
	Integer selectUsers(Integer cid);
	
	@Delete("delete from coffee_bar where id=#{id}")
	Integer removeBar(Integer id);
	
	@Update("update coffee_bar_user set ifelo=1 where user_id in (#{toUserId},#{fromUserId})")
	Integer elopeSucceed(@Param("toUserId")Integer toUserId,@Param("fromUserId")Integer fromUserId);
	
	@Update("update coffee_ber_user set ifelo=0 where user_id=#{id}")
	Integer elopeFailed(Integer id);
	
	@Select("select head_icon from icon,coffee_bar_user where label=name and seat=#{seatId} and coffee_bar_id=#{barId}")
	String getHeadIcon(@Param("seatId")Integer seatId,@Param("barId")Integer barId);
	
	@Update("update coffee_bar_user set info_chk=1 where user_id=#{userId}")
	Integer infoCheck(Integer userId);
	
	@Select("select info_chk from coffee_bar_user where user_id=#{userId}")
	CoffeeBarUser selectChecked(Integer userId);

		@Select("select login_name from users inner join coffee_bar_user as c on c.user_id=users.id where c.coffee_bar_id=#{barId}")
	List<String> selectUserIdByBarId(Integer barId);
}
