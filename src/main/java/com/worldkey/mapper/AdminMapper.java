package com.worldkey.mapper;

import com.worldkey.entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author HP
 */
@Repository
public interface AdminMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

	Admin selectByName(String name);

	List<Admin> selectBySelective(Admin vo);
	/**
	 * 通过用户名获取管理员权限
	 * @param username
	 * @return
	 */
	Admin findByUsername(String username);
	
	
	
	
}