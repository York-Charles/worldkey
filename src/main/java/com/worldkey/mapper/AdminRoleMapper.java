package com.worldkey.mapper;

import com.worldkey.entity.AdminRole;
import com.worldkey.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRoleMapper {
    int insert(AdminRole record);

    int insertSelective(AdminRole record);

	int delete(AdminRole record);

	List<Role> seleceByAdminId(Integer id);
}