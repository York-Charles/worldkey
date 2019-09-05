package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

@Data
@TableName("user_group")
public class UserGroup {
	
	private Long userId;
	private Integer groupId;
	
	private Users users;
	private ThreeType group;
	
	public UserGroup(Long userId, Integer groupId) {
		super();
		this.userId = userId;
		this.groupId = groupId;
	}

	public UserGroup() {
		super();
	}
	
}
