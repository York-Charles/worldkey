package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Comment;
import com.worldkey.entity.History;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Praise;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.PraiseCommentNum;
import com.worldkey.entity.PraiseNum;
import com.worldkey.entity.Users;
import com.worldkey.exception.Z406Exception;
import com.worldkey.jdpush.Jdpush;
import com.worldkey.mapper.CommentMapper;
import com.worldkey.mapper.HistoryMapper;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.mapper.PraiseCommentNumMapper;
import com.worldkey.mapper.PraiseNumMapper;
import com.worldkey.service.CommentService;
import com.worldkey.service.PraiseService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import io.rong.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DB161 点赞模块
 */
@RestController
@RequestMapping("praise")
@Slf4j
public class PraiseController {
	private PraiseService praiseService;
	private UsersService usersService;
	private PraiseNumMapper praiseNumMapper;
	private PraiseCommentNumMapper praiseCommentNumMapper;
	private InformationAllMapper informationAllMapper;
	private CommentService commentService;
	private HistoryMapper hMapper;
	private CommentMapper commentMapper;
	private boolean flag = true;

	/**
	 * @param token
	 * @param informationId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("addPraise")
	public ResultUtil addPraise(String token, Integer informationId) throws Exception {

		// 验证登录
		Users uId = usersService.findByToken(token);
		if (uId == null || token == null) {
			return new ResultUtil(406, "no", "请先登录");
		}
		InformationAll informationAll = this.informationAllMapper.selectByPrimaryKey(informationId.longValue());
		// 4.28
		String s = informationAll.getUsers().getLoginName();
		Long a1 = informationAll.getUsers().getId();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("img", uId.getHeadImg());
		map.put("Mid", Long.toString(uId.getId()));
		map.put("name", uId.getPetName());
		map.put("infoImg", informationAll.getTitleImg());
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");
		String date = df.format(day);
		map.put("date", date);
		map.put("webUrl", informationAll.getWeburl());
		map.put("id", Long.toString(informationAll.getId()));
		map.put("fenlei", "0");
		map.put("title", informationAll.getTitle());
		map.put("abstracte", informationAll.getAbstracte());
		if (informationAll == null) {
			return new ResultUtil(406, "no", "请输入合法的文章ID");
		}

		int a = this.praiseService.addPraise(token, informationId);

		if (a == 1) {
			// 4.28
			if (flag == true) {
				if (!(uId.getId().equals(a1))) {
					Jdpush.jpushAndriod(uId.getPetName(), s, map);
				}
			}
				flag = false;
				Integer p =this.praiseNumMapper.p(informationId);
				return new ResultUtil(200, "Y", p);
		}
		Integer pp =this.praiseNumMapper.p(informationId);
		return new ResultUtil(200, "N", pp);
	}
	
	@RequestMapping("addCommentPraise")
	public ResultUtil addCommentPraise(String token, Integer commentId) throws Exception  {

		// 验证登录
		Users uId = usersService.findByToken(token);
		if (uId == null || token == null) {
			return new ResultUtil(406, "no", "请先登录");
		}
		Comment comment = commentService.selectByPrimaryKey(commentId.longValue());
		// 4.28
		Users master = usersService.selectByPrimaryKey(comment.getUsers().getId());
		String s = master.getLoginName();
		Long a1 = comment.getUsers().getId();
		String s1 = comment.getInfo();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("img", uId.getHeadImg());
		map.put("Mid", Long.toString(uId.getId()));
		map.put("name", uId.getPetName());
		map.put("info", comment.getInfo());
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");
		String date = df.format(day);
		map.put("date", date);
		map.put("id", Long.toString(comment.getCommentId()));
		map.put("fenlei", "6");
		//推送记录
		History h=new History();
		h.setCreateTime(new Date());
		h.setHeadImg(uId.getHeadImg());
		h.setPetName(uId.getPetName());
		h.setUserId(uId.getId());
		h.setUserName(uId.getLoginName());
		h.setCommentInfo(comment.getInfo()); //点赞的评论
		h.setClassify(6);
		if(comment.getComment()!=null){
		Comment comment1 = commentService.selectByPrimaryKey(comment.getComment());
		h.setACommentInfo(comment1.getInfo()); //点赞评论的评论
		h.setToCommentId(Integer.parseInt(comment1.getCommentId()+""));
		h.setToPetName(comment1.getUsers().getPetName());
		Integer iii = this.praiseCommentNumMapper.selectPraiseNum(Integer.parseInt(comment1.getCommentId()+""));
		if(iii!=null){
		h.setPraiseNum(iii);
		}
		Date day1 = comment1.getGmtCreate();
		SimpleDateFormat dff = new SimpleDateFormat("MM月dd日 HH:mm");
		String date1 = dff.format(day1);
		h.setCommentCreateTime(date1);	
		Integer aaa=this.commentMapper.status(uId.getId(),comment1.getCommentId());
		h.setStatus(aaa);
		}
		int a = this.praiseService.addCommentPraise(token, commentId);
		if (a == 1) {
			if (flag == true) {
				this.hMapper.aaa(h);
				if (!(uId.getId().equals(a1))) {
					Jdpush.jpushAndriod6(uId.getPetName(), s,s1, map);
				}
			}
				flag = false;
				int i = this.praiseCommentNumMapper.selectPraiseNum(commentId);
				return new ResultUtil(200, "Y", i);
		}
		int ii = this.praiseCommentNumMapper.selectPraiseNum(commentId);
		return new ResultUtil(200, "N", ii);
	}

	/**
	 * 显示点赞数
	 * 
	 * @param informationId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("showPraise")
	public ResultUtil showPraise(Integer informationId) throws Exception {
		// 获取主键ID
		Long praiseNumId = this.praiseNumMapper.selectPKByinfo(informationId);
		// 获取对象
		PraiseNum praiseNum = this.praiseNumMapper.selectByPrimaryKey(praiseNumId);
		if (praiseNum == null) {
			throw new Z406Exception("无点赞记录");
		}
		Integer pn = praiseService.showPraise(informationId);
		ResultUtil ok = new ResultUtil(200, "ok", pn);
		// log.info(GsonUtil.toJson(ok,ok.getClass()));
		return ok;
	}

	@PostMapping("statusPraise")
	public ResultUtil statusPraise(String token, Integer informationId) throws Exception {
		// 验证登录
		Users uId = usersService.findByToken(token);
		if (uId == null || token == null) {
			return new ResultUtil(406, "no", "请先登录");
		}
		InformationAll informationAll = informationAllMapper.selectByPrimaryKey(informationId.longValue());
		if (informationAll == null) {
			return new ResultUtil(406, "no", "请输入合法的文章ID");
		}
		boolean i = this.praiseService.statusPraise(token, informationId);

		if (i) {
			return new ResultUtil(200, "ok", 1);
		}
		return new ResultUtil(200, "no", 0);
	}

	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	@Autowired
	public void setPraiseService(PraiseService praiseService) {
		this.praiseService = praiseService;
	}

	@Autowired
	public void setPraiseNumMapper(PraiseNumMapper praiseNumMapper) {
		this.praiseNumMapper = praiseNumMapper;
	}

	@Autowired
	public void setInformationAllMapper(InformationAllMapper informationAllMapper) {
		this.informationAllMapper = informationAllMapper;
	}
	@Autowired
	public void setCommentService(CommentService commentService){
		this.commentService = commentService;
	}
	@Autowired
	public void setHistoryMapper(HistoryMapper hMapper){
		this.hMapper = hMapper;
	}
	@Autowired
	public void setPraiseCommentNumMapper(PraiseCommentNumMapper praiseCommentNumMapper){
		this.praiseCommentNumMapper = praiseCommentNumMapper;
	}
	@Autowired
	public void setCommentMapper(CommentMapper commentMapper){
		this.commentMapper = commentMapper;
	}
	
	/**5.19
     * 对应ID查找所有文章进行的操作
     */
	@RequestMapping("find")
	public @ResponseBody ResultUtil find(Long userId
			,@RequestParam(defaultValue = "1") Integer page
			,@RequestParam(defaultValue = "10") Integer pageSize) {
		List<PraiseApp> shows = this.praiseService.praise(userId);
		PageHelper.startPage(page, pageSize, true);
		PageInfo<PraiseApp> pageInfo = new PageInfo<>(shows);
		return new ResultUtil(200, "ok", pageInfo);
	}
}
