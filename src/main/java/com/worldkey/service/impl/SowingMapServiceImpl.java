package com.worldkey.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.worldkey.entity.SowingMap;
import com.worldkey.mapper.SowingMapMapper;
import com.worldkey.service.SowingMapService;

@Service
public class SowingMapServiceImpl implements SowingMapService{

	@Resource
	SowingMapMapper sowingMapper;
	
	@Override
	public List<SowingMap> findSowingImg(Map<String,Object> map) {
		return this.sowingMapper.getSowingImg(map);
	}

	@Override
	public String findCoffee() {
		
		return this.sowingMapper.coffeeImg();
	}

}
