package com.worldkey.controller;

import com.worldkey.entity.Action;
import com.worldkey.entity.Role;
import com.worldkey.entity.RoleAction;
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
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限管理
 *
 * @author HP
 */
@Controller
@RequestMapping("power")
public class PowerController {
	@Resource
	private ActionService actionService;
	@Resource
	private RoleService roleService;
	
	private Logger log=LoggerFactory.getLogger(PowerController.class);
	/**
	 * 删除角色的权限
	 */
	@RequiresPermissions("power:role/delaction")
	@RequestMapping("role/delaction")
	@ResponseBody
	public ResultUtil delAction(RoleAction vo){
		return new ResultUtil(200, "ok", this.roleService.delAction(vo));
	}
	
	
	/**
	 * 查看所有角色的信息
	 */
	@RequiresPermissions("power:roles")
	@RequestMapping("roles")
	public ModelAndView roleList(){
		ModelAndView model=new ModelAndView("power/role/list");
		model.addObject("list", this.roleService.findJustRole());
		return model;
	}
	/**
	 * 获取对应角色的权限表
	 * @param id 角色的ID
	 */
	@RequiresPermissions("power:action")
	@RequestMapping("action/{id}")
	public ModelAndView findActionByRole(@PathVariable Integer id){
		ModelAndView model=new ModelAndView("power/role/actionlist");
		model.addObject("list", this.actionService.findByRoleId(id));
		model.addObject("roleid", id);
		return model;
	}
	/**
	 * 添加角色
	 */
	@RequiresPermissions("power:role/add")
	@RequestMapping("role/add")
	public String add(Role vo){
		if (vo!=null&&vo.getName()==null) {
			return "power/role/add";
		}
		this.roleService.add(vo);
		return "redirect:/power/roles";
	}
	/**
	 * 删除角色
	 */
	@RequiresPermissions("power:role/del")
	@RequestMapping("role/del/{id}")
	@ResponseBody
	public ResultUtil del(@PathVariable Integer id){
		return new ResultUtil(200, "ok", this.roleService.del(id));
	}
	@RequiresPermissions("power:role/update")
	@RequestMapping("role/update")
	public String update(Role vo, Model model){
		if (vo!=null&&vo.getName()==null) {
			model.addAttribute("info", this.roleService.findById(vo.getId()));
			return "power/role/update";
		} 
		this.roleService.update(vo);
		return "redirect:/power/roles";
	}
	/**
	 * 为角色添加action
	 */
	@RequiresPermissions("power:role/addaction")
	@RequestMapping("role/addaction")
	public String addAction(RoleAction vo,Integer roleid,Model model){
		log.info("到达role/addaction");
		if (roleid!=null) {
			Role info=this.roleService.findById(roleid);
			model.addAttribute("info", info);
			List<Action>now=this.actionService.findByRoleId(roleid);
			List<Action>all=this.actionService.findAll();
			/*for (Action action : all) {
				if (action.getBar()==0) {
					now.add(action);
				}
			}*/
			all.removeAll(now);
			model.addAttribute("actionlist",all);
			return "power/role/addaction";
		}
		this.roleService.addAction(vo);
		return "redirect:/power/roles";
	}
	
}
