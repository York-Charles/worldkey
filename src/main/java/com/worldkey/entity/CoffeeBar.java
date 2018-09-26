package com.worldkey.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@Data
public class CoffeeBar {
	
	private Integer id;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private Long userId;
	
	public CoffeeBar(Long userId,Integer id) {
		this.userId = userId;
		this.id = id;
		this.createTime = new Date();
	}
	
	
}
