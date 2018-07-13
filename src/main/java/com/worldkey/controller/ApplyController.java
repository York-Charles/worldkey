package com.worldkey.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Apply;
import com.worldkey.entity.ApplyRecord;
import com.worldkey.entity.BaseShow;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Users;
import com.worldkey.mapper.ApplyMapper;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.service.ApplyService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;

/**
 * 乐赠模块接口
 *
 */

@RestController
@RequestMapping("apply")
public class ApplyController {
	@Resource
	private ApplyService applyService;
	@Resource
	private UsersService usersService;
	@Resource
	private ApplyMapper applyMapper;
	@Resource
	private InformationAllMapper informationAllMapper;

	/**
	 * 查询当前用户(买方)的申请状态(已报名、未报名、已选中)
	 */
	@RequestMapping("showAS/{token}")
	public ResultUtil showApplyStatus(@PathVariable String token, Long information) {
		Users users = usersService.findByToken(token);
		if (users == null || token == null) {
			return new ResultUtil(200, "ok", "user null");
		}
		Integer i = applyService.showApplyStatus(token, information);
		// 已报名
		if (i == 1) {
			return new ResultUtil(200, "ok", "apply start");
			// 已选中
		} else if (i == 2) {
			return new ResultUtil(200, "ok", "apply checked");
		}
		// 未选中
		return new ResultUtil(200, "ok", "apply end");
	}

	/**
	 * 当前用户(买方)执行，我想要、申请操作的接口
	 */
	@RequestMapping("all/{token}")
	public ResultUtil apply(@PathVariable String token, Long information) {
		// 验证是否登录
		Users users = usersService.findByToken(token);
		if (users == null || token == null) {
			return new ResultUtil(200, "ok", "user null");
		}
		// 商品发布者和商品申请者不能是同一个人，确保不是自己买自己的情况！
		InformationAll info = informationAllMapper.selectByPrimaryKey(information);
		if (info.getUsers().getId().equals(users.getId())) {

			return new ResultUtil(200, "ok", "error");
		}

		Integer i = applyService.addApply(token, information);
		if (i == 1) {
			return new ResultUtil(200, "ok", "apply start");
		}
		return new ResultUtil(200, "ok", "apply end");
	}

	/**
	 * 当前用户(卖方)获取分享的物品详情 类型type=1:总的分享详情 、type=2:正在分享中、type=3:分享成功的
	 */
	@RequestMapping("getInfo")
	public ResultUtil applyNum(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
								@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
								Long users, Integer type) {
		PageHelper.startPage(pageNum,pageSize);
		PageInfo<InformationAll> info = applyService.findByUsersId(pageNum, pageSize, users, type);

		return new ResultUtil(200, "ok", info);
	}

	/**
	 * 未登录状态(买家看到的 ) 物品下方的 显示申请人、选中人，报名管理中(已选中和未选中) 不使用分页写
	 */
	@RequestMapping("num")
	public ResultUtil applyNum(Long information) {
		List<Apply> applys = applyMapper.findByinfoId(information);
		List<ApplyRecord> applyss = applyMapper.findByinfooo(information);
		List<Object> list = new ArrayList<>();
		list.add(0, applyss);
		list.add(1, applys);
		/*薛秉臣  判断该物品是否被选中  
		Integer a = applyMapper.Checked(information);
		if(a == 1){
			return new ResultUtil(200, "ok", "get the nod");
		}
		 */
		return new ResultUtil(200, "ok", list);
	}

	/**
	 * 互相查看对方的个人首页，获取分享总数量，分享中的数量，和分享结束的数量
	 */
	@RequestMapping("applyInfo")
	public ResultUtil applyInfo(Long users) {

		// 获取个人总分享数
		Integer infoTotle = applyMapper.findByinfo101(users);
		// 获取个人正在分享中的
		Integer infoIng = applyMapper.findByinfoNotin(users);
		// 获取个人分享成功的
		Integer infoOver = applyMapper.findByinfoOver(users);

		List<Object> list = new ArrayList<>();
		// 分享总数
		list.add(0, infoTotle);
		// 分享中的数
		list.add(1, infoIng);
		// 分享成功的
		list.add(2, infoOver);
		return new ResultUtil(200, "ok", list);
	}

	/**
	 * 卖方在申请表中执行选中(送出)的接口
	 */
	@RequestMapping("addRecord/{token}")
	public ResultUtil addRecord(@PathVariable String token, Long usersBuyer, Long information) {
		Users users = usersService.findByToken(token);
		if (users == null) {
			return new ResultUtil(200, "ok", "user null");
		}
		Integer i = applyService.addApplyRecord(token, information, usersBuyer);
		if (i == 0) {
			return new ResultUtil(200, "ok", "error");
		}
		return new ResultUtil(200, "ok", "give success");
	}

	/**
	 * 显示已选中和未选中用户
	 */
	@RequestMapping("showUsers")
	public ResultUtil showUsers(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
								@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, 
								Long information,
								Integer type) {
		PageHelper.startPage(pageNum,pageSize);
		PageInfo<Users> usersList = applyService.showUsers(pageNum, pageSize, information, type);
		return new ResultUtil(200, "ok", usersList);
	}

	/**
	 * 显示已选中和未选中数量
	 */
	@RequestMapping("showApplyNum")
	public ResultUtil showApplyNum(Long information) {
		Integer a0 = applyMapper.showApplyNum0(information);
		Integer a1 = applyMapper.showApplyNum1(information);
		List<Integer> num = new ArrayList<>();
		num.add(0, a1);
		num.add(1, a0);
		return new ResultUtil(200, "ok", num);
	}

	/**
	 * 获取当前文章的总报名数
	 */
	@RequestMapping("showApplyNumAll")
	public ResultUtil showApplyNumAll(Long information) {
		Integer i = applyMapper.showApplyNum2(information);
		return new ResultUtil(200, "ok", i);
	}

	/*
	 * 显示个人申请详情defaultValue = "10"
	 */

	@RequestMapping("showMyApply/{token}")
	@ResponseBody
	public ResultUtil showShenqing(@RequestParam(value = "pageNum", defaultValue = "1")  Integer pageNum,
								   @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize, 
								   @PathVariable String token) {
		
	
		Users users = usersService.findByToken(token);
		if (users == null) {
			return new ResultUtil(200, "ok", "user null");
		}
		PageHelper.startPage(pageNum,pageSize);
		List<BaseShow> baseShows= this.applyMapper.findBaseShow(users.getId());
		//PageInfo<BaseShow> allList = new PageInfo<>(baseShows);
		return new ResultUtil(200, "ok", new PageInfo<>(baseShows));
	}
	
	
}
