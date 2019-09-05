package com.worldkey.controller;

import com.worldkey.service.ImageService;
import com.worldkey.util.FileUploadUtilAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * @author HP
 */
@Controller
public class WangEditorUploadController {
    @Resource
    private ImageService imageService;

    private static Logger log= LoggerFactory.getLogger(WangEditorUploadController.class);

    @RequestMapping("/upload")
    @ResponseBody
    public void to2(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws IOException, ServletException {


        response.setContentType("textml;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        // 文件类型限制
        String[] allowedType = {"image/bmp", "image/gif", "image/jpeg", "image/png"};
        System.out.println(file.isEmpty());
        boolean allowed = Arrays.asList(allowedType).contains(file.getContentType());
        if (!allowed) {
            response.getWriter().write("error|不支持的类型");
            return;
        }
        String realPath = new FileUploadUtilAsync().getFileName(request.getHeader("host"), file);
        //图片放入临时文件夹
        imageService.imageUploadHandle(realPath, request.getHeader("host"));
        // 返回图片的URL地址
        response.getWriter().write(realPath);
    }
    @RequestMapping("/upload2")
    @ResponseBody
    public  Object  to3(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("errno","0");
        // 文件类型限制
        String[] allowedType = {"image/bmp", "image/gif", "image/jpeg", "image/png"};
        log.debug(file.isEmpty()+"");
        boolean allowed = Arrays.asList(allowedType).contains(file.getContentType());
        if (!allowed) {
            return "error|不支持的类型";
        }
        String realPath = new FileUploadUtilAsync().getFileName(request.getHeader("host"), file);
        // 返回图片的URL地址
        String[]strings={realPath};
        stringObjectHashMap.put("data",strings);
        return stringObjectHashMap;
    }
    
    @RequestMapping("/upload3")
    @ResponseBody
    public  Object  to3(HttpServletRequest request, HttpServletResponse response, MultipartFile file,String name) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("errno","0");
        // 文件类型限制
        String[] allowedType = {"image/bmp", "image/gif", "image/jpeg", "image/png"};
        log.debug(file.isEmpty()+"");
        boolean allowed = Arrays.asList(allowedType).contains(file.getContentType());
        if (!allowed) {
            return "error|不支持的类型";
        }
        String name1 = name.replace("/storage/emulated/0/Android/data/life.dubai.com.mylife/files/", "");
        String realPath = new FileUploadUtilAsync().getFileName2(request.getHeader("host"), file,name1);
        // 返回图片的URL地址
        String[]strings={realPath};
        stringObjectHashMap.put("data",strings);
        return stringObjectHashMap;
    }
    
    
//    @RequestMapping("delete")
//    @ResponseBody
//    public void deleteImg(String img){
//    	 SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//         String dir = format.format(new Date());
//         String s = "D:\\mnt\\image\\"+ dir+"\\"+img;//文件的绝对路径
//         File file = new File(s);
//         if(file.exists()){
//        	    boolean d = file.delete();
//        	 
//
//        	 
//
//        	    if(d){
//        	     System.out.print("删除成功！");
//        	    }else{
//        	     System.out.print("删除失败！");
//        	    }
//        	   }  
//    	return;
//    }
    
}
