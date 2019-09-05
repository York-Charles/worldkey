package com.worldkey.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.History;
import com.worldkey.entity.ThreeType;
import com.worldkey.entity.TwoType;
import com.worldkey.entity.UserGroup;
import com.worldkey.entity.Users;
import com.worldkey.jdpush.Jdpush;
import com.worldkey.mapper.HistoryMapper;
import com.worldkey.mapper.ThreeTypeMapper;
import com.worldkey.mapper.UserGroupMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.ThreeTypeService;
import com.worldkey.service.UsersService;

/*
 * 三级标签实现类
 */
@Service
public class ThreeTypeServiceImpl extends ServiceImpl<ThreeTypeMapper, ThreeType> implements ThreeTypeService {
	@Resource
	private InformationAllService informationAllService;
	@Resource
	private ThreeTypeMapper threeTypeMapper;
	@Resource
	private UserGroupMapper userGroupMapper;
	@Resource
	private UsersMapper uMapper;
	@Resource
	private HistoryMapper hMapper;
	@Resource
	private UsersService usersService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer del(Integer id) {
		return this.threeTypeMapper.deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer update(ThreeType three) {
		return this.threeTypeMapper.updateById(three);
	}

	@Override
	public List<ThreeType> findByTwo(Integer id) {
		if(id==null){
			id=161;
		}
		return this.threeTypeMapper.selectByTwo(id);
	}

	@Override
	public List<TwoType> findByOne(Integer id) {
		return this.threeTypeMapper.selectByOne(id);
	}

	/**
	 * 通过Id获取
	 */
	@Override
	public ThreeType findById(Integer id) {
		return this.threeTypeMapper.selectById(id);
	}

	/**
	 * 获取全部三级分类
	 */
	@Override
	public List<ThreeType> findAll() {
		return this.threeTypeMapper.selectAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer add(ThreeType three) {
		return this.threeTypeMapper.insert(three);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer replace(Integer id, Integer replace) {
		int c = this.threeTypeMapper.replaceId(id, replace);
		informationAllService.replaceType(id, replace);
		return c;
	}

	@Override
	public ThreeType findByTypeName(String typeName) {
		return this.threeTypeMapper.selectByTypeName(typeName);
	}

	@Override
	public List<ThreeType> findAllByOne(Integer id) {
		return this.threeTypeMapper.selectAllByOne(id);
	}

	@Override
	public ThreeType findByType(Integer type) {
		// TODO Auto-generated method stub
		return this.threeTypeMapper.findByThreeType(type);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer checked(Integer checked, Integer id) {
		int c = this.threeTypeMapper.checked(checked, id);

		ThreeType t = this.threeTypeMapper.selectGroupById(id);
		Long a = this.threeTypeMapper.findUserId(id);
		Users u = this.uMapper.uuu(a);
		String groupName = t.getTypeName();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("groupImg", t.getHeadImg());
		map.put("groupId", id.toString());

		History h = new History();
		h.setCreateTime(new Date());
		h.setGroupImg(t.getHeadImg());
		h.setGroupName(t.getTypeName());
		h.setPetName(u.getPetName());
		h.setUserId(u.getId());
		h.setUserName(u.getLoginName());
		h.setClassify(4);
		h.setGroupId(id);
		this.hMapper.aaa(h);

		Jdpush.jpushAndriod4(u.getLoginName(), groupName, map);
		return c;
	}

	// 5.11

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer addGroup(ThreeType group, Long id) {
		// TODO Auto-generated method stub
		this.threeTypeMapper.addGroup(group);
		ThreeType groupc = this.threeTypeMapper.selectMAX();
		UserGroup userGroup = new UserGroup(id, groupc.getId());
		return this.userGroupMapper.insertUG(userGroup);
	}

	@Override
	public PageInfo<ThreeType> getGroup(Integer pageNum, Integer pageSize, Users user) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<ThreeType> groupAll = this.threeTypeMapper.findGroup();
		if (user != null) {
			List<Integer> joinedGroupId = this.userGroupMapper.SelectExistence(user.getId());
			for (int i = 0; i < groupAll.size(); i++) {
				if (joinedGroupId.contains(groupAll.get(i).getId())) {
					groupAll.get(i).setIsJoin(1);
				} else {
					groupAll.get(i).setIsJoin(0);
				}
				/*
				 * for(int j = 0;j<joinedGroupId.size();j++){
				 * log.info(joinedGroupId.get(j)+"");
				 * log.info(groupAll.get(i).getId()+"");
				 * if(joinedGroupId.get(j)==groupAll.get(i).getId()){
				 * temp.setIsJoin(1); break; } }
				 */
			}
		}
		return new PageInfo<ThreeType>(groupAll);
	}

	@Override
	public PageInfo<ThreeType> findGroupByUser(Long id, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<ThreeType> list = this.threeTypeMapper.findGroupByUser(id);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setIsJoin(1);
		}
		return new PageInfo<>(list);
	}

	@Override
	public List<ThreeType> findRandGroup(Users user) {
		// TODO Auto-generated method stub
		List<ThreeType> group = this.threeTypeMapper.findRandGroup();
		if (user != null) {
			List<Integer> joinedGroupId = this.userGroupMapper.SelectExistence(user.getId());
			for (int i = 0; i < group.size(); i++) {
				if (joinedGroupId.contains(group.get(i).getId())) {
					group.get(i).setIsJoin(1);
				} else {
					group.get(i).setIsJoin(0);
				}
			}
			return group;
		}
		return group;
	}

	@Override
	public ThreeType findGroupById(Integer id) {
		// TODO Auto-generated method stub
		return this.threeTypeMapper.selectGroupById(id);
	}

	@Override
	public String findGroupByTypeName(String typeName) {
		// TODO Auto-generated method stub
		return this.threeTypeMapper.selectGroupByTypeName(typeName);
	}

	@Override
	public List<ThreeType> selectHotGroup(Users user) {
		// TODO Auto-generated method stub
		List<ThreeType> groupAll = this.threeTypeMapper.findHotGroup();
		if (user != null) {
			List<Integer> joinedGroupId = this.userGroupMapper.SelectExistence(user.getId());
			for (int i = 0; i < groupAll.size(); i++) {
				if (joinedGroupId.contains(groupAll.get(i).getId())) {
					groupAll.get(i).setIsJoin(1);
				} else {
					groupAll.get(i).setIsJoin(0);
				}
			}
			return groupAll;
		}
		return groupAll;

	}

	@Override
	public Long selectLeaderId(Integer groupId) {
		return this.threeTypeMapper.findUserId(groupId);
	}

	@Override
	public Integer updateHeadImg(ThreeType group) {
		// TODO Auto-generated method stub
		return this.threeTypeMapper.updateHeadImg(group);
	}

	@Override
	public Integer updateBgImg(ThreeType group) {
		// TODO Auto-generated method stub
		return this.threeTypeMapper.updateBgImg(group);
	}

	@Override
	public Integer updateContent(ThreeType group) {
		// TODO Auto-generated method stub
		return this.threeTypeMapper.updateContent(group);
	}

	@Override
	public PageInfo<ThreeType> findOwnGroup(Integer userId, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize, true);
		List<ThreeType> findLeaderGroup = this.threeTypeMapper.findLeaderGroup(userId);
		return new PageInfo<ThreeType>(findLeaderGroup);
	}

	@Override
	public PageInfo<ThreeType> findJoinedGroup(Integer userId, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize, true);
		List<ThreeType> joinedGroup = this.threeTypeMapper.findJoinedGroup(userId);
		return new PageInfo<ThreeType>(joinedGroup);
	}

	@Override
	public PageInfo<ThreeType> getXiaozu(Integer pageNum, Integer pageSize, String name, String token) {
		Users user = usersService.findByToken(token);
		PageHelper.startPage(pageNum, pageSize, true);
		List<ThreeType> groupAll = this.threeTypeMapper.findXiaozu(name);
		if (user != null) {
			List<Integer> joinedGroupId = this.userGroupMapper.SelectExistence(user.getId());
			for (int i = 0; i < groupAll.size(); i++) {
				if (joinedGroupId.contains(groupAll.get(i).getId())) {
					groupAll.get(i).setIsJoin(1);
				} else {
					groupAll.get(i).setIsJoin(0);
				}
			}
		}

		return new PageInfo<ThreeType>(groupAll);
	}

}
