package com.worldkey.mapper;

import com.worldkey.entity.OneType;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OneTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OneType record);

    int insertSelective(OneType record);

    OneType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OneType record);

    int updateByPrimaryKey(OneType record);
    @Select("select id,type_name as typeName from one_type")
	List<OneType> selectAll();
    @Select("select id,type_name as typeName " +
            "FROM one_type AS one " +
            "WHERE ( SELECT COUNT(*) " +
                    "FROM two_type " +
                    "WHERE one_type=one.id )!=0")
    List<OneType> selectTwoNotNull();
    @Select("SELECT o.type_name,o.id,t.id AS tId,t.type_name AS tTypeName " +
            "FROM one_type AS o LEFT JOIN two_type AS t ON o.id=t.one_type")
    @ResultMap("com.worldkey.mapper.OneTypeMapper.selectAllOneTypeWithTwoTypeMap")
    List<OneType> selectAllOneTypeWithTwoType();

    @Select("select one_type.id,one_type.type_name from one_type inner join two_type on one_type.id=two_type.one_type where two_type.id=#{id}")
    OneType selectByTwoType(Integer id);
}