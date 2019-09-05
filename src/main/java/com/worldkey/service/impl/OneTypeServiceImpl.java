package com.worldkey.service.impl;

import com.worldkey.entity.OneType;
import com.worldkey.entity.TwoType;
import com.worldkey.mapper.OneTypeMapper;
import com.worldkey.mapper.TwoTypeMapper;
import com.worldkey.service.OneTypeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OneTypeServiceImpl implements OneTypeService {
	@Resource
	private OneTypeMapper typeMapper;
	@Resource
	private TwoTypeMapper twoMapper;

	/**
	 * 获取全部一级分类列表
	 */
	@Override
    @Cacheable(value="OneTypeService",key="'OneTypeAll'")
	public List<OneType> findAll() {
		return this.typeMapper.selectAll();
	}
	/**
	 * 删除一级分类，若该分类下存在二级分类返回-1，不删除一级分类
	 * 若该分类下无二级分类，删除该分类，并返回1
	 * @param id 一级分类的Id
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@CacheEvict(value="OneTypeService",key="'OneTypeAll'")
	public Integer del(Integer id) {
		List<TwoType>list=this.twoMapper.selectByOne(id);
		if (list.size()!=0) {
			return -1;
		}
		return this.typeMapper.deleteByPrimaryKey(id);
	}
	/**
	 * 更新一级分类，同时更新缓存
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@CacheEvict(value="OneTypeService",key="'OneTypeAll'")
	public OneType update(OneType one) {
		this.typeMapper.updateByPrimaryKeySelective(one);
		System.out.println(this.typeMapper.selectByPrimaryKey(one.getId()).getTypeName());
		return this.typeMapper.selectByPrimaryKey(one.getId());
	}
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@CacheEvict(value="OneTypeService",key="'OneTypeAll'")
	public Integer add(OneType one) {
		return this.typeMapper.insert(one);
	}

	@Override
	public List<OneType> findNotNull() {
		return this.typeMapper.selectTwoNotNull();
	}

	@Override
	public List<OneType> selectAllOneTypeWithTwoType() {
		return this.typeMapper.selectAllOneTypeWithTwoType();
	}

	@Override
	public OneType findById(Integer type) {
		return this.typeMapper.selectByPrimaryKey(type);
	}
	@Override
	public OneType selectByTwoType(Integer id) {
		// TODO Auto-generated method stub
		return this.typeMapper.selectByTwoType(id);
	}

}
