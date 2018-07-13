package com.worldkey.entity;

import lombok.*;

@Data
public class Album {
	private Integer id;
	private String createDate;
	private Integer userId;
	private String url;
	
	public Album(Integer userId, String url) {
		super();
		this.userId = userId;
		this.url = url;
	}
	
}
