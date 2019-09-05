package com.worldkey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Admin;
import com.worldkey.entity.AdminRole;
import com.worldkey.entity.Role;
import com.worldkey.mapper.AdminMapper;
import com.worldkey.mapper.AdminRoleMapper;
import com.worldkey.service.AdminService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
	@Resource
	private AdminMapper adminMapper;
	@Resource
	private AdminRoleMapper adminRoleMapper;

	/**
	 * 获取用户权限信息
	 */
	@Override
	@Cacheable(key = "'findActionByName'+#name", value = "findActionByName")
	public Admin findActionByName(String name) {
		return this.adminMapper.findByUsername(name);
	}

	/**
	 * 管理员登陆
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
	public Admin login(Admin admin) {
		String name = admin.getName();
		if (name == null) {
			return null;
		}
		String password = admin.getPassword();
		Admin co = adminMapper.selectByName(name);
		if (co == null) {
			return null;
		}
		if (co.getPassword().equals(DigestUtils.md5Hex(password))) {
			return co;
		}
		return null;
	}

	/**
	 * 为管理员添加角色
	 * 
	 * @return 成功返回1
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@CacheEvict(value = "findActionByName", key = "'findActionByName'+#name")
	public int addRole(AdminRole record, String name) {
		return adminRoleMapper.insert(record);
	}

	/**
	 * 删除管理员角色
	 *
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@CacheEvict(value = "findActionByName", key = "'findActionByName'+#name")
	public int delRole(AdminRole record, String name) {
		return adminRoleMapper.delete(record);
	}

	/**
	 * 添加管理员
	 * 
	 * @return 成功返回1
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int add(Admin record) {
		record.setPassword(DigestUtils.md5Hex(record.getPassword()));
		return adminMapper.insertSelective(record);
	}

	/**
	 * 删除管理员
	 *
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int del(Integer id) {
		return adminMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 更新管理员信息updateByPrimaryKeySelective
	 *
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int update(Admin record) {
		if (record.getPassword() != null) {
			record.setPassword(DigestUtils.md5Hex(record.getPassword()));
		}
		return adminMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 获取管理员拥有的角色
	 */
	@Override
	public List<Role> findRoleByAdmin(Integer id) {
		return this.adminRoleMapper.seleceByAdminId(id);
	}

	/**
	 * 分页获取管理员信息
	 */
	@Override
	public PageInfo<Admin> findBySelective(Admin vo, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<Admin> list = this.adminMapper.selectBySelective(vo);
		return new PageInfo<>(list);
	}

	@Override
	public Admin selectByPrimaryKey(Integer id) {
		return this.adminMapper.selectByPrimaryKey(id);
	}

}
