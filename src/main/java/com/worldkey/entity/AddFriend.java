package com.worldkey.entity;

import java.util.Date;

import lombok.Data;

@Data
public class AddFriend {
	private Integer id;
	private Long userId;//被添加人的Id
	private Long toUserId;//添加人的Id
	private Date createTime;
	private Integer agree;
	
	private String petName;
	private String headImg;
	private String loginName;

}
