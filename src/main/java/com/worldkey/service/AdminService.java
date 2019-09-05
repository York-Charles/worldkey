package com.worldkey.service;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Admin;
import com.worldkey.entity.AdminRole;
import com.worldkey.entity.Role;

import java.util.List;

public interface AdminService {
    Admin findActionByName(String name);
    Admin login(Admin admin);
    int addRole(AdminRole record, String name);
    int delRole(AdminRole record, String name);
    int add(Admin record);
    int del(Integer id);
    int update(Admin record);
    List<Role> findRoleByAdmin(Integer id);
    PageInfo<Admin> findBySelective(Admin vo, Integer pageNum, Integer pageSize);
    Admin selectByPrimaryKey(Integer id);
}
