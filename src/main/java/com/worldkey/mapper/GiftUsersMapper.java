package com.worldkey.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.GiftShow;
import com.worldkey.entity.GiftUsers;

@Repository
public interface GiftUsersMapper {

	@Insert("insert into gift_users (id,time,users,to_users,gift_id) values (null,#{createTime,jdbcType=VARCHAR},#{users,jdbcType=BIGINT},#{toUsers,jdbcType=BIGINT},#{giftId,jdbcType=INTEGER})")
	boolean insert(GiftUsers giftUsers);

	//11.16 排行榜摘除咖啡厅赠送礼物
	@Insert("insert into gift_users (id,time,users,to_users,gift_id,is_anony) values (null,#{createTime,jdbcType=VARCHAR},#{users,jdbcType=BIGINT},#{toUsers,jdbcType=BIGINT},#{giftId,jdbcType=INTEGER},1)")
	boolean insertBarGift(GiftUsers giftUsers);
	
	@Select("select distinct users from gift_users where to_users=#{touserId} and is_anony=0")
	List<Long> getPresentorsByUsers(Long touserId);
	
	@Select("select gift_id from gift_users where users=#{userId} and is_anony=0")
	List<Integer> giftByUserPressentors(Long userId);
	
	@Select("select gift_users.gift_id as giftId,g.name As giftName,g.gift_img As giftImg,count(gift_users.gift_id) as giftNum,g.jd_price as jd,g.zs_price as zs from gift_users " +
			"inner join gift as g on g.gift_id=gift_users.gift_id " +
			"inner join users on users.id=gift_users.to_users " +
			"where users.id=#{userId} group by gift_users.gift_id")
	List<GiftShow> findNumByUser(Long userId);
	
	@Select("select count(gift_id) as giftNum from gift_users where to_users=#{userId}")
	Integer getUserGiftNum(@Param("userId") Long userId);
}