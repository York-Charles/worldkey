package com.worldkey.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.DissRecord;

@Repository
public interface DissRecordMapper {
	
	@Insert("insert into diss_record values (null,#{userId},#{dissId})")
	Integer dissSomeone(DissRecord dr);

	@Select("select count(user_id) from diss_record where diss_id=#{dissId}")
	Integer getDissCount(Integer dissId);
	
	@Delete("delete from diss_record where diss_id=#{dissId}")
	Integer truncRecord(Integer dissId);
	
	@Select("select count(user_id) from diss_record where diss_id=#{dissId} and user_id=#{userId}")
	Integer getExist(@Param("dissId")Integer dissId,@Param("userId")Long userId);
	
	@Select("select diss_id from diss_record where user_id=#{userId}")
	Integer getDissIdByUserId(Long userId);
	
	@Update("update diss_record set diss_id=#{dissId} where user_id=#{userId}")
	Integer changeDissId(DissRecord dr);
}
