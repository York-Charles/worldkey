package com.worldkey.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Assess;
import com.worldkey.entity.CommentApp;


public interface AssessService {
	Integer insert(Assess assess);
	
	PageInfo<Assess> findAssess(Integer page,Integer pageSize,Integer userId);
    
    List<Assess> findAssesss(Integer userId);

}
