package com.worldkey.controller;

import com.github.pagehelper.PageInfo;
import com.worldkey.check.user.Login;
import com.worldkey.entity.Admin;
import com.worldkey.entity.Comment;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Users;
import com.worldkey.jdpush.Jdpush;
import com.worldkey.service.CommentService;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import com.worldkey.worldfilter.WordFilter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HP
 * 用户管理
 */
@Controller
@RequestMapping("users")
public class UsersManageController {
	@Resource
	private UsersService usersService;
	 @Resource
	    private CommentService service;
	 @Resource
	    private InformationAllService informationAllService;
	 @Resource
	    private WordFilter wordFilter;


	private static Logger log= LoggerFactory.getLogger(UsersManageController.class);


	@RequiresRoles("usersManage")
	@RequestMapping("getExcel")
	public void getExcel(HttpServletResponse response) throws IOException {
		usersService.getUsersExcel(response);
	}

	@RequestMapping("list")
	@ResponseBody
	@RequiresRoles("usersManage")
	public ModelAndView list(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
		ModelAndView modelAndView=new ModelAndView("users/list");
		PageInfo<Users> all = this.usersService.findAll(pageNum, pageSize);
		modelAndView.addObject("pageinfo",all);
		return modelAndView;
	}


	@RequestMapping("dologin")
	@ResponseBody
	public Object doLogin(/*@Validated(Login.class) Users users, BindingResult errors,*/HttpServletRequest request, Model model, HttpSession session){
		String loginName = request.getParameter("loginName");
		String password = request.getParameter("password");
		
		Users users = new Users();
		users.setLoginName(loginName);
		users.setPassword(password);
		/*log.error("users1:{}",users);*/
		/*if (errors.hasErrors()) {
			model.addAttribute("users", users);
			return errors.getFieldErrors();
		}*/
		log.debug("users2:{}",users);
		Users co=this.usersService.login(users);
		if (co!=null) {
			session.setAttribute("users", co);
			return "ok";
		}		
		else{
			/*model.addAttribute("userserr", "用户名或密码错误");
			model.addAttribute("users", users);*/
			return "用户名或密码错误";
		}
	}
	@RequestMapping("manage")
	public String manage(){
		return "usersM/manage";
	}
	
	@RequestMapping("login")
	public String login(Model model){
		model.addAttribute("users", new Users());
		 model.addAttribute("admin",new Admin());
		return "manage/login";
	}
	@RequestMapping("")
	public String login(){
		return "redirect:/users/login";
	}
	
//	@RequestMapping("commentLogin")
//	@ResponseBody
//	public Object commentLogin(/*@Validated(Login.class) Users users, BindingResult errors,*/HttpServletRequest request, Model model, HttpSession session,Comment comment, BindingResult result){
//		String loginName = request.getParameter("loginName");
//		String password = request.getParameter("password");
//		
//		Users users = new Users();
//		users.setLoginName(loginName);
//		users.setPassword(password);
//
//		Users co=this.usersService.login(users);
//		if (co!=null) {
//			session.setAttribute("users", co);
//			
//			try {
//				this.add(comment, result, co);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			return "ok";
//		}else{
//
//			return "用户名或密码错误";
//		}
//	}
//
//	 private ResultUtil add(@Validated({com.worldkey.check.comment.Comment.class}) Comment comment, BindingResult result, Users user) throws Exception {
//	        //参数规则校验
//	        Map<String, String> errorMap = this.check(result);
//	        if (errorMap != null) {
//	            return new ResultUtil(406, "no", errorMap);
//	        }
//	       
//	        //敏感词过滤
//	        String infoStr = comment.getInfo();
//	        boolean contains = wordFilter.isContains(infoStr);
//	        if (contains) {
//	            return new ResultUtil(406, "no", "存在非法词语,请文明用语");
//	        }
//	        Long information = comment.getInformation();
//	        InformationAll info = this.informationAllService.info(information);
//	        //验证商品是否存在
//	        
//	        //4.29
//	        String s = info.getUsers().getLoginName();
//	        String a = comment.getInfo();//获取评论内容
//	        Long a1 = info.getUsers().getId();
//	        if (info == null) {
//	            return new ResultUtil(406, "no", "商品不存在");
//	        }
//	        comment.setUsers(user);
//	        comment.setGmtCreate(new Date());
//	        comment.setAuthor(info.getUsers().getId());
//	        Integer insert = this.service.insert(comment);
//	        //4.29
//	        HashMap<String, String> map = new HashMap<String, String>();
//	        map.put("img", user.getHeadImg());
//	        map.put("name", user.getPetName());
//	        map.put("Mid", Long.toString(user.getId()));
//	        map.put("infoImg",info.getTitleImg());
//	        Date day=new Date();
//	        SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm"); 
//	        String date = df.format(day);
//	        map.put("date", date);
//	        map.put("webUrl", info.getWeburl());
//	        map.put("id", Long.toString(info.getId()));
//	        map.put("fenlei", "1");
//	        map.put("content",comment.getInfo());
//	        map.put("title", info.getTitle());
//	        map.put("abstracte", info.getAbstracte());
//	        if (insert != 0) {
//	        	//4.29
//	        	if(!(user.getId().equals(a1))){
//	            	Jdpush.jpushAndriod1(user.getPetName(),s,a,map);
//	            	}
//	            return new ResultUtil(200, "ok", insert + "");
//	        }
//	        //添加失败
//	        return new ResultUtil(500, "no", "添加失败");
//	    }
//	 private Map<String, String> check(BindingResult result) {
//	        Map<String, String> errorMap = new HashMap<>(10);
//	        if (result.hasFieldErrors()) {
//	            List<FieldError> fieldErrors = result.getFieldErrors();
//	            for (FieldError item : fieldErrors) {
//	                errorMap.put(item.getField(), item.getDefaultMessage());
//	            }
//	            return errorMap;
//	        }
//	        return null;
//	    }
//	
//	
}
