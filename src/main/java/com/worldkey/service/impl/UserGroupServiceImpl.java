package com.worldkey.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.worldkey.entity.UserGroup;
import com.worldkey.mapper.ThreeTypeMapper;
import com.worldkey.mapper.UserGroupMapper;
import com.worldkey.service.UserGroupService;
/**
 * 
 * @author 蔡亚坤
 *
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {

	@Resource
	UserGroupMapper userGroupMapper;
	@Resource
	ThreeTypeMapper threeTypeMapper;
	
	/**
	 * 添加小组成员
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Integer addMember(UserGroup userGroup) {
		this.userGroupMapper.insertUG(userGroup);
		return this.threeTypeMapper.updateAmount(userGroup.getGroupId());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Integer delMember(UserGroup userGroup) {
		this.userGroupMapper.delMember(userGroup);
		return this.threeTypeMapper.updateAmountQuit(userGroup.getGroupId());
	}

}
