package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.*;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.OneTypeService;
import com.worldkey.service.ThreeTypeService;
import com.worldkey.service.TwoTypeService;
import com.worldkey.service.UsersService;
import com.worldkey.util.FileUploadUtilAsync;
import com.worldkey.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("show")
@ResponseBody
@Slf4j
public class ShowController {

	@Resource
	private UsersService usersService;
	@Resource
	private InformationAllService informationAllService;
	@Resource
	private TwoTypeService twoTypeService;
	@Resource
	private OneTypeService oneTypeService;
	@Resource
	private ThreeTypeService threeTypeService;

	/**
	 * 展示推送到生态系统
	 */
	@RequestMapping("push")
	public ResultUtil push(Long itemID, Integer type, @RequestHeader("host") String host) {
		log.info(itemID + ">>>>" + type);
		Integer integer = informationAllService.showPush(itemID, type, host);
		return new ResultUtil(200, "ok", integer);
	}

	/**
	 * 管理页面list
	 */
	@RequestMapping("manageList")
	public ModelAndView list(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer pageSize, Integer oneType, Integer twoType, Integer threeType,
			@RequestParam(defaultValue = "") String q) {
		ModelAndView model = new ModelAndView("show/list");
		model.addObject("q", q);
		// 4.19
		if (threeType != null) {
			model.addObject("threeType", threeType);
		}
		if (twoType != null) {
			model.addObject("twoType", twoType);
		}

		OneType one;
		List<TwoType> twoTypes;
		// 4.18
		List<ThreeType> threeTypes;

		if (oneType != null) {
			PageHelper.startPage(page, pageSize);
			List<Show> list = this.informationAllService.findShowByOneTypeAll(oneType, q);
			PageInfo<Show> pageInfo = new PageInfo<>(list);
			model.addObject("pageInfo", pageInfo);
			// 查询的一级分类
			one = this.oneTypeService.findById(oneType);
			// 查询的二级分类
			twoTypes = twoTypeService.findByOne(oneType);
			// 4.18
			threeTypes = threeTypeService.findByTwo(twoType);

			model.addObject("oneType", one);
			model.addObject("twoTypes", twoTypes);
			// 4.18
			model.addObject("threeTypes", threeTypes);
		} else {
			TwoType byId = twoTypeService.findById(twoType);
			// //4.18
			ThreeType byId2 = threeTypeService.findById(threeType);
			// 查询的一级分类
			one = this.oneTypeService.findById(byId.getOneType());
			// 查询的二级分类
			twoTypes = twoTypeService.findByOne(byId.getOneType());
			// 4.18
			threeTypes = threeTypeService.findByTwo(byId2.getTwoType());

			PageHelper.startPage(page, pageSize);
			List<Show> list3 = this.informationAllService.findShowByTwoTypeAll(twoType);
			// 4.18
			List<Show> list = this.informationAllService.findShowByThreeTypeAll(threeType);
			PageInfo<Show> pageInfo = new PageInfo<>(list);
			model.addObject("pageInfo", pageInfo);
			log.debug(pageInfo.getList().toString());
		}
		model.addObject("oneType", one);
		model.addObject("twoTypes", twoTypes);
		// 4.18
		model.addObject("threeTypes", threeTypes);
		// 所有分类
		List<OneType> oneTypes = this.oneTypeService.selectAllOneTypeWithTwoType();
		model.addObject("oneTypes", oneTypes);
		return model;
	}

	/**
	 * 发布展示
	 */
	@RequestMapping("release")
	public ResultUtil release(String token, @RequestHeader("host") String host, String typeName, String info,
			String title, String titleImgs) {
		Users user = usersService.findByToken(token);
		// 未登录
		if (user == null) {
			return new ResultUtil(200, "no", "未登录");
		}
		TwoType twoType = twoTypeService.findByTypeName(typeName);
		// 4.18
		ThreeType threeType = threeTypeService.findByTypeName(typeName);
		// 发布的分类不存在
		if (threeType == null) {
			return new ResultUtil(200, "no", "分类不存在");
		}
		InformationAll informationAll = new InformationAll(title, titleImgs, threeType.getId(), user.getPetName(), 0,
					user, 4, info);
		String add = this.informationAllService.add1(informationAll, host);
		// 发布成功
		if (add != null) {
			return new ResultUtil(200, "ok", add);
		}
		// 发布失败 一般不会发生
		return new ResultUtil(200, "ok", "发布失败");
	}
	
	@RequestMapping("release1")
	public ResultUtil release1(String token, @RequestHeader("host") String host, String typeName, String info,
			String title, String titleImgs) {
		Users user = usersService.findByToken(token);
		// 未登录
		if (user == null) {
			return new ResultUtil(200, "no", "未登录");
		}
		TwoType twoType = twoTypeService.findByTypeName(typeName);
		// 4.18
		ThreeType threeType = threeTypeService.findByTypeName(typeName);
		// 发布的分类不存在
		if (threeType == null) {
			return new ResultUtil(200, "no", "分类不存在");
		}
		InformationAll informationAll = new InformationAll(title, titleImgs, threeType.getId(), user.getPetName(), 0,
					user, 4, info,1);
		informationAll.setUserBrand(1);
		String add = this.informationAllService.add1(informationAll, host);
		// 发布成功
		if (add != null) {
			return new ResultUtil(200, "ok", add);
		}
		// 发布失败 一般不会发生
		return new ResultUtil(200, "ok", "发布失败");
	}

	/**
	 * 以一级分类查找展示
	 */
	@RequestMapping("oneType/list")
	public ResultUtil findByOneType(String oneTypeName, Long userId, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		if (userId == null) {
			return new ResultUtil(406, "no", "用户编号不能为空");
		}
		PageHelper.startPage(page, pageSize, true);
		List<BaseShow> shows = this.informationAllService.findShowByOneType(oneTypeName, userId);
		PageInfo<BaseShow> pageInfo = new PageInfo<>(shows);
		return new ResultUtil(200, "ok", pageInfo);
	}

	/**
	 * 以二级分类查找展示
	 */
	@RequestMapping("twoType/list")
	public @ResponseBody ResultUtil findByTwoType(String twoTypeName, Long userId,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
		if (userId == null) {
			return new ResultUtil(406, "no", "用户编号不能为空");
		}
		PageHelper.startPage(page, pageSize, true);
		List<BaseShow> shows = this.informationAllService.findShowByTwoType(twoTypeName, userId);
		PageInfo<BaseShow> pageInfo = new PageInfo<>(shows);
		return new ResultUtil(200, "ok", pageInfo);
	}
	

	
	

	/**
	 * 4.19添加三级
	 */
	@RequestMapping("threeType/list")
	public @ResponseBody ResultUtil findByThreeType(String threeTypeName, Long userId,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
		if (userId == null) {
			return new ResultUtil(406, "no", "用户编号不能为空");
		}
		PageHelper.startPage(page, pageSize, true);
		// 4.18
		List<BaseShow> shows = this.informationAllService.findShowByThreeType(threeTypeName, userId);
		PageInfo<BaseShow> pageInfo = new PageInfo<>(shows);
		return new ResultUtil(200, "ok", pageInfo);
	}

	/**
	 * 5.17 查找自己的说说
	 */
	@RequestMapping("findshuoshuo/list")
	public @ResponseBody ResultUtil findshuoshuo(Long userId, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		if (userId == null) {
			return new ResultUtil(406, "no", "用户编号不能为空");
		}
		return new ResultUtil(200, "ok", this.informationAllService.findShuoshuo(userId,page,pageSize));
	}

	@GetMapping("addShuoContent")
	public ResultUtil addShuo(String token, String typeName, String title) {
		Users user = this.usersService.findByToken(token);
		InformationAll info = new InformationAll();
		info.setAuther(user.getPetName());
		info.setUsers(user);
		info.setTitle(title);
		ThreeType threeType = threeTypeService.findByTypeName(typeName);
		info.setType(threeType.getId());
		this.informationAllService.addShuoContent(info);
		return new ResultUtil(200, "ok", info.getId());
	}

	@RequestMapping(value="addShuo",method=RequestMethod.POST)
	public ResultUtil addShuo(String token, 
			@RequestHeader("host") String host, 
			HttpServletRequest request,@RequestParam(defaultValue="-1") Integer imgNum){
		Users user = usersService.findByToken(token);
		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		String typeName = params.getParameter("typeName");
		String title = params.getParameter("title");
		String titleImg = "";
		String info = "";
		String realPath = "";
		if (imgNum>=0) {
			for (int i = 0; i < imgNum; i++) {
				MultipartFile infoImg = params.getFile("info" + i);
				String[] allowedType = { "image/bmp", "image/gif", "image/jpeg", "image/png","image/jpg" };
				boolean allowed = Arrays.asList(allowedType).contains(infoImg.getContentType());
				if (!allowed) {
					return new ResultUtil(200, "ok", "error|不支持的类型");
				}
				try {
					realPath = new FileUploadUtilAsync().getFileName(request.getHeader("host"), infoImg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String streo = changeToHtml(realPath);
				info = info + streo;
				titleImg = titleImg +"," +realPath;
			}
			titleImg = titleImg.substring(1, titleImg.length());
		}

		// 未登录
		if (user == null) {
			return new ResultUtil(200, "no", "未登录");
		}
		// 4.18
		ThreeType threeType = threeTypeService.findByTypeName(typeName);
		// 发布的分类不存在
		if (threeType == null) {
			return new ResultUtil(200, "no", "分类不存在");
		}

		InformationAll informationAll = new InformationAll(title, titleImg, threeType.getId(), user.getPetName(), 0,
				user, 4, info);
		String add = this.informationAllService.addShuo(informationAll, host);
		// 发布成功
		if (add != null) {
			return new ResultUtil(200, "ok", add);
		}
		// 发布失败 一般不会发生
		return new ResultUtil(200, "ok", "发布失败");
	}
	/**
	 * 个人品牌文章
	 * @param token
	 * @param host
	 * @param typeName
	 * @param info
	 * @param title
	 * @param titleImgs
	 * @param userBrand
	 * @return
	 */
	@RequestMapping("editBrandArticle")
	public ResultUtil editBrandArticle(String token, @RequestHeader("host") String host, String typeName, String info,
			String title, String titleImgs,String userBrand) {
		Users user = usersService.findByToken(token);
		// 未登录
		if (user == null) {
			return new ResultUtil(200, "no", "未登录");
		}
		ThreeType threeType = threeTypeService.findByTypeName(typeName);
		// 发布的分类不存在
		if (threeType == null) {
			return new ResultUtil(200, "no", "分类不存在");
		}
		InformationAll informationAll = new InformationAll(title, titleImgs, threeType.getId(), user.getPetName(), 0,
					user, 4, info);
		String add = this.informationAllService.editBrandArticle(informationAll, host);
		// 发布成功
		if (add != null) {
			return new ResultUtil(200, "ok", add);
		}
		// 发布失败 一般不会发生
		return new ResultUtil(500, "ok", "修改失败");
	}

	private String changeToHtml(String s) {
		StringBuilder sb = new StringBuilder("<img src=\"");
		sb.append(s);
		sb.append("\">");
		return sb.toString();
	}
}
