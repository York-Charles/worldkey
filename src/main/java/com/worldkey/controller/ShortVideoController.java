package com.worldkey.controller;

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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("addVideo/{token}")
    public ResultUtil uploadVideo(String name, @PathVariable String token,@RequestHeader("host")String host,
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
                return new ResultUtil(200,"ok",list.get(1));
            }
        }
        return new ResultUtil(200,"ok","");
    }

    /**
     * 测试视频上传接口
     * @param name
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addVideo")
    public ResultUtil uploadVideo1(String name,@RequestHeader("host")String host,
                                  HttpServletRequest request) throws Exception{
        MultipartFile file = ((MultipartHttpServletRequest)request).getFile("video");
        String[] allowedType = {"video/mp4","video/wmv","video/avi"};
        if (file != null) {
            if (!file.isEmpty()) {
                boolean allowed = Arrays.asList(allowedType).contains(file.getContentType());
                if (!allowed) {
                    return new ResultUtil(500, "error", "不支持的文件类型!");
                }
                List<String> list= new VideoStorageAsync().getFileName(host, file);
                this.shortVideoService.AddVideo(new Video(name,0,
                        list.get(0),list.get(1)));
                return new ResultUtil(200,"ok",list.get(1));
            }
        }
        return new ResultUtil(200,"ok","上传成功！");
    }
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
}
