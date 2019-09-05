package com.worldkey.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Assess;
import com.worldkey.entity.CommentApp;
import com.worldkey.entity.Users;
import com.worldkey.jdpush.Jdpush;
import com.worldkey.mapper.AssessMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.AssessService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import com.worldkey.util.TimeUtils;
import com.worldkey.worldfilter.WordFilter;


@RestController
@RequestMapping("assess")
public class AssessController {
	 @Resource
     private WordFilter wordFilter;
	 @Resource
	 private UsersService usersService;
	 @Resource
	 private AssessService assessService;
	 @Resource
	 private UsersMapper usersMapper;
	 @Resource
	 private AssessMapper assessMapper;
	 
	 @RequestMapping("add")
	 public ResultUtil add(Assess assess, BindingResult result, String token) throws Exception {
		//参数规则校验
	        Map<String, String> errorMap = this.check(result);
	        if (errorMap != null) {
	            return new ResultUtil(406, "no", errorMap);
	        }
	       
	        //敏感词过滤
	        String infoStr = assess.getConTent();
//	        boolean contains = wordFilter.isContains(infoStr);
//	        if (contains) {
//	            return new ResultUtil(406, "no", "存在非法词语,请文明用语");
//	        }
	        //登录验证
	        Users user = usersService.findByToken(token);
	        //未登录
	        if (token == null || user == null) {
	            return new ResultUtil(406, "no", "请先登陆");
	        }
	        Users user1 = usersMapper.user(assess.getUserId());//被评论人对象
	        Integer a1 = assess.getUserId();//验证是否自己给自己评价
	        String s = user1.getLoginName();//被评价人的登录账号
	        String a = assess.getConTent();//评价内容
	        HashMap<String, String> map = new HashMap<String, String>();
	       //评论人的信息
	        map.put("img", user.getHeadImg());
	        map.put("name", user.getPetName());
	        map.put("Mid", Long.toString(user.getId()));
	        Date day=new Date();
	        SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm"); 
	        String date = df.format(day);
	        map.put("date", date);
	        map.put("fenlei", "5");
	        map.put("content", assess.getConTent());
	        //重复评价
	        List<Assess> show = this.assessMapper.findByAll(assess.getUserId());
	        int aa = Integer.parseInt((assess.getUserId()+""));
	        int bb = Integer.parseInt((user.getId()+""));
	        for(Assess q:show){
	        	if(q.getAssessId().equals(bb)){
	        		String conTent = assess.getConTent();
	        		this.assessMapper.update(conTent,bb,aa);
	        		return new ResultUtil(200, "ok", "添加成功");
	        	}
	        }
	        //评价
	        assess.setAssessId(Integer.parseInt(user.getId()+""));//添加存入assessID
	        Integer insert = this.assessService.insert(assess);
	        if (insert != 0) {
	        	
	        	//推送
	        	if(!(user.getId().equals(a1))){
	            	Jdpush.jpushAndriod1(user.getPetName(),s,a,map);
	            	}
	            return new ResultUtil(200, "ok", insert + "");
	        }
			return new ResultUtil (500, "no", "添加失败");
	 }
	 
	 private Map<String, String> check(BindingResult result) {
	        Map<String, String> errorMap = new HashMap<>(10);
	        if (result.hasFieldErrors()) {
	            List<FieldError> fieldErrors = result.getFieldErrors();
	            for (FieldError item : fieldErrors) {
	                errorMap.put(item.getField(), item.getDefaultMessage());
	            }
	            return errorMap;
	        }
	        return null;
	    }
	        

	 @RequestMapping("finds")
		public @ResponseBody ResultUtil finds(Integer userId
				,@RequestParam(defaultValue = "1") Integer page
				,@RequestParam(defaultValue = "10") Integer pageSize) {
		 	PageInfo<Assess> shows = this.assessService.findAssess(page,pageSize,userId);
			return new ResultUtil(200, "ok", shows);
		}
	 
	 @RequestMapping("find")
		public @ResponseBody ResultUtil find(Integer userId) {
			List<Assess> shows = this.assessService.findAssesss(userId);
			for(Assess a:shows){
				a.setTime(TimeUtils.getTime(a.getCreateTime()));
			}
			return new ResultUtil(200, "ok", shows);
		}


}
