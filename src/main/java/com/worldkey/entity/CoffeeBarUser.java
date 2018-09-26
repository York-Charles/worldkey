package com.worldkey.entity;

import lombok.Data;

@Data
public class CoffeeBarUser {

	private Integer id;
	private Integer userId;
	private Integer barId;
	private Integer seat;
	private Integer sex;
	private String label;
	private Integer isOwn = 0;
	private Integer userState;
	private Integer infoChk;

}
