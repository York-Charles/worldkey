package com.worldkey.mapper;

import com.worldkey.entity.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    Set<Role> selectAll();
    Set<Role> selectJustRole();
    @Select("SELECT " +
            "role.id, " +
            "role.`name`, " +
            "role.`value` " +
            "FROM " +
            "role LEFT JOIN " +
            "admin_role ON role.id= admin_role.role " +
            "WHERE " +
            "admin_role.admin = #{id}")
    Set<Role> findByAdminId(Integer id);
}