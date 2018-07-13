package com.worldkey.mapper;

import com.worldkey.entity.Image;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);
    
    int updateByUrl(Image record);

	List<Image> selectBySelective();

	int deleteUnused();
}