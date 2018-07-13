package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.BaseShow;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Praise;
import com.worldkey.entity.UserGroup;
import com.worldkey.entity.Users;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.mapper.PraiseMapper;
import com.worldkey.mapper.UserGroupMapper;
import com.worldkey.service.BrowsingHistoryService;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@RequestMapping("/find/oneType")
	public ResultUtil findByOneType(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,Integer type,String token) {
		// 原方法selectShowByOneType由于增加三级标签，已被更改替换为如下2018.4.17
		// List<BaseShow> baseShows =
		// informationAllMapper.selectShowByOneType(type);
		//5.28注
//		PageHelper.startPage(pageNum, pageSize);
//		List<BaseShow> baseShows = informationAllMapper.selectShowAllByOneType(type);
//		List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
//		if (token != null) {
//			for (int i = 0; i < baseShows.size(); i++) {
//				for (int j = 0; j < commentNum.size(); j++) {
//					Users user = usersService.findByToken(token);
//					Praise p = new Praise();
//					p.setUsers(user);
//					p.setInformation(Integer.parseInt(baseShows.get(i).getId() + ""));
//					List<Praise> isPraise = praiseMapper.selectExist(p);
//					if (isPraise.size() == 0) {
//						baseShows.get(i).setIsPraise(0);
//					} else {
//						baseShows.get(i).setIsPraise(1);
//					}
//					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
//						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
//						break;
//					}
//				}
//			}
//		} else {
//			for (int i = 0; i < baseShows.size(); i++) {
//				for (int j = 0; j < commentNum.size(); j++) {
//					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
//						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
//						break;
//					}
//				}
//			}
//		}
		
//		PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return new ResultUtil(200, "ok", this.informationAllService.selectByOneType(type, pageNum, pageSize, token));
	}

	@RequestMapping("/find/twoType/{type}")
	public ResultUtil findByTwoType(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @PathVariable Integer type) {
		PageHelper.startPage(pageNum, pageSize);
		// 因添加三级，固将此selectShowByTwoType方法隐藏4.19
		// List<BaseShow> baseShows =
		// informationAllMapper.selectShowByTwoType(type);
		List<BaseShow> baseShows = informationAllMapper.selectShowThreeTypeAllByTwoType(type);
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
			return new ResultUtil(200, "ok", browsingHistoryService.recommendation(user.getId(), pageNum, pageSize));
		}
	}

	// 我的文章
	@RequestMapping("/find/three")
	public ResultUtil findthree(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Integer type, Long userId) {
		PageHelper.startPage(pageNum, pageSize);
		List<BaseShow> baseShows = informationAllMapper.selectype(type, userId);
		PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return new ResultUtil(200, "ok", baseShowPageInfo);
	}
	
	@RequestMapping("putElegant")
	public ResultUtil putElegant(Long id,Long userId){
		Integer putElegant = this.informationAllService.putElegant(id,userId);
		if(putElegant==2){
			return new ResultUtil(500,"error",1);
		}
		return new ResultUtil(200,"ok",putElegant);
	}
	
	@RequestMapping("putWindow")
	public ResultUtil putWindow(Long userId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", this.informationAllService.putWindow(userId));
		return new ResultUtil(200,"ok",map);
	}

	@RequestMapping("selectCompetitive")
	public ResultUtil competitive(Long id){
		return new ResultUtil(200,"ok",this.informationAllService.windowState(id));
	}
	
	@RequestMapping("selectBrandArticle")
	public ResultUtil getBrandArticle(Long userId){
		Users user = this.usersService.selectByPrimaryKey(userId);
		InformationAll info = this.informationAllService.brandArticle(userId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", info.getId());
		map.put("webUrl", info.getWeburl());
		map.put("loginName", user.getLoginName());
		map.put("author", info.getAuther());
		map.put("headImg", user.getHeadImg());
		map.put("userId", user.getId());
		map.put("title", info.getTitle());
		list.add(map);
		return new ResultUtil(200,"ok",new PageInfo<>(list));
	}
	
	@RequestMapping("brandExist")
	public ResultUtil brandExist(Long userId){
		return new ResultUtil(200,"ok",this.informationAllService.BrandExist(userId)==null?0:1);
	}
}
