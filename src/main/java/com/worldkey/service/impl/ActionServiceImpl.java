package com.worldkey.service.impl;

import com.worldkey.entity.Action;
import com.worldkey.entity.RoleAction;
import com.worldkey.mapper.ActionMapper;
import com.worldkey.mapper.RoleActionMapper;
import com.worldkey.service.ActionService;
import com.worldkey.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActionServiceImpl implements ActionService{
	@Resource
	private ActionMapper actionMapper;
	@Resource
	private RoleActionMapper roleActionMapper;
	@Resource
	private RoleService roleService;
	
	/**
	 * 通过管理员id获取管理员权限列表
	 */
	@Override
    public List<Action>findByAdminId(Integer adminId){
		return this.actionMapper.selectByAdmin(adminId);
	}
	 /**
	  * 添加权限列表
	  * @return 成功返回 1
	  */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int add(Action record,Integer role){
		int a=this.actionMapper.insert(record);
		if (role!=0) {
			roleService.addAction(new RoleAction(role, record.getId()));
		}
		//roleService.addAction(new RoleAction(1, record.getId()));
		return a;
	}
	/**
	 * 通过ID删除权限
	 * @return 成功返回1，失败返回0，说明该权限可能在被角色使用
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int del(Integer id){
		List<RoleAction>list=roleActionMapper.selectByAction(id);
		if (list!=null&&list.size()!=0) {
			return 0;
		}
		return this.actionMapper.deleteByPrimaryKey(id);
	}
	@Override
    public List<Action> findAll() {
		return this.actionMapper.selectAll();
	}
	@Override
    public List<Action> findByRoleId(Integer id) {
		return actionMapper.selectByRoleId(id);
	}
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int updateById(Action vo) {
		return this.actionMapper.updateByPrimaryKey(vo);
	}
	@Override
    public Action findById(Integer id) {
		return this.actionMapper.selectByPrimaryKey(id);
	}
}
