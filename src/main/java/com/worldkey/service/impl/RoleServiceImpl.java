package com.worldkey.service.impl;

import com.worldkey.entity.Role;
import com.worldkey.entity.RoleAction;
import com.worldkey.mapper.RoleActionMapper;
import com.worldkey.mapper.RoleMapper;
import com.worldkey.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
	@Resource
	private RoleActionMapper roleActionMapper;
	@Resource
	private RoleMapper roleMapper;

	/**
	 * 为角色添加权限
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int addAction(RoleAction record){
		return this.roleActionMapper.insert(record);
	}
	/**
	 * 为角色删除权限
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int delAction(RoleAction record){
		return this.roleActionMapper.delete(record);
	}
	@Override
    public Set<Role> findAll() {
		return this.roleMapper.selectAll();
	}

	@Override
	public Set<Role> findByAdminId(Integer id) {
		return this.roleMapper.findByAdminId(id);
	}

	@Override
	public Set<Role> findJustRole() {
		return this.roleMapper.selectJustRole();
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int add(Role vo) {
		//插入role记录并返回主键
		return this.roleMapper.insert(vo);
	}
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int del(Integer id) {
		roleActionMapper.deleteRoleActionByRole(id);
		return this.roleMapper.deleteByPrimaryKey(id);
	}
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int update(Role vo) {
		return this.roleMapper.updateByPrimaryKeySelective(vo);
	}
	@Override
    public Role findById(Integer id) {
		return this.roleMapper.selectByPrimaryKey(id);
	}
	
}
