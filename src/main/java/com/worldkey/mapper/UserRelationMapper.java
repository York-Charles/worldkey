package com.worldkey.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.UserRelation;

/**
 * 6.7 个人信息间的粉丝与关注
 * @author 北京都百01
 *
 */
@Repository
public interface UserRelationMapper {
	
	@Insert("insert into user_relation values  (null,now(),#{follower},#{leader})")
	Integer insertUR(UserRelation userRelation);
	
	@Delete("delete from user_relation where follower=#{follower} and leader=#{leader}")
	Integer deleteUR(UserRelation userRelation);
	
	@Select("select id from user_relation where follower=#{follower} and leader=#{leader}")
	UserRelation selectUR(UserRelation userRelation);
	
	@Select("select T.fans from("+
						"select count(follower) as fans,leader from user_relation group by leader) as T "+
						"where T.leader=#{leader}")
	Integer selectFans(Integer leader);

	@Select("select T.stars from("+
			"select count(leader) as stars,follower from user_relation group by follower) as T "+
			"where T.follower=#{follower}")
	Integer selectStars(Integer follower);
	
	@Select("select count(id) from user_relation where follower=#{follower} and leader=#{leader}")
	Integer actionState(@Param("follower")Integer follower,@Param("leader")Integer leader);
}
