package com.worldkey.controller;

import com.worldkey.entity.Action;
import com.worldkey.service.ActionService;
import com.worldkey.service.RoleService;
import com.worldkey.util.ResultUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("action")
public class ActionController {
	@Resource
	private ActionService actionService;
	@Resource
	private RoleService roleService;
	
	private Logger log=LoggerFactory.getLogger(ActionController.class);
	
	/**
	 * 获取全部的action
	 */
	@RequiresPermissions("action:list")
	@RequestMapping("list")
	public String list(Model model){
		model.addAttribute("list", this.actionService.findAll());
		return "power/action/list";}
	
	/**
	 * 添加权限
	 */
	@RequiresPermissions("action:add")
	@RequestMapping("add")
	public String actionAdd(Action vo, Integer role, Model model){
		log.debug("vo==null :"+(vo==null));
		model.addAttribute("roles", roleService.findAll());

		if (vo != null&&vo.getUrl()==null) {
			return "power/action/add";
		}
		  int rs=this.actionService.add(vo,role);
		  if (rs==1) {
			  return "redirect:/power/roles";
		}
		return "manage/action?msg=错误";
	}
	/**
	 * 更新权限
	 */
	@RequiresPermissions("action:update")
	@RequestMapping("update")
	public String actionUpdate(Action vo, Model model){
		if (vo.getUrl()==null) {
			//Action action=this.actionService.findById(vo.getId());
			//action.setRoles(this.roleService.findAll());
			model.addAttribute("info", this.actionService.findById(vo.getId()));
			//model.addAttribute("roles", this.roleService.findAll());
			return "power/action/update";
		}
		int rs=this.actionService.updateById(vo);
		if (rs==1) {
			return "redirect:/action/list";
		}
		return "manage/action?msg=错误";
	}
	/**
	 * 删除权限，带外键连接判定
	 */
	@RequiresPermissions("action:del")
	@RequestMapping("del/{id}")
	@ResponseBody
	public ResultUtil actionDel(@PathVariable Integer id){
		int rs=this.actionService.del(id);
		if (rs==1) {
			return new ResultUtil(200, "ok", rs);
		}
		return new ResultUtil(200, "no","该记录的主键可能被其他记录的外键关联，请处理关联后在删除");
	}
	
	
	
	
}
