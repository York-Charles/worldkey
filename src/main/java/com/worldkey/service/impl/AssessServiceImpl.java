package com.worldkey.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Assess;
import com.worldkey.entity.CommentApp;
import com.worldkey.mapper.AssessMapper;
import com.worldkey.service.AssessService;
import com.worldkey.worldfilter.WordFilter;

@Service
public class AssessServiceImpl implements AssessService{
	@Resource
    private AssessMapper mapper;

	@Override
	public Integer insert(Assess assess){
		return this.mapper.add(assess);
	}

	@Override
	public PageInfo<Assess> findAssess(Integer page,Integer pageSize,Integer userId) {
		PageHelper.startPage(page, pageSize, true);
		List<Assess> assess = this.mapper.findAssess(userId);
		return new PageInfo<>(assess);
	}

	@Override
	public List<Assess> findAssesss(Integer userId) {
		return this.mapper.findAssesss(userId);
	}

}
