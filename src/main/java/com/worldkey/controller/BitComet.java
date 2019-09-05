package com.worldkey.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("BitComet")
public class BitComet {
	@RequestMapping("download")
	public Map<String,String> download(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("versionCode","243");
		map.put("versionName","2.6.1");
		map.put("versionInfo","增加即时通讯\n修复了一些bug");
		map.put("apkUrl","http://47.93.15.192:8080/image/worldKey.apk");
		map.put("apkName","worldKey.apk");
		return map;
	}

}
