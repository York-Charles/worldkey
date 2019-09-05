package com.worldkey.controller;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Admin;
import com.worldkey.entity.AdminRole;
import com.worldkey.entity.Role;
import com.worldkey.jdpush.Jdpush;
import com.worldkey.service.AdminService;
import com.worldkey.service.RoleService;
import com.worldkey.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("admin")
public class AdminController {
	@Resource
	private AdminService adminService;
	@Resource
	private RoleService roleService;

	/**
	 * 重置管理员密码
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("admin:reloadpwd")
	@RequestMapping("reloadpwd")
	@ResponseBody
	public ResultUtil reloadpwd(Integer id, Model model) {
		Admin admin = new Admin();
		admin.setId(id);
		admin.setPassword("123456");
		return new ResultUtil(200, "ok", this.adminService.update(admin));
	}

	/**
	 * 更新管理员信息
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequiresPermissions("admin:update")
	@RequestMapping("update")
	public String update(@Validated Admin vo, Model model) {
		if (vo.getId() != null && vo.getName() == null) {
			Admin a = this.adminService.selectByPrimaryKey(vo.getId());
			model.addAttribute("vo", a);
			return "admin/update";
		}
		this.adminService.update(vo);
		return "redirect:/admin/list";
	}

	/**
	 * 为管理员添加角色
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequiresPermissions("admin:role/add")
	@RequestMapping("role/add")
	public String addRole(AdminRole vo, Model model, HttpSession session) {
		if (vo.getAdmin() != null && vo.getRole() == null) {
			Set<Role> all = this.roleService.findAll();
			all.removeAll(this.adminService.findRoleByAdmin(vo.getAdmin()));
			model.addAttribute("rolelist", all);
			model.addAttribute("admin", vo.getAdmin());
			return "admin/roleadd";
		}
		Admin admin = (Admin) session.getAttribute("admin");
		this.adminService.addRole(vo, admin.getName());
		return "redirect:/admin/list";
	}

	@RequestMapping("toLogin")
	public String toLogin() {
		return "admin/login";
	}

	@RequestMapping("login")
	public String login(@ModelAttribute("admin") Admin admin, HttpServletRequest request, Model model) {
		UsernamePasswordToken token = new UsernamePasswordToken(admin.getName(), admin.getPassword());
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			Admin adminCo = (Admin) subject.getPrincipal();
			HttpSession session = request.getSession();
			session.setAttribute("admin", adminCo);
			return "redirect:/";
		} catch (Exception e) {
			// e.printStackTrace();
			model.addAttribute("adminerr", "用户名或密码错误");
			return "admin/login";
		}
	}

	/**
	 * 添加管理员
	 * 
	 * @param vo
	 *            Admin
	 * @return
	 */
	@RequiresPermissions("admin:add")
	@RequestMapping("add")
	public String add(@Validated Admin vo) {
		if (vo.getPassword() == null) {
			return "admin/add";
		}
		this.adminService.add(vo);
		return "redirect:/admin/list";
	}

	/**
	 * 删除管理员
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("admin:del")
	@RequestMapping("del")
	@ResponseBody
	public ResultUtil del(Integer id) {
		return new ResultUtil(200, "ok", this.adminService.del(id));
	}

	/**
	 * 查看管理员信息
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:list")
	@RequestMapping("list")
	public String list(Admin vo, Model model, @RequestParam(name = "pagenum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pagesize", defaultValue = "10") Integer pageSize) {
		PageInfo<Admin> info = this.adminService.findBySelective(vo, pageNum, pageSize);
		model.addAttribute("pageinfo", info);
		model.addAttribute("select", vo);
		return "admin/list";
	}

	/**
	 * 通过用户Id获取用户的角色信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("admin:roles")
	@RequestMapping("roles")
	public String seeRole(Integer id, Model model) {
		List<Role> listAR = this.adminService.findRoleByAdmin(id);
		model.addAttribute("list", listAR);
		model.addAttribute("id", id);
		return "admin/rolelist";
	}

	/**
	 * 删除用户的角色
	 * 
	 * @param ar
	 * @return
	 */
	@RequiresPermissions("admin:role/del")
	@RequestMapping("role/del")
	@ResponseBody
	public ResultUtil delRole(AdminRole ar, HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");
		return new ResultUtil(200, "ok", this.adminService.delRole(ar, admin.getName()));
	}

	@RequestMapping("logout")
	public String logout(HttpSession session) {
		Subject subject = SecurityUtils.getSubject();
		session.removeAttribute("admin");
		subject.logout();
		return "ok";
	}

}
