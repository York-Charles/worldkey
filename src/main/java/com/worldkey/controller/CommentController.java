package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.check.comment.Reply;
import com.worldkey.entity.Comment;
import com.worldkey.entity.CommentApp;
import com.worldkey.entity.History;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.Users;
import com.worldkey.jdpush.Jdpush;
import com.worldkey.mapper.CommentMapper;
import com.worldkey.mapper.HistoryMapper;
import com.worldkey.service.CommentService;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import com.worldkey.worldfilter.WordFilter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HP
 */
@RestController
@RequestMapping("comment")
public class CommentController {
    @Resource
    private WordFilter wordFilter;
    @Resource
    private CommentService service;
    @Resource
    private UsersService usersService;
    @Resource
    private InformationAllService informationAllService;
    @Resource
    private CommentMapper commenMapper;
    @Resource
    private HistoryMapper hMapper;

    /**
     * 为评论添加回复接口
     * @param comment  回复实体类
     * @param result   参数校验结果
     * @param token    用户token
     * @return          ResultUtil对象
     * @throws Exception 懒
     */
    @PostMapping("addReply")
    public ResultUtil addReply(@Validated({Reply.class}) Comment comment, BindingResult result, String token) throws Exception {
        //参数规则校验
        Map<String, String> errorMap = this.check(result);
        if (errorMap != null) {
            return new ResultUtil(406, "no", errorMap);
        }
        int i = this.service.addReply(comment, token);
        Users user = usersService.findByToken(token);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("img", user.getHeadImg());
        map.put("name", user.getPetName());
        map.put("Mid", Long.toString(user.getId()));
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm"); 
        String date = df.format(day);
        map.put("date", date);
        map.put("commentId", Long.toString(comment.getCommentId()));
        map.put("fenlei", "7");
        map.put("content",comment.getInfo());
        Comment c = this.commenMapper.selectByPrimaryKey(comment.getComment());
        Users user1 = usersService.selectByPrimaryKey(Long.parseLong(comment.getUsers().getId()+""));
        String s = user1.getLoginName();//被推送人
        String s1 = c.getInfo();
        String a1 = user1.toString();
        String a = comment.getInfo();//评论内容
        
      //推送记录
      		History h=new History();
      		h.setCreateTime(new Date());
      		h.setHeadImg(user.getHeadImg());
      		h.setPetName(user.getPetName());
      		h.setUserId(user1.getId());
      		h.setToUserId(user.getId());
      		h.setUserName(user.getLoginName());
      		h.setCommentId(Integer.parseInt(c.getCommentId()+""));
      		h.setCommentInfo(comment.getInfo());
      		h.setClassify(7);
      		h.setACommentInfo(c.getInfo());
      		this.hMapper.aaa(h);
        
        
        if (i==1){
        	if(!(user.getId().equals(a1))){
            	Jdpush.jpushAndriod7(user.getPetName(),s,s1,a,map);
            	}
            return new ResultUtil(200,"ok",1);
        }else {
            throw  new Exception("添加失败");
        }
    }
    //为回复添加回复
    @PostMapping("addReply1")
    public ResultUtil addReply1(@Validated({Reply.class}) Comment comment, BindingResult result, String token) throws Exception {
        //参数规则校验
        Map<String, String> errorMap = this.check(result);
        if (errorMap != null) {
            return new ResultUtil(406, "no", errorMap);
        }
        int i = this.service.addReply1(comment, token);

        Users user = usersService.findByToken(token);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("img", user.getHeadImg());
        map.put("name", user.getPetName());
        map.put("Mid", Long.toString(user.getId()));
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm"); 
        String date = df.format(day);
        map.put("date", date);
        map.put("commentId", Long.toString(comment.getCommentId()));
        map.put("fenlei", "8");
        map.put("content",comment.getInfo());
        Comment c = this.commenMapper.selectByPrimaryKey(comment.getComment());
        Users user1 = usersService.selectByPrimaryKey(Long.parseLong(comment.getUsers().getId()+""));
        String s = user1.getLoginName();//被推送人
        String s1 = c.getInfo();
        String a1 = user1.toString();
        String a = comment.getInfo();//评论内容
        
      //推送记录
      		History h=new History();
      		h.setCreateTime(new Date());
      		h.setHeadImg(user.getHeadImg());
      		h.setPetName(user.getPetName());
      		h.setUserId(user1.getId());
      		h.setToUserId(user.getId());
      		h.setUserName(user.getLoginName());
      		h.setCommentId(Integer.parseInt(c.getCommentId()+""));
      		h.setCommentInfo(comment.getInfo());
      		h.setClassify(8);
      		h.setACommentInfo(c.getInfo());
      		this.hMapper.aaa(h);
        
        if (i==1){
        	if(!(user.getId().equals(a1))){
            	Jdpush.jpushAndriod8(user.getPetName(),s,s1,a,map);
            	}
            return new ResultUtil(200,"ok",1);
        }else {
            throw  new Exception("添加失败");
        }
    }
    //查评论回复
    @RequestMapping("getReply")
    public ResultUtil getReply(Long comment,@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo info=this.service.getReply(comment,pageNum,pageSize);
        return new ResultUtil(200,"ok",info);
    }
    //查回复的回复
    @RequestMapping("getReply1")
    public ResultUtil getReply1(Long comment,@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo info=this.service.getReply1(comment,pageNum,pageSize);
        return new ResultUtil(200,"ok",info);
    }




    /**
     * 添加评论
     */
    @PostMapping("add")
    public ResultUtil add(@Validated({com.worldkey.check.comment.Comment.class}) Comment comment, BindingResult result, String token) throws Exception {
        //参数规则校验
        Map<String, String> errorMap = this.check(result);
        if (errorMap != null) {
            return new ResultUtil(406, "no", errorMap);
        }
       
        //敏感词过滤
        String infoStr = comment.getInfo();
        boolean contains = wordFilter.isContains(infoStr);
        if (contains) {
            return new ResultUtil(406, "no", "存在非法词语,请文明用语");
        }
        //登录验证
        Users user = usersService.findByToken(token);
        //未登录
        if (token == null || user == null) {
            return new ResultUtil(406, "no", "请先登陆");
        }
        Long information = comment.getInformation();
        InformationAll info = this.informationAllService.info(information);
        //验证商品是否存在
        
        //4.29
        String s = info.getUsers().getLoginName();
        String a = comment.getInfo();//获取评论内容
        Long a1 = info.getUsers().getId();
        if (info == null) {
            return new ResultUtil(406, "no", "商品不存在");
        }
        comment.setUsers(user);
        comment.setGmtCreate(new Date());
        comment.setAuthor(info.getUsers().getId());
        Integer insert = this.service.insert(comment);
        //4.29
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("img", user.getHeadImg());
        map.put("name", user.getPetName());
        map.put("Mid", Long.toString(user.getId()));
        map.put("infoImg",info.getTitleImg());
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm"); 
        String date = df.format(day);
        map.put("date", date);
        map.put("webUrl", info.getWeburl());
        map.put("id", Long.toString(info.getId()));
        map.put("fenlei", "1");
        map.put("content",comment.getInfo());
        map.put("title", info.getTitle());
        map.put("abstracte", info.getAbstracte());
        if (insert != 0) {
        	//4.29
        	if(!(user.getId().equals(a1))){
            	Jdpush.jpushAndriod1(user.getPetName(),s,a,map);
            	}
            return new ResultUtil(200, "ok", insert + "");
        }
        //添加失败
        return new ResultUtil(500, "no", "添加失败");
    }

    @RequestMapping("comment")
    public ResultUtil selectComment(Long information,
                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<Comment> commentPageInfo = this.service.selectByInformationOrderByIdDesc(information, pageNum, pageSize);
        return new ResultUtil(200, "ok", commentPageInfo);
    }
  //按时间
    @RequestMapping("comment1")
    public ResultUtil selectComment1(Long information,
                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<Comment> commentPageInfo = this.service.selectByInformationOrderByIdDesc1(information, pageNum, pageSize);
        return new ResultUtil(200, "ok", commentPageInfo);
   }
    //按点赞量
    @RequestMapping("comment2")
    public ResultUtil selectComment2(Long information,
                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<Comment> commentPageInfo = this.service.selectByInformationOrderByIdDesc2(information, pageNum, pageSize);
        return new ResultUtil(200, "ok", commentPageInfo);
    }

    @RequestMapping("test")
    public ModelAndView test() {
        return new ModelAndView("test");
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
    
	/**5.19
     * 对应ID查找所有文章进行的操作
     */
	@RequestMapping("find")
	public @ResponseBody ResultUtil find(Long userId
			,@RequestParam(defaultValue = "1") Integer page
			,@RequestParam(defaultValue = "10") Integer pageSize) {
		List<CommentApp> shows = this.service.comment(userId);
		PageHelper.startPage(page, pageSize, true);
		PageInfo<CommentApp> pageInfo = new PageInfo<>(shows);
		return new ResultUtil(200, "ok", pageInfo);
	}
	
	//7.4评论删除
	@RequestMapping("delete")
	public ResultUtil deleteComment(Long commentId){
		int i = this.service.deleteComment(commentId);
		return new ResultUtil(200,"ok",i);	
	}
}
