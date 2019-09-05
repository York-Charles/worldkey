package com.worldkey.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Apply;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Users;

/**
 * 乐赠模块，申请接口
 *
 */
public interface ApplyService extends IService<Apply> {
	/**
	 * 执行申请好物操作
	 */
	Integer addApply(String token, Long information);

	/**
	 * 查询(总分享、分享中、分享成功)的详情操作
	 */
	PageInfo<InformationAll> findByUsersId(Integer pageNum, Integer pageSize, Long users, Integer type);

	/**
	 * 执行生成(申请)订单操作
	 */
	Integer addApplyRecord(String token, Long information, Long usersBuyer);

	/**
	 * 显示申请和申请成功的用户
	 * 
	 * Integer showApply(String token);
	 */
	/*
	 * 显示当前登录者申请状态
	 */
	Integer showApplyStatus(String token, Long information);

	/*
	 * 展示选中者和未选中者
	 */
	PageInfo<Users> showUsers(Integer pageNum, Integer pageSize, Long information, Integer type);

}
