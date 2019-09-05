package com.worldkey.service;

import java.util.List;
import java.util.Map;

import com.worldkey.entity.SowingMap;

public interface SowingMapService {
	
	public List<SowingMap> findSowingImg(Map<String,Object> map);
	
	public String findCoffee();
 
}
