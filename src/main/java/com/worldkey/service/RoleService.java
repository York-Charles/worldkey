package com.worldkey.service;

import com.worldkey.entity.Role;
import com.worldkey.entity.RoleAction;

import java.util.Set;

public interface RoleService {
    int addAction(RoleAction record);
    int delAction(RoleAction record);
    Set<Role> findAll();
    Set<Role> findByAdminId(Integer id);

    Set<Role> findJustRole();
    int add(Role vo);
    int del(Integer id);
    int update(Role vo);
    Role findById(Integer id);
}
