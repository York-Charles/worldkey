package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Show;
import com.worldkey.entity.Users;
import com.worldkey.entity.Video;
import com.worldkey.mapper.ShortVideoMapper;
import com.worldkey.service.ShortVideoService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import com.worldkey.util.VideoStorageAsync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("ShortVideo")
@Slf4j
public class ShortVideoController {

	@Resource
	UsersService usersService;
	@Resource
	ShortVideoService shortVideoService;
	@Resource
	ShortVideoMapper svm;

	   @RequestMapping("addVideo")
	    public ResultUtil uploadVideo(String name,String token,@RequestHeader("host")String host,
	                                  HttpServletRequest request) throws Exception{
	        Users u = this.usersService.findByToken(token);
	        MultipartFile file = ((MultipartHttpServletRequest)request).getFile("video");
	        String[] allowedType = {"video/mp4","video/wmv","video/avi"};
	        if (file != null) {
	            if (!file.isEmpty()) {
	                boolean allowed = Arrays.asList(allowedType).contains(file.getContentType());
	                if (!allowed) {
	                    return new ResultUtil(500, "error", "不支持的文件类型!");
	                }
	                List<String> list= new VideoStorageAsync().getFileName(host, file);
	                this.shortVideoService.AddVideo(new Video(name,u.getId().intValue()
	                        , list.get(0),list.get(1)));
//	                return new ResultUtil(200,"ok",list.get(1));
	                return new ResultUtil(200,"ok",new Video(list.get(0),list.get(1)));
	            }
	        }
	        return new ResultUtil(200,"ok","");
	    }

//	    /**
//	     * 测试视频上传接口
//	     * @param name
//	     * @param request
//	     * @return
//	     * @throws Exception
//	     */
//	    @RequestMapping("addVideo")
//	    public ResultUtil uploadVideo1(String name,@RequestHeader("host")String host,
//	                                  HttpServletRequest request) throws Exception{
//	        MultipartFile file = ((MultipartHttpServletRequest)request).getFile("video");
//	        String[] allowedType = {"video/mp4","video/wmv","video/avi"};
//	        if (file != null) {
//	            if (!file.isEmpty()) {
//	                boolean allowed = Arrays.asList(allowedType).contains(file.getContentType());
//	                if (!allowed) {
//	                    return new ResultUtil(500, "error", "不支持的文件类型!");
//	                }
//	                List<String> list= new VideoStorageAsync().getFileName(host, file);
//	                this.shortVideoService.AddVideo(new Video(name,0,
//	                        list.get(0),list.get(1)));
//	                return new ResultUtil(200,"ok",list.get(1));
//	            }
//	        }
//	        return new ResultUtil(200,"ok","上传成功！");
//	    }
    @RequestMapping("randomVideo")
    public ResultUtil getRandomVideo(){
        return new ResultUtil(200,"ok",this.shortVideoService.getRandomVideo());
    }

    @RequestMapping("getUserVideo")
    public ResultUtil getUserVideo(Long userId,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum
            ,@RequestParam(value="pageSize",defaultValue="10")Integer pageSize){
        return new ResultUtil(200,"ok",
                this.shortVideoService.getUserVideo(userId,pageNum,pageSize));
    }

    @RequestMapping("truncate")
    public ResultUtil truncateVideo(Integer id){
        this.shortVideoService.truncVideo(id);
        return new ResultUtil(200,"ok","删除成功");
    }

    @RequestMapping("KeywordQuery")
    public ResultUtil getAllied(String keyword) {
        return new ResultUtil(200,"ok",this.svm.getAlliedVideo(keyword));
    }

	/**
	 * 视频管理页面
	 */
	@RequestMapping("video")
	public ModelAndView video(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pagesize", defaultValue = "2") Integer pageSize, @RequestHeader("host") String host,
			@RequestParam(defaultValue = "") String q) {
		ModelAndView model = new ModelAndView("show/video");
		PageInfo<Video> page = new PageInfo<Video>();
		model.addObject("q", q);
		page = this.shortVideoService.getAll(q, pageNum, pageSize);
		model.addObject("pageinfo", page);
		return model;
	}
	
	@RequestMapping("videoRecycle")
	public ModelAndView videoRecycle(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pagesize", defaultValue = "2") Integer pageSize, @RequestHeader("host") String host,
			@RequestParam(defaultValue = "") String q) {
		ModelAndView model = new ModelAndView("show/videoRecycle");
		PageInfo<Video> page = new PageInfo<Video>();
		model.addObject("q", q);
		page = this.shortVideoService.getAllRecycle(q, pageNum, pageSize);
		model.addObject("pageinfo", page);
		return model;
	}

	@RequestMapping("checked")
	public ResultUtil checked(Integer checked, Integer id) {
		return new ResultUtil(200, "ok", this.shortVideoService.checked(checked, id));
	}
	
	@RequestMapping("stick")
	public ResultUtil stick(Integer id) {
		return new ResultUtil(200, "ok", this.shortVideoService.stick(id));
	}

	@RequestMapping("toDel")
	public ResultUtil todel(Integer id) {
		Integer i=this.shortVideoService.todel(id);
		if(i!=null){
		return new ResultUtil(200,"ok","刪除成功");
		}else{
			return new ResultUtil(200,"no","刪除失败");
		}
	}
	
	@RequestMapping("del")
	public ResultUtil del(Integer id) {
		Integer i=this.shortVideoService.del(id);
		if(i!=null){
			log.info("刪除成功"+i);
		return new ResultUtil(200,"ok","刪除成功");
		}else{
			log.info("刪除失敗"+i);
			return new ResultUtil(200,"no","刪除失败");
		}
	}
}
