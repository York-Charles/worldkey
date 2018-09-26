package com.worldkey.mapper;

import java.util.List;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.CountAndSex;

@Repository
public interface CoffeeBarMapper {
	
	@Select("select id from coffee_bar")
	List<Integer> getCid();
	
	@Insert("Insert into coffee_bar values (null,now(),ceiling(rand()*(select count(id) from coffee_scene)))")
	Integer addCoffeeHome();
	
	@Select("select Max(id) from coffee_bar")
	Integer newCoffeeBar();
	
	@Select("select count(user_id) as count,sex as sex from coffee_bar_user as c "
			 +"inner join coffee_bar as cb on c.coffee_bar_id=cb.id "
			 +"inner join users on users.id=c.user_id where cb.id=#{cbId} group by sex")
	List<CountAndSex> getBarUsers(Integer cbId);
	
	@Select("select scene from coffee_bar where id=#{cbId}")
	Integer selectScene(Integer cbId);
	
	@Delete("delete from coffee_bar where id=#{barId}")
	Integer removeBar(Integer barId);
}
