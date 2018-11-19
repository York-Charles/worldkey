package com.worldkey.controller;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.*;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.OneTypeService;
import com.worldkey.service.ThreeTypeService;
import com.worldkey.service.TwoTypeService;
import com.worldkey.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("users/information")
public class InformationController {
	@Resource
	private InformationAllService allService;
	@Resource
	private TwoTypeService twoTypeService;
	@Resource
	private OneTypeService oneTypeService;
	@Resource
	private ThreeTypeService threeTypeService;

	private static Logger log = LoggerFactory.getLogger(InformationController.class);

	/**
	 * 用户发布文章
	 * 
	 * @param id
	 *            发布的文章的id
	 */
	@RequestMapping(value = "issuance", method = RequestMethod.POST)
	@ResponseBody
	public Object issuance(Long id, HttpSession session) {
		InformationAll info = this.allService.info(id);
//		if (info.getTitle() == null || Objects.equals("", info.getTitle())) {
//			return new ResultUtil(500, "no", "标题不能为空");
//		}
//		if (info.getInfo() == null || Objects.equals("", info.getInfo())) {
//			return new ResultUtil(500, "no", "不能没有内容");
//		}

		InformationAll vo = new InformationAll();
		vo.setId(id);
		vo.setChecked(1);
		vo.setState(0);
		Users users = (Users) session.getAttribute("users");
		vo.setUsers(users);
		boolean usersInformation = this.isUsersInformation(id, users);
		if (usersInformation) {
			int checked = this.allService.checked(vo);
			return new ResultUtil(200, "ok", checked);
		} else {
			return new ResultUtil(500, "no", "权限不足");
		}

	}

	/**
	 * 写文章页面路径
	 */
	@RequestMapping("writeArticle")
	public ModelAndView writeArticle(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("usersM/information/writeArticle");
		Users users = (Users) session.getAttribute("users");
		List<InformationAll> list = this.allService.userFindDraft(users);
		modelAndView.addObject("informationList", list);
		modelAndView.addObject("information", new InformationAll());
		
		/*4.13 薛秉臣添加   （根据二级ID查三级） 2018.4.16*/
		modelAndView.addObject("threeTypes", this.threeTypeService.findByTwo(161));
		modelAndView.addObject("twoTypes", this.twoTypeService.findByOne(1));
		modelAndView.addObject("oneTypes", this.oneTypeService.findNotNull());
		return modelAndView;
	}
	
	
	/**
	 *草稿箱
	 */
	@RequestMapping("caogaoxiang")
	public ModelAndView caogaoxiang(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("usersM/information/caogaoxiang");
		Users users = (Users) session.getAttribute("users");
		List<InformationAll> list = this.allService.userFindDraft(users);
		modelAndView.addObject("informationList", list);
		modelAndView.addObject("information", new InformationAll());
		
		/*4.13 薛秉臣添加   （根据二级ID查三级） 2018.4.16*/
		modelAndView.addObject("threeTypes", this.threeTypeService.findByTwo(161));
		modelAndView.addObject("twoTypes", this.twoTypeService.findByOne(1));
		modelAndView.addObject("oneTypes", this.oneTypeService.findNotNull());
		return modelAndView;
	}

	/**
	 * 用户登陆后的文章列表界面
	 */
	@RequestMapping("list")
	public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
			@ModelAttribute(value = "select") InformationExample vo, HttpSession session) {
		Users users = (Users) session.getAttribute("users");
		vo.setUsers(users);
		ModelAndView model = new ModelAndView("usersM/information/list");
		PageInfo<InformationAll> info = this.allService.usersFindBySelective(pageNum, pageSize, vo);
		model.addObject("pageinfo", info);
		List<OneType> oneTypes = this.oneTypeService.selectAllOneTypeWithTwoType();
		model.addObject("type", oneTypes);
		/* System.out.println(oneTypes); */
		return model;
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(@Validated @ModelAttribute("information") InformationExample vo,
			@RequestHeader("host") String host, HttpSession session) {
		Users users = (Users) session.getAttribute("users");
		if (users == null) {
			return "redirect:/users/login";
		}
		vo.setUsers(users);
		vo.setAuther(users.getPetName());
		this.allService.add(vo, host);
		return "redirect:/users/information/writeArticle" ;
	}
	
	/**
	 * 创建状态：
	 * 		id:自增
	 * 		title:新建文章
	 * 		checked:2
	 * 		type:null
	 * @param vo
	 * @param host
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "emptyFrame", method = RequestMethod.POST)
	public String emptyFrame(@Validated @ModelAttribute("information") InformationExample vo,
			@RequestHeader("host") String host, HttpSession session) {
		Users users = (Users) session.getAttribute("users");
		if (users == null) {
			return "redirect:/users/login";
		}
		vo.setUsers(users);
		vo.setAuther(users.getPetName());
		vo.setDraft(1);
		this.allService.add(vo, host);
		return "redirect:/users/information/writeArticle";
	}
	
	/*
	 * @RequestMapping(value = "add") public String add(Model model) {
	 * model.addAttribute("information", new InformationExample());
	 * model.addAttribute("twotypes", this.twoTypeService.findByOne(1));
	 * model.addAttribute("onetypes", this.oneTypeService.findNotNull()); return
	 * "usersM/information/add"; }
	 */

	@RequestMapping(value = "ajaxUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Object ajaxUpdate(@RequestHeader("host") String host, String info, String title, String abstracte, Long id,
			String titleImg, MultipartFile file, HttpSession session, String oldTitleImg) {

		Users users = (Users) session.getAttribute("users");

		if (users == null) {
			return "登陆失效";
		}
		if (!isUsersInformation(id, users)) {
			return "权限不足";
		}

		String type = "";
		InformationAll informationAll = new InformationAll();
		if (id == null) {
			return null;
		}
		informationAll.setId(id);

		if (title != null) {
			informationAll.setTitle(title);
			type = "title";
		}

		if (abstracte != null) {
			informationAll.setAbstracte(abstracte);
			type = "abstracte";
		}

		if (info != null) {
			informationAll.setInfo(info);
			informationAll.setTitleImg(titleImg);
			type = "info";
		}
		InformationAll update = this.allService.update(informationAll, host, file, oldTitleImg);
		String result;
		switch (type) {
		case "title":
			result = update.getTitle();
			break;
		case "abstracte":
			result = update.getAbstracte();
			break;
		case "info":
			result = update.getInfo();
			break;
		default:
			result = "no";
		}

		return result;
	}

	@RequestMapping(value = "update", params = "title")
	public String update(@Validated @ModelAttribute("info") InformationExample vo, Errors errors,
			@RequestHeader("host") String host, MultipartFile file, HttpSession session, Model model) {
		Users users = (Users) session.getAttribute("users");
		vo.setUsers(users);
		if (errors.hasErrors()) {
			InformationExample info = this.allService.getAllMapper().selectById(vo.getId());
			if (!(users.getId().equals(info.getUser()))) {
				return "redirect:/users/information/list";
			}
			model.addAttribute("info", info);
			model.addAttribute("onetypes", this.oneTypeService.findNotNull());
			model.addAttribute("twotypes", this.twoTypeService.findByOne(1));
			return "usersM/information/update";
		}
		InformationAll co = this.allService.update(vo, host, file, "");
		if (co == null) {
			InformationExample info = this.allService.getAllMapper().selectById(vo.getId());
			if (!(users.getId().equals(info.getUser()))) {
				return "redirect:/users/information/list";
			}

			model.addAttribute("onetypes", this.oneTypeService.findNotNull());
			model.addAttribute("twotypes", this.twoTypeService.findByOne(1));
			return "usersM/information/update";
		}
		return "redirect:/users/information/list";

	}
	/*
	@RequestMapping(value = "update/{id}")
	public String update(Model model, @PathVariable Long id, HttpSession session) {
		Users users = (Users) session.getAttribute("users");
		InformationExample info = this.allService.getAllMapper().selectById(id);
		if (!(users.getId().equals(info.getUser()))) {
			return "redirect:/users/information/list";
		}
		model.addAttribute("info", this.allService.info(id));
		TwoType byId = this.twoTypeService.findById(info.getType());
		model.addAttribute("twoType", byId);
		model.addAttribute("oneType", this.oneTypeService.findById(byId.getOneType()));
		return "usersM/information/updateWrite";
	}
	*/
	/*
	 * 以下2018.4.16更新
	 */
//	@RequestMapping(value = "update/{id}")
//	public String update(Model model, @PathVariable Long id, HttpSession session) {
//		Users users = (Users) session.getAttribute("users");
//		InformationExample info = this.allService.getAllMapper().selectById(id);//根据Id查询InformationAll的某条信息(含内容)
//		if (!(users.getId().equals(info.getUser()))) {
//			return "redirect:/users/information/list";
//		}
//		TwoType byId = null;
//		model.addAttribute("info", this.allService.info(id));// 增加点击次数，根据传入Id选择InformationAll的一条数据
//		ThreeType byId2 = this.threeTypeService.findById(info.getType());//根据该info的type查询到threeType表中对应的type
//		if (byId2 != null) {//验证是否存在
//			byId = this.twoTypeService.findById(byId2.getTwoType());//若存在，则判断info的type对应TwoType的type
//			model.addAttribute("threeType", byId2);
//		} else {
//			byId = this.twoTypeService.findById(info.getType());
//		}
//		model.addAttribute("twoType", byId);
//		model.addAttribute("oneType", this.oneTypeService.findById(byId.getOneType()));
//		return "usersM/information/updateWrite";
//	}
	/**
	 * 
	 * @param model
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "update/{id}")
	public String update(Model model, @PathVariable Long id, HttpSession session) {
		Users users = (Users) session.getAttribute("users");
		InformationExample info = this.allService.getAllMapper().selectById(id);
		if (!(users.getId().equals(info.getUser()))) {
			return "redirect:/users/information/list";
		}
		model.addAttribute("info", this.allService.info(id));
		return "usersM/information/updateWrite";
	}
	/**
	 * @param id
	 *            要删除的记录的主键
	 * @return ResultUtil
	 */
	@RequestMapping("del")
	@ResponseBody
	public ResultUtil del(@NotNull Long id, HttpSession session) {
		Users user = (Users) session.getAttribute("users");
		if (this.isUsersInformation(id, user)) {
			return new ResultUtil(200, "ok", this.allService.delById(id));
		}
		return new ResultUtil(406, "权限不足", "这不是你的文章");

	}

		

	@RequestMapping("updateType")
	public ModelAndView updateType(@Validated @ModelAttribute("information") InformationExample vo,Model model,HttpSession session){
		 InformationAll info = this.allService.info(vo.getId());
		 info.setType(vo.getType());
		 info.setPointNumber(0);
		 this.allService.updateType(info);
		 ModelAndView modelAndView = this.writeArticle(session);
		 ThreeType threeT = this.threeTypeService.findByType(vo.getType());
		 if(vo.getType() >= 10000){
			 modelAndView.addObject("threeType", threeT.getTypeName());
		 } else if(vo.getType() >=63 && vo.getType() <= 202){
			 modelAndView.addObject("threeType", null);
		 }
		 TwoType byId = this.twoTypeService.findByThree(vo.getType());
		 modelAndView.addObject("twoType", byId.getTypeName());
		 OneType oneT = this.oneTypeService.selectByTwoType(byId.getId());
		 modelAndView.addObject("oneType", oneT.getTypeName());
		 return modelAndView;
	}
	
	private boolean isUsersInformation(Long id, Users user) {
		InformationAll info = this.allService.info(id);
		Long id1 = info.getUsers().getId();
		Long id2 = user.getId();
		if (Objects.equals(id1, id2)) {
			return true;
		} else {
			log.error("id:{},infoUserId:{},userId:{}", id, id1, id2);
			return false;
		}
	}

}
