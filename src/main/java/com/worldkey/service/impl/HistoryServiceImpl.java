package com.worldkey.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.History;
import com.worldkey.mapper.HistoryMapper;
import com.worldkey.service.HistoryService;
import com.worldkey.util.ResultUtil;
@Service
public class HistoryServiceImpl implements HistoryService{
	@Resource
	private HistoryMapper hMapper;

	@Override
	public List<History> selectHistory(Long id) {
		 List<History> list = this.hMapper.selectHistory(id);
		return  list;
	}

}
