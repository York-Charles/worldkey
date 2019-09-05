package com.worldkey.mapper;

import com.worldkey.entity.Action;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Action record);

    int insertSelective(Action record);

    Action selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Action record);

    int updateByPrimaryKey(Action record);

	List<Action> selectByAdmin(Integer adminId);

	List<Action> selectAll();
	
	List<Action> selectByRoleId(Integer id);
}