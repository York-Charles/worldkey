package com.worldkey.mapper;

import com.worldkey.entity.SensitiveWord;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensitiveWordMapper {
    @Select("select id, word, is_Word as isWord from sensitive_word where is_Word = 1")
    List<SensitiveWord> findAllWord();
    @Select("select id, word, is_Word as isWord from sensitive_word where is_Word = 1 AND word LIKE CONCAT('%',#{search},'%')ORDER BY id DESC")
    List<SensitiveWord> search(String search);

    @Select("select id, word, is_Word as isWord from sensitive_word where is_Word = 0")
    List<SensitiveWord> findAllStopWord();

    int deleteByPrimaryKey(Integer id);

    int insert(SensitiveWord record);

    int insertSelective(SensitiveWord record);

    SensitiveWord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SensitiveWord record);

    int updateByPrimaryKey(SensitiveWord record);
}