package com.worldkey.service;

import com.worldkey.entity.TwoType;

import java.util.List;

public interface TwoTypeService {
    Integer del(Integer id);
    Integer update(TwoType two);
    List<TwoType> findByOne(Integer id);
    TwoType findById(Integer id);
    List<TwoType> findAll();
    Integer add(TwoType two);

    /**
     * 交换二级分类的ID
     * @param id
     * @param replace
     * @return boolean
     */
    Integer replace(Integer id, Integer replace);

    TwoType findByTypeName(String typeName);
    
    TwoType findByThree(Integer threeType);
}
