package com.worldkey.mapper;

import com.worldkey.entity.SystemConfig;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigMapper {
    int insert(SystemConfig record);

    int insertSelective(SystemConfig record);
    @Select("select "
    		+ "fileSrc as filesrc,"
    		+ "imgPath,emailfrom,"
    		+ "emailhost,"
    		+ "emailpassword,"
    		+ "text,"
    		+ "subject,"
    		+"default_headimg as defaultHeadimg"
    		+ " from "
    		+ "system_config")
    @ResultType(value=SystemConfig.class)
    SystemConfig find();
    
	int update(SystemConfig sc);
}