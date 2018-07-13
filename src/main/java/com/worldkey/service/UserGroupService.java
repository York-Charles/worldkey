package com.worldkey.service;

import com.worldkey.entity.UserGroup;

public interface UserGroupService {
	
	/**
	 * 小组添加成员
	 * @return
	 */
	Integer addMember(UserGroup userGroup);

	Integer delMember(UserGroup userGroup);
}
