package com.worldkey.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.ThreeType;
import com.worldkey.entity.Users;

/**
 * 三级标签分类接口
 */
public interface ThreeTypeService extends IService<ThreeType> {
	Integer del(Integer id);

	Integer update(ThreeType three);

	/*
	 * 通过二级id查找到三级所有实体
	 */
	List<ThreeType> findByTwo(Integer id);

	ThreeType findById(Integer id);

	List<ThreeType> findAll();

	Integer add(ThreeType three);

	/**
	 * 交换三级分类的ID
	 * 
	 * @param id
	 * @param replace
	 * @return boolean
	 */
	Integer replace(Integer id, Integer replace);

	ThreeType findByTypeName(String typeName);
	
	/*
	 * 通过一级id获取全部三级分类标签
	 */
	List<ThreeType> findAllByOne(Integer id);
	
	ThreeType findByType(Integer type);
	
	Integer checked(Integer checked,Integer id);
	
	//5.11
	
	Integer addGroup(ThreeType group,Long id);
	
	PageInfo<ThreeType> getGroup(Integer pageNum,Integer pageSize,Users user);
	
	PageInfo<ThreeType> findGroupByUser(Long id,Integer pageNum,Integer pageSize);
	
	List<ThreeType> findRandGroup(Users user);
	
	ThreeType findGroupById(Integer id);
	
	List<ThreeType> selectHotGroup(Users user);
	
	String findGroupByTypeName(String typeName);

	Long selectLeaderId(Integer groupId);
	
	Integer updateHeadImg(ThreeType group);
	
	Integer updateBgImg(ThreeType group);
	
	Integer updateContent(ThreeType group);
	
	PageInfo<ThreeType> findOwnGroup(Integer userId,Integer pageNum,Integer pageSize);
	
	PageInfo<ThreeType> findJoinedGroup(Integer userId,Integer pageNum,Integer pageSize);
}
