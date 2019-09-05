package com.worldkey.util;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author HP
 */
public class FileUploadUtilAsync {
    private static final Logger log = LoggerFactory.getLogger(FileUploadUtilAsync.class);

   /* private SystemConfigService systemConfigService;

    {
        systemConfigService= SpringUtil.getBean(SystemConfigService.class);
    }*/

    @Async
    public void upload(String fileName, MultipartFile file) throws IOException {
        String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dir = format.format(new Date());
        File f = new File("/mnt/image" + File.separator + dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        String uploadDir = f.getAbsolutePath() + File.separator + fileName + fileAfter;
        
   //测试     
//        @Async
//        public void upload(String fileName, MultipartFile file) throws IOException {
//            String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//            String dir = format.format(new Date());
//            File f = new File("/mnt/image-test" + File.separator + dir);
//            if (!f.exists()) {
//                f.mkdirs();
//            }
//            String uploadDir = f.getAbsolutePath() + File.separator + fileName + fileAfter;


        /*
        图片剪切为720宽，高等比例缩放的图片
         */
        File file1 = new File(uploadDir);
//         Thumbnails.of(file.getInputStream()).scale(1.00f).outputQuality(0.25f).toFile(file1);
        Thumbnails.of(file.getInputStream()).scale(1.00f).toFile(file1);
       log.info(" files):"+file1.getAbsolutePath() );

       /* try {
            File file1 = new File(uploadDir);
            file.transferTo(file1);
            log.warn("uploadDir:{}",file1.getAbsolutePath());
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }*/

    }

    /**
     * 文件上传，异步上传，反回文件名
     */
    public String getFileName(String host, MultipartFile file) throws IOException {
        if (file.getSize() == 0) {
            return "http://"+host+"/image/6cb922d7-e35f-4065-a37e-b4d5aef3e241.png";
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dir = format.format(new Date());
        String url = "http://" + host + "/image/" + dir + "/" + fileName + fileAfter;
        this.upload(fileName, file);
        return url;
    }
    
    
   // 测试
//    public String getFileName(String host, MultipartFile file) throws IOException {
//        if (file.getSize() == 0) {
//            return "http://"+host+"/image-test/6cb922d7-e35f-4065-a37e-b4d5aef3e241.png";
//        }
//        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
//        String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//        String dir = format.format(new Date());
//        String url = "http://" + host + "/image-test/" + dir + "/" + fileName + fileAfter;
//        this.upload(fileName, file);
//        return url;
//    }
    
    
    
    @Async
    public void upload2(String name2, MultipartFile file) throws IOException {
        String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dir = format.format(new Date());
        File f = new File("/mnt/image" + File.separator + dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        String uploadDir = f.getAbsolutePath() + File.separator + name2 + fileAfter;
   //测试     
//        @Async
//        public void upload2(String name2, MultipartFile file) throws IOException {
//            String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//            String dir = format.format(new Date());
//            File f = new File("/mnt/image-test" + File.separator + dir);
//            if (!f.exists()) {
//                f.mkdirs();
//            }
//            String uploadDir = f.getAbsolutePath() + File.separator + name2 + fileAfter;

        /*
        图片剪切为720宽，高等比例缩放的图片
         */
        File file1 = new File(uploadDir);
//        Thumbnails.of(file.getInputStream()).scale(1.00f).outputQuality(0.25f).toFile(file1);
        Thumbnails.of(file.getInputStream()).scale(1.00f).toFile(file1);
       log.info(" files):"+file1.getAbsolutePath() );

       /* try {
            File file1 = new File(uploadDir);
            file.transferTo(file1);
            log.warn("uploadDir:{}",file1.getAbsolutePath());
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }*/

    }
    /**
     * 文件上传，异步上传，反回文件名
     */
    public String getFileName2(String host, MultipartFile file,String name1) throws IOException {
        if (file.getSize() == 0) {
            return "http://"+host+"/image/6cb922d7-e35f-4065-a37e-b4d5aef3e241.png";
        }
//        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dir = format.format(new Date());
        String name2 = name1.replace(fileAfter,"");
        String url = "http://" + host + "/image/" + dir + "/" + name2 + fileAfter;
        this.upload2(name2, file);
        return url;
    }
      //测试  
//        public String getFileName2(String host, MultipartFile file,String name1) throws IOException {
//            if (file.getSize() == 0) {
//                return "http://"+host+"/image-test/6cb922d7-e35f-4065-a37e-b4d5aef3e241.png";
//            }
////            String fileName = UUID.randomUUID().toString().replaceAll("-", "");
//            String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//            String dir = format.format(new Date());
//            String name2 = name1.replace(fileAfter,"");
//            String url = "http://" + host + "/image-test/" + dir + "/" + name2 + fileAfter;
//            this.upload2(name2, file);
//            return url;
//        }

    @Async
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(String img) {
        if (img == null) {
            log.error("文件不存在：");
            return;
        }
        File file = new File(img);
        if (file.exists()) {
            boolean delete = file.delete();
            if (delete) {
                log.error("删除失败：" + img);
            }
            log.info("删除文件：" + img);
        } else {
            log.error("文件不存在：" + img);

        }

    }

}
