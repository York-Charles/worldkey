package com.worldkey.service.impl;

import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Praise;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.PraiseComment;
import com.worldkey.entity.PraiseCommentNum;
import com.worldkey.entity.PraiseNum;
import com.worldkey.entity.Users;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.mapper.PraiseCommentMapper;
import com.worldkey.mapper.PraiseCommentNumMapper;
import com.worldkey.mapper.PraiseMapper;
import com.worldkey.mapper.PraiseNumMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.PraiseService;
import com.worldkey.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author DB161 方法的实现
 */
@Service
@Slf4j
public class PraiseServiceImpl implements PraiseService {
	@Resource
	private PraiseMapper praiseMapper;
	@Resource
	private PraiseCommentMapper praiseCommentMapper;
	@Resource
	private PraiseCommentNumMapper praiseCommentNumMapper;
	@Resource
	private UsersService usersService;
	@Resource
	private PraiseNumMapper praiseNumMapper;
	@Resource
	private UsersMapper uMapper;
	@Resource
	private InformationAllMapper iMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int addPraise(String token, Integer informationId) throws Exception {
		// 获取用户对象
		Users user = usersService.findByToken(token);
		// 获取用户ID
		Long uId = user.getId();
		// 获取点赞表主键ID
		Long praiseId = this.praiseMapper.selectByUserAndInfo(uId, informationId);
		// 获取点赞表对象
		Praise praise = this.praiseMapper.selectByPrimaryKey(praiseId);
		// 创建点赞表对象
		Praise p0 = new Praise();
		// 创建点赞记录表对象
		PraiseNum pn = new PraiseNum();
		if (praise == null) {
			// 点赞表插入记录
			p0.setUsers(user);
			p0.setInformation(informationId);
			p0.setStatus(1);
			p0.setCreateTime(new Date());
			this.praiseMapper.insert(p0);
			// 判定记录表是否为空;获取记录表文章ID
			Long praiseNumId = this.praiseNumMapper.selectPKByinfo(informationId);
			// 获取记录表对象
			PraiseNum praiseNum = this.praiseNumMapper.selectByPrimaryKey(praiseNumId);
			if (praiseNum == null) {
				// 点赞记录表插入记录
				pn.setInformation(informationId);
				pn.setPraiseNum(1);
				this.praiseNumMapper.insert(pn);
				return 1;
			}
			this.praiseMapper.addPraiseNum(informationId);
			return 1;
		}
		if (praise.getStatus() == 0) {
			praise.setStatus(1);
			this.praiseMapper.addPraiseNum(informationId);
			this.praiseMapper.updateByPrimaryKey(praise);
			return 1;
		}

		praise.setStatus(0);
		this.praiseMapper.lessenPraiseNum(informationId);
		this.praiseMapper.updateByPrimaryKey(praise);
		return 0;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int addCommentPraise(String token, Integer commentId) throws Exception {
		// 获取用户对象
		Users user = usersService.findByToken(token);
		// 获取用户ID
		Long uId = user.getId();
		// 获取点赞表主键ID
		Long praiseId = this.praiseCommentMapper.selectByUserAndInfo(uId, commentId);
		// 获取点赞表对象
		PraiseComment praise = this.praiseCommentMapper.selectByPrimaryKey(praiseId);
		// 创建点赞表对象
		PraiseComment p0 = new PraiseComment();
		// 创建点赞记录表对象
		PraiseCommentNum pn = new PraiseCommentNum();
		if (praise == null) {
			// 点赞表插入记录
			p0.setUsers(user);
			p0.setComment(commentId);
			p0.setStatus(1);
			p0.setCreateTime(new Date());
			this.praiseCommentMapper.insert(p0);
			// 判定记录表是否为空;获取记录表评论ID
			Long praiseNumId = this.praiseCommentNumMapper.selectPKByinfo(commentId);
			// 获取记录表对象
			PraiseCommentNum praiseNum = this.praiseCommentNumMapper.selectByPrimaryKey(praiseNumId);
			if (praiseNum == null) {
				// 点赞记录表插入记录
				pn.setComment(commentId);
				pn.setPraiseNum(1);
				this.praiseCommentNumMapper.insert(pn);
				return 1;
			}
			this.praiseCommentMapper.addPraiseNum(commentId);
			return 1;
		}
		if (praise.getStatus() == 0) {
			praise.setStatus(1);
			this.praiseCommentMapper.addPraiseNum(commentId);
			this.praiseCommentMapper.updateByPrimaryKey(1,praise.getId());
			return 1;
		}

		praise.setStatus(0);
		this.praiseCommentMapper.lessenPraiseNum(commentId);
		this.praiseCommentMapper.updateByPrimaryKey(0,praise.getId());
		return 0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int showPraise(Integer informationId) throws Exception {
		// 获取主键ID
		Long praiseNumId = this.praiseNumMapper.selectPKByinfo(informationId);
		// 由主键ID获取对象
		PraiseNum praiseNum = this.praiseNumMapper.selectByPrimaryKey(praiseNumId);
		// 判断对象非空
		if (praiseNum == null) {
			throw new Exception("无点赞记录");
		}
		return praiseNum.getPraiseNum();
	}

	@Override
	public boolean statusPraise(String token, Integer informationId) throws Exception {
		// 获取用户对象
		Users user = usersService.findByToken(token);
		// 获取用户ID
		Long uId = user.getId();
		// 获取点赞表对象
		Long praiseId = this.praiseMapper.selectByUserAndInfo(uId, informationId);
		Praise praise = this.praiseMapper.selectByPrimaryKey(praiseId);

		if (praise == null) {
			return false;
		}
		int s = praise.getStatus();
		if (s == 0) {
			return false;
		}
		return true;
	}

	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	@Autowired
	public void setPraiseMapper(PraiseMapper praiseMapper) {
		this.praiseMapper = praiseMapper;
	}

	@Autowired
	public void setPraiseNumMapper(PraiseNumMapper praiseNumMapper) {
		this.praiseNumMapper = praiseNumMapper;
	}

	@Override
	public List<PraiseApp> praise(Long userId) {
		List<Long> chooseUsers = this.praiseMapper.chooseUsers(userId);
		List<Praise> praise = this.praiseMapper.Praise(userId);
		List<PraiseApp> praiseApp1 = new ArrayList<PraiseApp>();
		List<PraiseApp> praiseApp2 = new ArrayList<PraiseApp>();
		List<PraiseApp> praiseApp = new ArrayList<PraiseApp>();
		for (int i = 0; i < praise.size(); i++) {
			PraiseApp p = new PraiseApp();
			p.setInformation(praise.get(i).getInformation());
			p.setCreateTime(praise.get(i).getCreateTime());
			Long id = Long.parseLong(praise.get(i).getInformation()+"");
			log.info(id+"=========================");
			InformationAll a = this.iMapper.selectinfo(id);
			p.setTitle(a.getTitle());
			p.setTitleImg(a.getTitleImg());
			p.setWebUrl(a.getWeburl());
			praiseApp1.add(p);
		}
		for (int i = 0; i < chooseUsers.size(); i++) {
			PraiseApp p = new PraiseApp();
			p.setUserId(chooseUsers.get(i));
			Long id = chooseUsers.get(i);
			Users u = this.uMapper.selectPetNameById(id);
			p.setPetName(u.getPetName());
			p.setLoginName(u.getLoginName());
			p.setHeadImg(u.getHeadImg());
			praiseApp2.add(p);
		}
		for (int i = 0; i < praiseApp1.size(); i++) {
			PraiseApp p1 = praiseApp1.get(i);
			PraiseApp p2 = praiseApp2.get(i);
			PraiseApp p = new PraiseApp();
			p.setUserId(p2.getUserId());
			p.setPetName(p2.getPetName());
			p.setHeadImg(p2.getHeadImg());
			p.setLoginName(p2.getLoginName());
			p.setInformation(p1.getInformation());
			p.setTitle(p1.getTitle());
			p.setTitleImg(p1.getTitleImg());
			p.setWebUrl(p1.getWebUrl());
			p.setCreateTime(p1.getCreateTime());
			praiseApp.add(p);
		}
		return praiseApp;
	}

	@Override
	public int deleteInformaiton(Integer id) {
		int i = this.praiseMapper.deleteInformaiton(id);
		return i;
	}

	@Override
	public int deleteInformaitonNum(Integer id) {
		int i = this.praiseNumMapper.deleteInformaiton(id);
		return i;
	}
}