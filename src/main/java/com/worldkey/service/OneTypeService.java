package com.worldkey.service;

import com.worldkey.entity.OneType;

import java.util.List;

public interface OneTypeService {
    public List<OneType> findAll();
    public Integer del(Integer id);
    public OneType update(OneType one);
    public Integer add(OneType one);
    List<OneType> findNotNull();

    /**
     * 左连接查询所有类型，包括一级和二级
     */
    List<OneType> selectAllOneTypeWithTwoType();
    OneType findById(Integer type);
    
    OneType selectByTwoType(Integer id);
}
