package com.worldkey.mapper;

import com.worldkey.entity.TwoType;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TwoTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TwoType record);

    int insertSelective(TwoType record);

    TwoType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TwoType record);

    int updateByPrimaryKey(TwoType record);

    @Select("select id,type_name as typeName,type_img as typeImg,one_type as oneType from two_type where one_type=#{id}")
    List<TwoType> selectByOne(Integer id);

    @Select("select id,type_name as typeName,type_img as typeImg,one_type as oneType from two_type")
    List<TwoType> selectAll();

    /**
     * @param id
     * @param replace
     * @return
     */
    int replaceId(@Param("id") Integer id, @Param("replace") Integer replace);

    @Select("select  id,type_name as typeName,type_img as typeImg,one_type as oneType from two_type where type_name=#{typeName}")
    TwoType selectByTypeName(@Param("typeName") String typeName);
    
    @Select("select two_type.id,two_type.type_name from two_type inner join three_type on two_type.id=three_type.two_type where three_type.id=#{id}")
    TwoType selectByThreeType(Integer id);
}