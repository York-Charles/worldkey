package com.worldkey.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Record {
	private Long id;
	private String toUserId;
	private String userId;
	private String message;
	private Date createTime;
	
	private String petName;
	private String loginName;
	private String headImg;

}
