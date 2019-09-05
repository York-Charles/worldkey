package com.worldkey.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import com.worldkey.service.SowingMapService;
import com.worldkey.util.ResultUtil;

@RestController
@ResponseBody
@RequestMapping("sowing")
public class SowingMapController {

	@Resource
	SowingMapService sowingMap;
	
	@RequestMapping("getImg")
	public ResultUtil getSowingImg(Integer oneType,String twoType){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("oneType", oneType);
		if(twoType!=null){
			map.put("twoType", Integer.parseInt(twoType));
		}else{
			map.put("twoType", null);
		}
		return new ResultUtil(200,"ok",this.sowingMap.findSowingImg(map));
	}
	
	@RequestMapping("community_banner")
	public ResultUtil community(){
		return new ResultUtil(200,"ok",this.sowingMap.findCoffee());
	}
}
