package com.worldkey.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.worldkey.entity.UserRelation;
import com.worldkey.mapper.UserRelationMapper;
import com.worldkey.service.UserRelationService;

@Service
public class UserRelationServiceImpl implements UserRelationService {

	@Resource
	private UserRelationMapper userRelationMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Integer insertUR(UserRelation userRelation) {
		// TODO Auto-generated method stub
		return this.userRelationMapper.insertUR(userRelation);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Integer deleteUR(UserRelation userRelation) {
		// TODO Auto-generated method stub
		return this.userRelationMapper.deleteUR(userRelation);
	}

	@Override
	public UserRelation selectUR(UserRelation userRelation) {
		// TODO Auto-generated method stub
		return this.userRelationMapper.selectUR(userRelation);
	}

	@Override
	public Integer actionState(Integer follower, Integer leader) {
		// TODO Auto-generated method stub
		return this.userRelationMapper.actionState(follower, leader);
	}

}
