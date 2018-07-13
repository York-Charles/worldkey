package com.worldkey.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Apply;
import com.worldkey.entity.ApplyRecord;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Users;
import com.worldkey.mapper.ApplyMapper;
import com.worldkey.mapper.ApplyRecordMapper;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.ApplyService;
import com.worldkey.service.UsersService;

/**
 * 乐赠模块，申请物品(我想要)实现类
 *
 */
@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {

	@Resource
	private UsersService usersService;
	@Resource
	private ApplyMapper applyMapper;
	@Resource
	private ApplyRecordMapper applyRecordMapper;
	@Resource
	private InformationAllMapper informationAllMapper;
	@Resource
	private UsersMapper usersMapper;

	/**
	 * 添加申请(我想要)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer addApply(String token, Long information) {
		// 获取用户对象
		Users users = usersService.findByToken(token);

		// 获取申请表实体
		Apply apply = applyMapper.findByuIdAndinfoId(users.getId(), information);

		// 创建申请表对象
		Apply a0 = new Apply();
		// 实体不存在时，表示未曾申请,开始执行申请操作
		if (apply == null) {
			a0.setUsers(users.getId());
			a0.setInformation(information);
			a0.setCreateTime(new Date());
			a0.setStatus(1);
			// 改变申请状态
			this.applyMapper.insert(a0);
			return 1;
		}
		// 实体存在时，状态为0，表示未曾申请；状态为1，表示已经申请
		if (apply != null) {
			int i = apply.getStatus();
			int c =apply.getChecked();
			// 执行取消申请
			if (i == 1&& c==0) {
				applyMapper.updateStatus0(users.getId(), information);
				return 0;
			}
			//已申请且被选中 取消时，状态变为0，applyRecord 记录删除
			if(i==1&& c==1){
				//申请状态变为0
				applyMapper.updateStatus0(users.getId(), information);
				//选中状态变为0
				applyMapper.updateChecked0(users.getId(), information);
				//applyRecord 记录删除
				applyMapper.deleteApplyRecord(users.getId(), information);
				return 0;
			}
			// 执行添加申请
			if (i == 0) {
				applyMapper.updateStatus1(users.getId(), information);
				return 1;
			}
		}
		return 1;
	}

	/**
	 * 个人总分享详情、分享中详情、分享成功详情
	 */
	@Override
	public PageInfo<InformationAll> findByUsersId(Integer pageNum, Integer pageSize, Long users, Integer type) {
		PageHelper.startPage(pageNum, pageSize);

		// 个人总分享详情
		if (type == 1) {
			List<InformationAll> infoList = applyMapper.findInfoList(users);
			return new PageInfo<>(infoList);
		}
		
		
		
		
		// 分享中详情
		if (type == 2) {
			List<InformationAll> infoListIng = applyMapper.findInfoListIng(users);
			return new PageInfo<>(infoListIng);
		}
		// 分享成功详情
		if (type == 3) {
			List<InformationAll> infoListEnd = applyMapper.findInfoListEnd(users);
			return new PageInfo<>(infoListEnd);
		}
		return null;

	}

	/**
	 * 生成订单的操作 ，token为卖方的登录信息
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer addApplyRecord(String token, Long information, Long usersBuyer) {
		Users users = usersService.findByToken(token);
		Users uBuyer = usersMapper.selectByPrimaryKey(usersBuyer);
		// 验证订单记录表中，此物品是否存在
		ApplyRecord applyRecord = this.applyMapper.findByinfo(information);
		if (applyRecord != null) {
			return 0;
		}

		// 通过文章ID,验证作者是否匹配该登录的token用户
		InformationAll info = informationAllMapper.selectByPrimaryKey(information);
		if (!(users.getId()).equals(info.getUsers().getId())) {
			return 0;
		}

		// 验证买方是否已经申请(我想要)
		Apply apply = applyMapper.findByuIdAndinfoId(usersBuyer, information);
		if (apply.getStatus().equals(0)) {
			return 0;
		}
		// 执行选中买方的操作
		ApplyRecord applyRecordNew = new ApplyRecord();
		applyRecordNew.setCreateTime(new Date());
		applyRecordNew.setUsersSeller(users.getId());
		applyRecordNew.setInformation(information);
		applyRecordNew.setUsersBuyer(usersBuyer);
		apply.setChecked(1);
		String msg = "<" + info.getAuther() + ">将ID为<" + information + ">的宝贝乐赠给<" + uBuyer.getPetName() + ">";
		applyRecordNew.setMsg(msg);
		this.applyMapper.updateById(apply);
		this.applyRecordMapper.insert(applyRecordNew);
		return 1;
	}
	/*
	 * @Override public Integer showApply(String token){ Users
	 * users=usersService.findByToken(token); //获取个人总分享数 List<InformationAll>
	 * infoTotle=applyMapper.findByinfo101(users.getId()); //获取个人正在分享中的
	 * List<InformationAll> infoIng=applyMapper.findByinfoNotin(users.getId());
	 * List<InformationAll> infoOver=applyMapper.findByinfoOver(users.getId());
	 * 
	 * return null; }
	 */

	/*
	 * 
	 * 显示当前登录用户的申请状态(已报名、未报名、已选中)
	 */
	@Override
	public Integer showApplyStatus(String token, Long information) {
		Users users = usersService.findByToken(token);
		Apply a = applyMapper.findApplyStatus(information, users.getId());
		if (a == null) {
			return 0;
		}
		// 未报名
		if (a.getStatus() == 0) {
			return 0;
		}
		// 已报名 已选中
		if (a.getStatus() == 1 && a.getChecked() == 1) {
			return 2;
		}
		return 1;

	}

	/*
	 * 显示已选中和未选中用户
	 */
	@Override
	public PageInfo<Users> showUsers(Integer pageNum, Integer pageSize, Long information, Integer type) {
		PageHelper.startPage(pageNum, pageSize);
		// 已选中
		if (type == 1) {
			List<Users> usersList1 = this.applyMapper.showUsers1(information);
			return new PageInfo<>(usersList1);
		}
		// 未选中
		List<Users> usersList0 = this.applyMapper.showUsers0(information);
		return new PageInfo<>(usersList0);

	}

	/*
	 * 我的申请列表
	 * 1、总申请 
	 * 2、申请中 
	 * 3、申请成功
	 
	@Override 
	public PageInfo<InformationAll> findInfoshenqingzhe(Integer pageNum, Integer pageSize, String token){ 
		PageHelper.startPage(pageNum, pageSize); 
		Users users = usersService.findByToken(token); 
		
		//获取该用户下所有的申请实体
		List<Apply> applys=this.applyMapper.findApplyObject(users.getId());
		for(){}
	
*/
	


}
