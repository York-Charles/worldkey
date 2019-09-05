package com.worldkey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Assess;

@Repository
public interface AssessMapper {
	
	@Insert("INSERT INTO assess (id,user_id,assess_id,create_time,content) VALUES (null,#{userId,jdbcType=INTEGER},#{assessId,jdbcType=INTEGER},NOW(),#{conTent,jdbcType=VARCHAR})")
	int add(Assess assess);
	
	@Select(" SELECT assess.id,assess.user_id,assess.assess_id,assess.create_time,assess.content,users.pet_name,users.head_img FROM assess INNER JOIN users ON users.id=assess.assess_id WHERE assess.user_id=#{userId} order by assess.create_time desc")
	List<Assess> findAssess(Integer userId);
	
	@Select(" SELECT assess.id,assess.user_id,assess.assess_id,assess.create_time,assess.content,users.pet_name,users.head_img FROM assess INNER JOIN users ON users.id=assess.assess_id WHERE assess.user_id=#{userId} order by assess.create_time desc limit 5")
	List<Assess> findAssesss(Integer userId);
	
	@Update("update assess set content=#{conTent},create_time=NOW() where assess_id=#{assessId} and user_id=#{userId}")
	void update(@Param("conTent")String conTent,@Param("assessId")Integer assessId,@Param("userId")Integer userId);
	
	@Select("select * from assess")
	List<Assess> findAll();
	
	@Select("select * from assess where user_id=#{userId}")
	List<Assess> findByAll(Integer userId);

}
