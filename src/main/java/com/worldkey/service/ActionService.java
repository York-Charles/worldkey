package com.worldkey.service;

import com.worldkey.entity.Action;

import java.util.List;

public interface ActionService {
    List<Action> findByAdminId(Integer adminId);
    int add(Action record, Integer role);
    int del(Integer id);
    List<Action> findAll();
    List<Action> findByRoleId(Integer id);
    int updateById(Action vo);
    Action findById(Integer id);

}
