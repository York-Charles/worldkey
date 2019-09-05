package com.worldkey.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.worldkey.entity.UserRelation;
import com.worldkey.entity.Users;
import com.worldkey.service.UserRelationService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;

@Controller
@ResponseBody
@RequestMapping("userRelation")
public class UserRelationController {

	@Resource
	private UserRelationService userRelationService;
	@Resource
	private UsersService uService;
	
//	@RequestMapping("takeAction/{token}")
//	public ResultUtil takeAction(Integer userId,@PathVariable String token){
//		UserRelation ur = new UserRelation();
//		Users user = this.uService.findByToken(token);
//		ur.setFollower(Integer.parseInt(user.getId()+""));
//		ur.setLeader(userId);
//		if(!isExist(ur)){
//			this.userRelationService.insertUR(ur);
//			return new ResultUtil(200,"ok",0);
//		}else{
//			this.userRelationService.deleteUR(ur);
//			return new ResultUtil(200,"ok",1);
//		}
//	}
	@RequestMapping("takeAction")
	public ResultUtil takeAction(Integer userId, String token){
		UserRelation ur = new UserRelation();
		Users user = this.uService.findByToken(token);
		ur.setFollower(Integer.parseInt(user.getId()+""));
		ur.setLeader(userId);
		if(!isExist(ur)){
			this.userRelationService.insertUR(ur);
			return new ResultUtil(200,"ok",0);
		}else{
			this.userRelationService.deleteUR(ur);
			return new ResultUtil(200,"ok",1);
		}
	}
	
//	@RequestMapping("actionState/{token}")
//	public ResultUtil actionState(Integer userId,@PathVariable String token){
//		Users user = this.uService.findByToken(token);
//		return new ResultUtil(200,"ok",this.userRelationService.actionState(Integer.parseInt(user.getId()+""), userId));
//	}
	@RequestMapping("actionState")
	public ResultUtil actionState(Integer userId, String token){
		Users user = this.uService.findByToken(token);
		return new ResultUtil(200,"ok",this.userRelationService.actionState(Integer.parseInt(user.getId()+""), userId));
	}
	
	private boolean isExist(UserRelation userRelation){
		boolean flag;
		if(this.userRelationService.selectUR(userRelation)==null){
			flag = false;
		}else{
			flag = true;
		}
		return flag;
	}
}
