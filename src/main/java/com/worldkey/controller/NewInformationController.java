package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.BaseShow;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Praise;
import com.worldkey.entity.UserGroup;
import com.worldkey.entity.Users;
import com.worldkey.mapper.CommentMapper;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.mapper.PraiseMapper;
import com.worldkey.mapper.PraiseNumMapper;
import com.worldkey.mapper.UserGroupMapper;
import com.worldkey.service.BrowsingHistoryService;
import com.worldkey.service.CommentService;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.PraiseService;
import com.worldkey.service.ThreeTypeService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/information")
public class NewInformationController {
	@Resource
	private BrowsingHistoryService browsingHistoryService;
	@Resource
	private InformationAllMapper informationAllMapper;
	@Resource
	private UsersService usersService;
	@Resource
	private InformationAllService informationAllService;
	@Resource
	private UserGroupMapper ugMapper;
	@Resource
	private PraiseMapper praiseMapper;
	@Resource
	private ThreeTypeService threeTypeService;
	@Resource
	private UsersService uService;
	@Resource
	private CommentMapper commentMapper;
	@Resource
	private CommentService commentService;
	@Resource
	private PraiseService pService;

	@RequestMapping("/find/oneType")
	public ResultUtil findByOneType(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Integer type, String token) {
		// 原方法selectShowByOneType由于增加三级标签，已被更改替换为如下2018.4.17
		// List<BaseShow> baseShows =
		// informationAllMapper.selectShowByOneType(type);
		// 5.28注
		// PageHelper.startPage(pageNum, pageSize);
		// List<BaseShow> baseShows =
		// informationAllMapper.selectShowAllByOneType(type);
		// List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
		// if (token != null) {
		// for (int i = 0; i < baseShows.size(); i++) {
		// for (int j = 0; j < commentNum.size(); j++) {
		// Users user = usersService.findByToken(token);
		// Praise p = new Praise();
		// p.setUsers(user);
		// p.setInformation(Integer.parseInt(baseShows.get(i).getId() + ""));
		// List<Praise> isPraise = praiseMapper.selectExist(p);
		// if (isPraise.size() == 0) {
		// baseShows.get(i).setIsPraise(0);
		// } else {
		// baseShows.get(i).setIsPraise(1);
		// }
		// if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
		// baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
		// break;
		// }
		// }
		// }
		// } else {
		// for (int i = 0; i < baseShows.size(); i++) {
		// for (int j = 0; j < commentNum.size(); j++) {
		// if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
		// baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
		// break;
		// }
		// }
		// }
		// }

		// PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return new ResultUtil(200, "ok", this.informationAllService.selectByOneType(type, pageNum, pageSize, token));
	}

	@RequestMapping("/find/twoType")
	public ResultUtil findByTwoType(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Integer type, String token) {
		PageHelper.startPage(pageNum, pageSize);
		// 因添加三级，固将此selectShowByTwoType方法隐藏4.19
		// List<BaseShow> baseShows =
		// informationAllMapper.selectShowByTwoType(type);
		List<BaseShow> baseShows = informationAllMapper.selectShowThreeTypeAllByTwoType(type);
		if (token != null) {
			Users user = usersService.findByToken(token);
			for (BaseShow l : baseShows) {

				Integer i = this.praiseMapper.i(user.getId(), l.getId());
				l.setStatus(i);
			}
		}
		List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
		if (token != null) {
			for (int i = 0; i < baseShows.size(); i++) {
				Users user = usersService.findByToken(token);
				Praise p = new Praise();
				p.setUsers(user);
				p.setInformation(Integer.parseInt(baseShows.get(i).getId() + ""));
				List<Praise> isPraise = praiseMapper.selectExist(p);
				if (isPraise.size() == 0) {
					baseShows.get(i).setIsPraise(0);
				} else {
					baseShows.get(i).setIsPraise(1);
				}
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < baseShows.size(); i++) {
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		}
		PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return new ResultUtil(200, "ok", baseShowPageInfo);
	}

	/*
	 * ------------------------以下三级页面展示2018.4.10写----------------------------
	 */
	// 通过三级id获取三级详情页
	@RequestMapping("/find/threeType/{type}")
	public ResultUtil findByThreeType(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @PathVariable Integer type) {
		PageHelper.startPage(pageNum, pageSize);
		List<BaseShow> baseShows = informationAllMapper.selectShowByThreeType(type);
		PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return new ResultUtil(200, "ok", baseShowPageInfo);
	}

	@RequestMapping("/find/xiaozuType")
	public ResultUtil xiaozutype(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "groupId") Integer type, String token) {
		return new ResultUtil(200, "ok", this.informationAllService.getShuoComponent(type, pageNum, pageSize, token));
	}

	@RequestMapping("/find/shequ")
	public ResultUtil shequ(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestHeader("host") String host) {
		return new ResultUtil(200, "ok", this.informationAllService.getshequ(pageNum, pageSize));
	}
	/*
	 * 此接口和上面("/find/twoType/{type}")重复，隐藏 //通过二级id获取三级的详情列表
	 * 
	 * @RequestMapping("/find/threeTypeAll/{type}") public ResultUtil
	 * findthreeTypeAllByTwoType(@RequestParam(value = "page", defaultValue =
	 * "1") Integer pageNum,
	 * 
	 * @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
	 * 
	 * @PathVariable Integer type){ PageHelper.startPage(pageNum,pageSize);
	 * List<BaseShow> baseShows =
	 * informationAllMapper.selectShowThreeTypeAllByTwoType(type);
	 * PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows); return
	 * new ResultUtil(200,"ok",baseShowPageInfo); }
	 */
	/*
	 * -------------------------------------------------------------------------
	 * ------------
	 */

	@RequestMapping("recommendation")
	@ResponseBody
	public ResultUtil recommendation(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, String token) throws Exception {
		Users user = null;
		if (token != null) {
			user = usersService.findByToken(token);
		}
		if (token == null || user == null) {
			PageHelper.startPage(pageNum, pageSize);
			List<BaseShow> list = this.informationAllService.findOrderByPointNumber1();
			PageInfo<BaseShow> pageInfo = new PageInfo<>(list);
			return new ResultUtil(200, "ok", pageInfo);
		} else {
			PageInfo<BaseShow> pageInfo = browsingHistoryService.recommendation(user.getId(), pageNum, pageSize);
			return new ResultUtil(200, "ok", pageInfo);
		}
	}

	// 我的文章
	@RequestMapping("/find/three")
	public ResultUtil findthree(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Integer type, Long userId) {
		PageHelper.startPage(pageNum, pageSize);
		List<BaseShow> baseShows = informationAllMapper.selectype(type, userId);
		if (userId != null) {
			for (BaseShow l : baseShows) {
				Integer i = this.praiseMapper.i(userId, l.getId());
				l.setStatus(i);
			}
		}
		List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
		if (userId != null) {
			for (int i = 0; i < baseShows.size(); i++) {
				Users user = usersService.selectByPrimaryKey(userId);
				Praise p = new Praise();
				p.setUsers(user);
				p.setInformation(Integer.parseInt(baseShows.get(i).getId() + ""));
				List<Praise> isPraise = praiseMapper.selectExist(p);
				if (isPraise.size() == 0) {
					baseShows.get(i).setIsPraise(0);
				} else {
					baseShows.get(i).setIsPraise(1);
				}
				baseShows.get(i).setImgNum(this.getImgNum(baseShows.get(i).getTitleImg()));
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < baseShows.size(); i++) {
				baseShows.get(i).setImgNum(this.getImgNum(baseShows.get(i).getTitleImg()));
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		}
		PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return new ResultUtil(200, "ok", baseShowPageInfo);
	}

	private Integer getImgNum(String s) {
		String[] arr = s.split(",");
		return arr.length;
	}

	@RequestMapping("putElegant")
	public ResultUtil putElegant(Long id, Long userId) {
		Integer putElegant = this.informationAllService.putElegant(id, userId);
		if (putElegant == 2) {
			return new ResultUtil(500, "error", 1);
		}
		return new ResultUtil(200, "ok", putElegant);
	}

	@RequestMapping("putWindow")
	public ResultUtil putWindow(Long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", this.informationAllService.putWindow(userId));
		return new ResultUtil(200, "ok", map);
	}

	@RequestMapping("selectCompetitive")
	public ResultUtil competitive(Long id) {
		return new ResultUtil(200, "ok", this.informationAllService.windowState(id));
	}

	@RequestMapping("selectBrandArticle")
	public ResultUtil getBrandArticle(Long userId) {
		Users user = this.usersService.selectByPrimaryKey(userId);
		InformationAll info = this.informationAllService.brandArticle(userId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", info.getId());
		map.put("webUrl", info.getWeburl());
		map.put("loginName", user.getLoginName());
		map.put("author", info.getAuther());
		map.put("headImg", user.getHeadImg());
		map.put("userId", user.getId());
		map.put("title", info.getTitle());
		list.add(map);
		return new ResultUtil(200, "ok", new PageInfo<>(list));
	}

	@RequestMapping("brandExist")
	public ResultUtil brandExist(Long userId) {
		return new ResultUtil(200, "ok", this.informationAllService.BrandExist(userId) == null ? 0 : 1);
	}

	@RequestMapping("findXiaozu")
	@ResponseBody
	public ResultUtil Xiaozu(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, String difference, String name,
			String token) throws Exception {
		if (difference.equals("小组")) {
			return new ResultUtil(200, "ok", this.threeTypeService.getXiaozu(pageNum, pageSize, name, token));
		} else if (difference.equals("活动")) {
			return new ResultUtil(200, "ok", "暂无");
		}
		return new ResultUtil(200, "ok", "111");

	}

	@RequestMapping("deleteInformation")
	@ResponseBody
	public ResultUtil deleteInformation(Long id) {
		List<Long> a = this.commentMapper.selectInformation(id);
		if (a != null) {
			for (Long commentId : a) {
				this.commentService.deleteComment(commentId);
			}
		}
		this.commentService.deleteCommentHistory(id);
		this.pService.deleteInformaiton(Integer.parseInt(id + ""));
		this.pService.deleteInformaitonNum(Integer.parseInt(id + ""));
		this.informationAllService.deleteInformation(id);
		return new ResultUtil(200, "ok", "删除成功");

	}

	// 文章编辑显示接口
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("compile")
	@ResponseBody
	public ResultUtil compile(Long id) {
		String title = this.informationAllService.findTitle(id);
		String info = this.informationAllService.findInfo(id);
		// String title1=title.replace("<br>", "");
		// title1=title1.replace("<div>", "");
		// title1=title1.replace("</div>", "");
		// title1=title1.replace("<p>", "");
		// title1=title1.replace("</p>", "");
		// String info1=info.replace("<br>", "");
		// info1=info1.replace("<div>", "");
		// info1=info1.replace("</div>", "");
		// info1=info1.replace("<p>", "");
		// info1=info1.replace("</p>", "");

		Pattern p_enter = Pattern.compile("<br>", Pattern.CASE_INSENSITIVE);// 下面三行是将HTML中的换行符<br/>替换成"\n"
		Matcher m_enter = p_enter.matcher(info);
		info = m_enter.replaceAll("\n");

		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		java.util.regex.Pattern p_special;
		java.util.regex.Matcher m_special;

		String regRxP = "<p.*?>(.*?)</p>";
		p_style = Pattern.compile(regRxP, Pattern.CASE_INSENSITIVE);
		m_style = p_style.matcher(info);
		info = m_style.replaceAll("");
		m_style = p_style.matcher(title);
		title = m_style.replaceAll("");
		String regEx_special = "\\&[a-zA-Z]{1,10};";
		p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
		m_special = p_special.matcher(info);
		info = m_special.replaceAll(""); // 过滤特殊标签
		m_special = p_special.matcher(title);
		title = m_special.replaceAll(""); // 过滤特殊标签
		String regEx = "(?!<(img).*?>)<.*?>";
		p_html = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		m_html = p_html.matcher(info);
		info = m_html.replaceAll("");
		m_html = p_html.matcher(title);
		title = m_html.replaceAll("");
		info = info.replace("\">", "\"/>");

		// String title1 = "";
		// String info1 = "";
		// java.util.regex.Pattern p_script;
		// java.util.regex.Matcher m_script;
		// java.util.regex.Pattern p_style;
		// java.util.regex.Matcher m_style;
		// java.util.regex.Pattern p_html;
		// java.util.regex.Matcher m_html;
		// java.util.regex.Pattern p_special;
		// java.util.regex.Matcher m_special;
		// try {
		// //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
		// String regEx_script =
		// "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
		// //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
		// String regEx_style =
		// "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
		// // 定义HTML标签的正则表达式
		// String regEx_html = "<[^>]+>";
		// // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		// String regEx_special = "\\&[a-zA-Z]{1,10};";
		//

		//
		//
		//
		// p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		// m_script = p_script.matcher(info);
		// info = m_script.replaceAll(""); // 过滤script标签
		// p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		// m_style = p_style.matcher(info);
		// info = m_style.replaceAll(""); // 过滤style标签
		// p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		// m_html = p_html.matcher(info);
		// info = m_html.replaceAll(""); // 过滤html标签
		// p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
		// m_special = p_special.matcher(info);
		// info = m_special.replaceAll(""); // 过滤特殊标签
		// info1 = info;
		//
		// p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		// m_script = p_script.matcher(title);
		// title = m_script.replaceAll(""); // 过滤script标签
		// p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		// m_style = p_style.matcher(title);
		// title = m_style.replaceAll(""); // 过滤style标签
		// p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		// m_html = p_html.matcher(title);
		// title = m_html.replaceAll(""); // 过滤html标签
		// p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
		// m_special = p_special.matcher(title);
		// title = m_special.replaceAll(""); // 过滤特殊标签
		// title1=title;
		//
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//

		// info1=info1.replace("\">", "\"/>");
		// StringBuilder sb = new StringBuilder();
		// sb.append("<p><h3>").append(title).append("</h3></p>").append("<br>").append(info);
		// String result = sb.toString();
		ArrayList list = new ArrayList();
		list.add(title);
		list.add(info);
		return new ResultUtil(200, "ok", list);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("compileto")
	@ResponseBody
	public ResultUtil compileto(Long id, String title, String titleImg, String info1,
			@RequestHeader("host") String host) {
		InformationAll informationa = informationAllMapper.selectByPrimaryKey1(id);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dir = format.format(new Date());
		String info = info1.replace("/storage/emulated/0/Android/data/life.dubai.com.mylife/files/",
				"http://" + host + "/image/" + dir + "/");
		// String info = info2.replace("<img ", "<br><img ");
		// info = info2.replace("\"/>", "\"/><br>");
		String titleImgs = titleImg.replace("/storage/emulated/0/Android/data/life.dubai.com.mylife/files/",
				"http://" + host + "/image/" + dir + "/");
		if (title.equals(informationa.getTitle())) {
			if (titleImgs.equals(informationa.getTitleImg())) {
				if (info.equals(informationa.getInfo())) {
					List list = new ArrayList();
					list.add(informationa.getTitle());
//					if (informationa.getTitleImg().equals(null) || informationa.getTitleImg().contains(",") == true) {
//						String Img = informationa.getTitleImg().substring(0, informationa.getTitleImg().indexOf(","));
//						list.add(Img);
//					} else {
						list.add(informationa.getTitleImg());
//					}
					return new ResultUtil(200, "无改动", list);
				}
			}
		}
		Integer i = this.informationAllService.compileto(id, title, titleImgs, info);
		if (i != 0) {
			List list1 = new ArrayList();
			list1.add(title);
//			if (titleImgs.equals(null) || titleImgs.contains(",") == true) {
//				String Img = titleImgs.substring(0, titleImgs.indexOf(","));
//				list1.add(Img);
//			} else {
				list1.add(titleImgs);
//			}
			list1.add(new Date().getTime());
			return new ResultUtil(200, "编辑成功", list1);
		} else {
			return new ResultUtil(500, "编辑失败", "error");
		}

	}
	// @SuppressWarnings({ "unchecked", "rawtypes"})
	// @RequestMapping("compileto")
	// @ResponseBody
	// public ResultUtil compileto(Long id,String title,String titleImg,String
	// info1,@RequestHeader("host") String host){
	// InformationAll informationa =
	// informationAllMapper.selectByPrimaryKey1(id);
	// SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	// String dir = format.format(new Date());
	// String info =
	// info1.replace("/storage/emulated/0/Android/data/life.dubai.com.mylife/files/",
	// "http://"+host+"/image-test/"+dir+"/");
	//// String info = info2.replace("<img ", "<br><img ");
	//// info = info2.replace("\"/>", "\"/><br>");
	// String titleImgs =
	// titleImg.replace("/storage/emulated/0/Android/data/life.dubai.com.mylife/files/",
	// "http://"+host+"/image-test/"+dir+"/");
	// if(title.equals(informationa.getTitle())){
	// if(titleImgs.equals(informationa.getTitleImg())){
	// if(info.equals(informationa.getInfo())){
	// List list = new ArrayList();
	// list.add(informationa.getTitle());
	// if(informationa.getTitleImg().equals(null)||informationa.getTitleImg().contains(",")==true){
	// String Img = informationa.getTitleImg().substring(0,
	// informationa.getTitleImg().indexOf(","));
	// list.add(Img);
	// }else{
	// list.add(informationa.getTitleImg());
	// }
	// return new ResultUtil(200,"无改动",list);
	// }
	// }
	// }
	// Integer i=this.informationAllService.compileto(id, title,titleImgs,
	// info);
	// if(i!=0){
	// List list1 = new ArrayList();
	// list1.add(title);
	// if(titleImgs.equals(null)||titleImgs.contains(",")==true){
	// String Img = titleImgs.substring(0, titleImgs.indexOf(","));
	// list1.add(Img);
	// }else{
	// list1.add(titleImgs);
	// }
	// list1.add(new Date().getTime());
	// return new ResultUtil(200,"编辑成功",list1);
	// }else{
	// return new ResultUtil(500,"编辑失败","error");
	// }
	//
	// }

	// //我的创作
	// @SuppressWarnings("unused")
	// @RequestMapping("myCreate")
	// @ResponseBody
	// public ResultUtil myCreate(@RequestParam(value = "page", defaultValue =
	// "1") Integer pageNum,
	// @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
	// String token) throws Exception {
	// Users user = usersService.findByToken(token);
	// PageHelper.startPage(pageNum, pageSize);
	// List<BaseShow> list = this.informationAllService.myCreate(user.getId());
	// if(token!=null){
	// for(BaseShow l:list){
	// Integer i = this.praiseMapper.i(user.getId(),l.getId());
	// l.setStatus(i);
	// }
	// }else {
	// return new ResultUtil(500,"请先登录","error");
	// }
	// List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
	// if (token != null) {
	// for (int i = 0; i < list.size(); i++) {
	// Praise p = new Praise();
	// p.setUsers(user);
	// p.setInformation(Integer.parseInt(list.get(i).getId() + ""));
	// List<Praise> isPraise = praiseMapper.selectExist(p);
	// if (isPraise.size() == 0) {
	// list.get(i).setIsPraise(0);
	// } else {
	// list.get(i).setIsPraise(1);
	// }
	// list.get(i).setImgNum(this.getImgNum(list.get(i).getTitleImg()));
	// for (int j = 0; j < commentNum.size(); j++) {
	// if (list.get(i).getId().equals((commentNum.get(j).getId()))) {
	// list.get(i).setCommentNum(commentNum.get(j).getCommentNum());
	// break;
	// }
	// }
	// }
	// } else {
	// for (int i = 0; i < list.size(); i++) {
	// list.get(i).setImgNum(this.getImgNum(list.get(i).getTitleImg()));
	// for (int j = 0; j < commentNum.size(); j++) {
	// if (list.get(i).getId().equals((commentNum.get(j).getId()))) {
	// list.get(i).setCommentNum(commentNum.get(j).getCommentNum());
	// break;
	// }
	// }
	// }
	// }
	// PageInfo<BaseShow> pageInfo = new PageInfo<>(list);
	// return new ResultUtil(200, "ok", pageInfo);
	//
	// }
	// 我的创作 1.6日参数改为userId
	@SuppressWarnings("unused")
	@RequestMapping("myCreate")
	@ResponseBody
	public ResultUtil myCreate(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Integer userId) throws Exception {
		// Users user = usersService.findByToken(token);
		Users user = usersService.selectByPrimaryKey(Long.parseLong(userId + ""));
		PageHelper.startPage(pageNum, pageSize);
		List<BaseShow> list = this.informationAllService.myCreate(Long.parseLong(userId + ""));
		if (userId != null) {
			for (BaseShow l : list) {
				Integer i = this.praiseMapper.i(Long.parseLong(userId + ""), l.getId());
				l.setStatus(i);
			}
		} else {
			return new ResultUtil(500, "请先登录", "error");
		}
		List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
		if (userId != null) {
			for (int i = 0; i < list.size(); i++) {
				Praise p = new Praise();
				p.setUsers(user);
				p.setInformation(Integer.parseInt(list.get(i).getId() + ""));
				List<Praise> isPraise = praiseMapper.selectExist(p);
				if (isPraise.size() == 0) {
					list.get(i).setIsPraise(0);
				} else {
					list.get(i).setIsPraise(1);
				}
				list.get(i).setImgNum(this.getImgNum(list.get(i).getTitleImg()));
				for (int j = 0; j < commentNum.size(); j++) {
					if (list.get(i).getId().equals((commentNum.get(j).getId()))) {
						list.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setImgNum(this.getImgNum(list.get(i).getTitleImg()));
				for (int j = 0; j < commentNum.size(); j++) {
					if (list.get(i).getId().equals((commentNum.get(j).getId()))) {
						list.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		}
		PageInfo<BaseShow> pageInfo = new PageInfo<>(list);
		return new ResultUtil(200, "ok", pageInfo);

	}

	// 放弃编辑时候删除图片
	@RequestMapping("delete")
	@ResponseBody
	public void deleteImg(String img) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dir = format.format(new Date());
		img = img.replace("/storage/emulated/0/Android/data/life.dubai.com.mylife/files/", "");
		String[] as = img.split(",");
		for (int i = 0; i < as.length; i++) {
			// String s = "D:\\mnt\\image\\"+ dir+"\\"+as[i];//文件的绝对路径
			String s = "/mnt/image" + File.separator + dir + File.separator + as[i];// 文件的绝对路径
			File file1 = new File(s);
			if (file1.exists()) {
				boolean d = file1.delete();

				if (d) {
					System.out.print("删除成功！");
				} else {
					System.out.print("删除失败！");
				}
			}
		}
		// String s = "D:\\mnt\\image\\"+ dir+"\\"+img;//文件的绝对路径
		//// String s = "/mnt/image" + File.separator +
		// dir+File.separator+img+fileAfter;//文件的绝对路径
		// File file1 = new File(s);
		// if(file1.exists()){
		// boolean d = file1.delete();
		//
		//
		//
		//
		// if(d){
		// System.out.print("删除成功！");
		// }else{
		// System.out.print("删除失败！");
		// }
		// }
		return;
	}

}
