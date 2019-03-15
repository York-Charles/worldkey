package com.worldkey.controller;

import com.worldkey.entity.OneType;
import com.worldkey.entity.ThreeType;
import com.worldkey.entity.TwoType;
import com.worldkey.entity.UserGroup;
import com.worldkey.entity.Users;
import com.worldkey.mapper.UserGroupMapper;
import com.worldkey.service.OneTypeService;
import com.worldkey.service.ThreeTypeService;
import com.worldkey.service.TwoTypeService;
import com.worldkey.service.UserGroupService;
import com.worldkey.service.UsersService;
import com.worldkey.util.FileUploadUtilAsync;
import com.worldkey.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("type")
@Slf4j
@ResponseBody
public class TypeController {
	@Resource
	private OneTypeService oneTypeService;
	@Resource
	private TwoTypeService twoTypeService;
	@Resource
	private ThreeTypeService threeTypeService;
	@Resource
	private UsersService uService;
	@Resource
	private UserGroupService UGservice;
	@Resource
	private UserGroupMapper ugMapper;


	/**
	 * 替换二级分类的ID
	 * 
	 * @param id
	 * @param replace
	 * @return
	 */
	@RequestMapping("replace")
	@RequiresRoles("type")
	@ResponseBody
	public Integer replaceTwoType(Integer id, Integer replace) {
		log.debug("replaceTwoType:" + id + "<->" + replace);
		return this.twoTypeService.replace(id, replace);
	}

	/**
	 * 添加二级分类
	 */
	@RequiresPermissions("type:two/add")
	@RequestMapping("two/add")
	public String twoAdd(@Validated TwoType two) {
		this.twoTypeService.add(two);
		return "redirect:/type/two/list/" + two.getOneType();
	}

	/**
	 * 删除二级分类
	 * 
	 * @param id
	 *            二级分类的ID
	 */
	@RequiresPermissions("type:two/del")
	@RequestMapping("two/del/{id}")
	@ResponseBody
	public ResultUtil twoDel(@PathVariable Integer id) {
		return new ResultUtil(200, "ok", this.twoTypeService.del(id));
	}

	/**
	 * 更新二级分类
	 */
	@RequiresPermissions("type:two/update")
	@RequestMapping("two/update")
	@ResponseBody
	public ResultUtil twoUpdate(@Validated TwoType two) {
		return new ResultUtil(200, "ok", this.twoTypeService.update(two));
	}

	/**
	 * 添加一级分类
	 */
	@RequiresPermissions("type:one/add")
	@RequestMapping("one/add")
	public String oneAdd(@Validated OneType one) {
		this.oneTypeService.add(one);
		return "redirect:/type/one";
	}

	/**
	 * 更新一级分类
	 */
	@RequiresPermissions("type:one/update")
	@RequestMapping(value = "one/update")
	@ResponseBody
	public ResultUtil oneUpdate(@Validated OneType one) {
		this.oneTypeService.update(one);
		return new ResultUtil(200, "ok", 1);
	}

	/**
	 * 删除一级分类
	 * 
	 * @param id
	 *            一级分类的ID
	 */
	@RequiresPermissions("type:one/del")
	@RequestMapping("one/del/{id}")
	@ResponseBody
	public ResultUtil oneDel(@PathVariable Integer id) {
		Integer a = this.oneTypeService.del(id);
		if (a == -1) {
			return new ResultUtil(200, "no", "该分类下存在二级分类，请先处理二级分类");
		}
		return new ResultUtil(200, "ok", a);

	}

	/**
	 * 浏览器请求方式返回
	 * 
	 * @return 一级分类集合的展示页
	 */
	@RequestMapping(value = "one", produces = "text/html")
	public ModelAndView findOne1() {
		ModelAndView model = new ModelAndView("informationall/type/onetype/list");
		model.addObject("list", this.oneTypeService.findAll());
		return model;
	}

	/**
	 * ajax方式请求返回
	 * 
	 * @return 一级分类的集合
	 */
	@RequestMapping("one")
	@ResponseBody
	public ResultUtil findOne() {
		return new ResultUtil(200, "ok", this.oneTypeService.findAll());
	}

	/**
	 * 获取二级分类集合
	 * 
	 * @param one
	 *            二级分类对应的一级分类
	 */
	@RequestMapping(value = "two/list/{one}", produces = "text/html")
	@ResponseBody
	public ModelAndView findTwoByOne(@PathVariable Integer one) {
		ModelAndView model = new ModelAndView("informationall/type/twotype/list");
		model.addObject("list", this.twoTypeService.findByOne(one));
		model.addObject("oneType", one);
		return model;
	}

	/**
	 * 获取二级分类集合
	 * 
	 * @param one
	 *            二级分类对应的一级分类
	 */
	@RequestMapping("two/{one}")
	@ResponseBody
	public ResultUtil findTwoByOne1(@PathVariable Integer one) {
		return new ResultUtil(200, "ok", this.twoTypeService.findByOne(one));
	}

	/**
	 * -------------以下2018年4月8日更新，添加了三级标签 暂未开放--------------
	 */

	/**
	 * 替换三级分类的ID 暂时关闭
	 */
	@RequestMapping("replace3")
	@RequiresRoles("type")
	@ResponseBody
	public Integer replaceThreeType(Integer id, Integer replace) {

		return this.threeTypeService.replace(id, replace);
	}

	/**
	 * 添加三级分类
	 */
	// @RequiresPermissions("type:three/add")
	@RequestMapping("three/add")
	public String threeAdd(@Validated ThreeType three) {
		this.threeTypeService.add(three);
		return "redirect:/type/three/list/" + three.getTwoType();
	}

	/**
	 * 删除三级分类
	 * 
	 * @param id
	 *            三级分类的ID
	 */
	// @RequiresPermissions("type:three/del")
	@RequestMapping("three/del/{id}")
	@ResponseBody
	public ResultUtil threeDel(@PathVariable Integer id) {
		return new ResultUtil(200, "ok", this.threeTypeService.del(id));
	}

	/**
	 * 更新三级分类
	 */
	// @RequiresPermissions("type:three/update")
	@RequestMapping("three/update")
	@ResponseBody
	public ResultUtil threeUpdate(@Validated ThreeType three) {
		return new ResultUtil(200, "ok", this.threeTypeService.update(three));
	}

	/**
	 * 获取三级分类集合
	 * 
	 * @param two
	 *            三级分类对应的二级分类
	 */
	@RequestMapping(value = "three/list/{two}", produces = "text/html")
	@ResponseBody
	public ModelAndView findThreeByTwo(@PathVariable Integer two) {
		ModelAndView model = new ModelAndView("informationall/type/threetype/list");

		model.addObject("list", this.threeTypeService.findByTwo(two));
		model.addObject("twoType", two);
		return model;
	}

	/**
	 * 获取三级分类集合
	 * 
	 * @param two
	 *            三级分类对应的二级分类
	 */
	@RequestMapping("three/{two}")
	@ResponseBody
	public ResultUtil findThreeByTwo1(@PathVariable Integer two) {
		return new ResultUtil(200, "ok", this.threeTypeService.findByTwo(two));
	}

	// 审核社区
	@RequestMapping("checked")
	@ResponseBody
	public Integer checked(Integer checked, Integer id) {
		return this.threeTypeService.checked(checked, id);
	}

	// 5.11 小组操作
	@ResponseBody
	@RequestMapping(value = "group/add/{token}", method = RequestMethod.POST)
	public ResultUtil addGroup(@NotNull @PathVariable String token, HttpServletRequest request) throws IOException {
		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		MultipartFile headImgFile = params.getFile("headImg");
		MultipartFile bgImgFile = params.getFile("bgImg");
		String typeName = params.getParameter("typeName");
		String content = params.getParameter("content");
		Users user = this.uService.findByToken(token);
		ThreeType group = new ThreeType();
		String[] allowedType = { "image/bmp", "image/gif", "image/jpeg", "image/png", "image/jpg" };
		if (headImgFile != null) {
			log.debug(headImgFile.isEmpty() + "");
			boolean allowed = Arrays.asList(allowedType).contains(headImgFile.getContentType());
			if (!allowed) {
				return new ResultUtil(500, "error", "不支持的文件类型!");
			}
			String headImgrRealPath = new FileUploadUtilAsync().getFileName(request.getHeader("host"), headImgFile);
			group.setHeadImg(headImgrRealPath);
		}
		if (bgImgFile != null) {
			log.debug(bgImgFile.isEmpty() + "");
			boolean allowed1 = Arrays.asList(allowedType).contains(bgImgFile.getContentType());
			if (!allowed1) {
				return new ResultUtil(500, "error", "不支持的文件类型!");
			}
			String bgImgrRealPath = new FileUploadUtilAsync().getFileName(request.getHeader("host"), bgImgFile);
			group.setBgImg(bgImgrRealPath);
		}
		if (isExistGroup(typeName)) {
			return new ResultUtil(500, "error", "小组名已存在");
		} else {
			group.setTypeName(typeName);
		}
		group.setContent(content);
		group.setUserId(user.getId());
		this.threeTypeService.addGroup(group, user.getId());
		return new ResultUtil(200, "ok", typeName);
	}

	@RequestMapping(value="group/updateDetail/{token}", method = RequestMethod.POST)
	public ResultUtil updateGroup(@PathVariable @NotNull String token, Integer groupId, HttpServletRequest request,String content) throws IOException{
		Users user = this.uService.findByToken(token);
		Long userId = this.threeTypeService.selectLeaderId(groupId);
		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		MultipartFile headImgFile = params.getFile("headImg");
		MultipartFile bgImgFile = params.getFile("bgImg");
		String[] allowedType = { "image/bmp", "image/gif", "image/jpeg", "image/png", "image/jpg" };
		ThreeType groupH = new ThreeType();
		ThreeType groupB = new ThreeType();
		ThreeType groupC = new ThreeType();
		if(userId.equals(user.getId())){
			if (headImgFile != null) {
				log.debug(headImgFile.isEmpty() + "");
				boolean allowed = Arrays.asList(allowedType).contains(headImgFile.getContentType());
				if (!allowed) {
					return new ResultUtil(500, "error", "不支持的文件类型!");
				}
				String headImgrRealPath = new FileUploadUtilAsync().getFileName(request.getHeader("host"), headImgFile);
				groupH.setHeadImg(headImgrRealPath);
				groupH.setId(groupId);
				this.threeTypeService.updateHeadImg(groupH);
			}
			if (bgImgFile != null) {
				boolean allowed1 = Arrays.asList(allowedType).contains(bgImgFile.getContentType());
				if (!allowed1) {
					return new ResultUtil(500, "error", "不支持的文件类型!");
				}
				String bgImgrRealPath = new FileUploadUtilAsync().getFileName(request.getHeader("host"), bgImgFile);
				groupB.setId(groupId);
				groupB.setBgImg(bgImgrRealPath);
				this.threeTypeService.updateBgImg(groupB);
			}
			if(content !=null){
				groupC.setContent(content);
				groupC.setId(groupId);
				this.threeTypeService.updateContent(groupC);
			}
			return new	 ResultUtil(200,"ok","更新成功！");
		}
		return new ResultUtil(500, "error", "您不是组长，不能更改小组设定！");
	}
	
	@RequestMapping(value = "group/getAll", method = RequestMethod.GET)
	public ResultUtil findAllGroup(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, String token) {
		Users user = this.uService.findByToken(token);
		return new ResultUtil(200, "ok", this.threeTypeService.getGroup(pageNum, pageSize, user));
	}
    //小组排序 先排自己的小组（组长），再排加入的小组
	@RequestMapping(value = "group/userGroup", method = RequestMethod.GET)
	public ResultUtil findGroupByUser(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@NotNull Long userId) {
		return new ResultUtil(200, "ok", this.threeTypeService.findGroupByUser(userId, pageNum, pageSize));
	}

	@RequestMapping(value = "group/addMember/{token}", method = RequestMethod.GET)
	public ResultUtil addMember(@PathVariable @NotNull String token, Integer groupId) {
		Users user = this.uService.findByToken(token);
		UserGroup userGroup = new UserGroup(user.getId(), groupId);
		//加入唯一小组		
				List<Integer> l = this.ugMapper.SelectExistence(user.getId());
					  if(!(l.contains(groupId))){
						  this.UGservice.addMember(userGroup);
							return new ResultUtil(200 ,"ok", "加入小组成功!");
					  }
				return new ResultUtil(500 ,"no", "已加入!");
	}

	@RequestMapping(value = "group/delMember/{token}", method = RequestMethod.GET)
	public ResultUtil delMember(@PathVariable @NotNull String token, Integer groupId) {
		Users user = this.uService.findByToken(token);
		Long userId = this.threeTypeService.selectLeaderId(groupId);
		if(userId.equals(user.getId())){
			return new ResultUtil(500, "error", "您是组长，不能退出！");
		}
		UserGroup userGroup = new UserGroup(user.getId(), groupId);
		this.UGservice.delMember(userGroup);
		return new ResultUtil(200, "ok", "退出小组成功！");
	}

	@RequestMapping(value = "group/randGroup", method = RequestMethod.GET)
	public ResultUtil randGroup(String token) {
		Users user = this.uService.findByToken(token);
		return new ResultUtil(200, "ok", this.threeTypeService.findRandGroup(user));
	}

	@RequestMapping(value = "group/groupDetail", method = RequestMethod.GET)
	public ResultUtil findGroupById(Integer id) {
		return new ResultUtil(200, "ok", this.threeTypeService.findGroupById(id));
	}

	@RequestMapping(value = "group/hotGroup", method = RequestMethod.GET)
	public ResultUtil findHotGroup(String token) {
		Users user = this.uService.findByToken(token);
		return new ResultUtil(200, "ok", this.threeTypeService.selectHotGroup(user));
	}

	@RequestMapping("/group/panDuan")
    public ResultUtil panDuan(Integer groupId, Long userId){
    		List<UserGroup> ug = this.ugMapper.SelectpanDuan(new UserGroup(userId,groupId));
    		Integer flag;
    		if(ug.size()==0){
    			flag = 0;
    		}else{
    			flag = 1;
    		}
    		return new ResultUtil(200,"ok",flag);
    }
	
	@RequestMapping("ownGroup")
	public ResultUtil findOwnGroup(Integer userId,@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
		return new ResultUtil(200,"ok",this.threeTypeService.findOwnGroup(userId,pageNum,pageSize));
	}

	@RequestMapping("joinedGroup")
	public ResultUtil findJoinedGroup(Integer userId,@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
		return new ResultUtil(200,"ok",this.threeTypeService.findJoinedGroup(userId,pageNum,pageSize));
	}
	private boolean isExistGroup(String typeName) {

		if (this.threeTypeService.findGroupByTypeName(typeName) != null) {
			return true;
		}
		return false;
	}

}
