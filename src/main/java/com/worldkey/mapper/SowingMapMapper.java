package com.worldkey.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.SowingMap;

@Repository
public interface SowingMapMapper {
	
	public List<SowingMap> getSowingImg(Map<String,Object> map);

}
