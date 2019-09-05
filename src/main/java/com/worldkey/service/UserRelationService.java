package com.worldkey.service;

import com.worldkey.entity.UserRelation;

public interface UserRelationService {
	
	Integer insertUR(UserRelation userRelation);

	Integer deleteUR(UserRelation userRelation);
	
	UserRelation selectUR(UserRelation userRelation);
	
	Integer actionState(Integer follower,Integer leader);
}
