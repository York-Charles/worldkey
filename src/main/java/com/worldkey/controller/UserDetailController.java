package com.worldkey.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.worldkey.entity.Album;
import com.worldkey.entity.Users;
import com.worldkey.service.AlbumService;
import com.worldkey.service.UsersService;
import com.worldkey.util.FileUploadUtilAsync;
import com.worldkey.util.ResultUtil;

@Controller
@ResponseBody
@RequestMapping("uDetail")
public class UserDetailController {

	@Resource
	private UsersService uService;
	@Resource
	private AlbumService albumService;

	@RequestMapping("getAlbums")
	public ResultUtil getAlbums(Integer userId) {
		return new ResultUtil(200, "ok", this.albumService.getAlbum(userId));
	}

	@RequestMapping("relationAndComments")
	public ResultUtil getRelationsAndComments(Long userId){
		return new ResultUtil(200,"ok",this.uService.getRelationsAndComments(userId));
	}
	
	@RequestMapping(value = "editDetail")
	public ResultUtil editDetail(Users user) throws Exception {
		if(user.getPetName()==null&user.getSignature()==null&&user.getSex()==null&&user.getAge()==null&&
				user.getConstellation()==null&&user.getHeight()==null&&user.getWeight()==null&&user.getEmotional()==null
				&&user.getOccupation()==null&&user.getFond()==null&&user.getBirth()==null){
			return new ResultUtil(500,"error","您未更改任何信息");
		}
		if(user.getBirth()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			user.setBirthday(sdf.parse(user.getBirth()));
			user.setBirth(null);
		}
		this.uService.updateByPrimaryKeySelective(user);
		return new ResultUtil(200, "ok", this.uService.selectByPrimaryKey(user.getId()));
	}
	
	@RequestMapping(value="editHeadImg" ,method = RequestMethod.POST)
	public ResultUtil editHeadImg(HttpServletRequest request,Users user) throws Exception{
		String[] allowedType = { "image/bmp", "image/gif", "image/jpeg", "image/png", "image/jpg" };
		MultipartFile file = ((MultipartHttpServletRequest) request).getFile("headimg");
		String headImg = "";
		if (file != null) {
			if (!file.isEmpty()) {
				boolean allowed = Arrays.asList(allowedType).contains(file.getContentType());
				if (!allowed) {
					return new ResultUtil(500, "error", "不支持的文件类型!");
				}
				headImg = new FileUploadUtilAsync().getFileName(request.getHeader("host"), file);
			}
		}
		user.setHeadImg(headImg);
		this.uService.updateByPrimaryKeySelective(user);
		return new ResultUtil(200, "ok", headImg);
	}

	@RequestMapping("album/{token}")
	public ResultUtil editAlbum(@PathVariable String token, HttpServletRequest request, Integer fileNum)
			throws Exception {
		Users user = this.uService.findByToken(token);
		String[] allowedType = { "image/bmp", "image/gif", "image/jpeg", "image/png", "image/jpg" };
		for (int i = 0; i < fileNum; i++) {
			MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file" + i);
			if (!file.isEmpty()) {
				boolean allowed = Arrays.asList(allowedType).contains(file.getContentType());
				if (!allowed) {
					return new ResultUtil(500, "error", "不支持的文件类型!");
				}
				String realPath = new FileUploadUtilAsync().getFileName(request.getHeader("host"), file);
				this.albumService.addAlbum(new Album(Integer.parseInt(user.getId()+""),realPath));
			}
		}
		return new ResultUtil(200, "ok", "相册更新成功");
	}

	@RequestMapping("editFond")
	public ResultUtil editFond(Long userId,String fond) {
		Users user = new Users();
		user.setFond(StringUtils.strip(fond,"[]"));
		user.setId(userId);
		this.uService.editFond(user);
		return new ResultUtil(200, "ok", StringUtils.strip(fond,"[]"));
	}
	
	
	
	//礼物榜
	@RequestMapping("allPresentors")
	public ResultUtil allPresentors(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,Long id){
		return new ResultUtil(200,"ok",this.uService.getPresentors(id, pageNum, pageSize));
	}
		
	@RequestMapping("delAlbum/{token}")
	public ResultUtil delAlbum(@PathVariable String token,Integer num, HttpServletRequest request){
		Users user = this.uService.findByToken(token);
		if(user==null){
			return new ResultUtil(500,"error",1);
		}else{
		for(int i = 0;i<num;i++){
			String url = request.getParameter("url"+i);
			this.albumService.delAlbum(url);
		}
		return new ResultUtil(200,"ok",2);
		}
	}
}
