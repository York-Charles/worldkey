package com.worldkey.mapper;

import com.worldkey.entity.BrowsingHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.mahout.cf.taste.impl.model.BooleanPreference;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BrowsingHistoryMapper {

	@Insert("insert into browsing_history(user,item,pf,time) values(#{user},#{item},#{pf},#{time})")
	int insert(BrowsingHistory repord);

	@Select("select * from  browsing_history")
	List<BrowsingHistory> select();
	@Select("select item as itemID,user as userID  from browsing_history where user=#{user}")
	List<BooleanPreference> selectByUser(Long user);
	@Select("select user from browsing_history")
	Set<Long> selectUsers();
	@Select("SELECT * " +
			"FROM " +
			"browsing_history " +
			"WHERE " +
			"`user` = #{user} AND " +
			"item = #{item} ")
    BrowsingHistory selectByUserAndItem(BrowsingHistory browsingHistory);
}
