package com.worldkey.util;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class VideoStorageAsync {

    public List<String> getFileName(String host, MultipartFile file) throws Exception{
        String fileName = UUID.randomUUID().toString().replaceAll("-","");
        String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dir = format.format(new Date());
        String url = "http://" + host + "/video/" + dir + "/" + fileName + fileAfter;
        this.upload(fileName, file);
        String castName = new VideoCastUtil().fetchFrame(fileName,host,fileAfter);
        List<String> l= new ArrayList<String>();
        l.add(url);
        l.add(castName);
        return l;
    }

    @Async
    public void upload(String fileName, MultipartFile file) throws IOException {
        String fileAfter = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dir = format.format(new Date());
        File f = new File("/mnt/video" + File.separator + dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        String uploadDir = f.getAbsolutePath() + File.separator + fileName + fileAfter;
        File files = new File(uploadDir);
        BufferedInputStream in = new BufferedInputStream(file.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(files));
        byte[] buf = new byte[1024];
        while(in.read(buf)!=-1) {
            out.write(buf);
            out.flush();
        }
        if(in!=null)in.close();
        if(out!=null)out.close();
    }

//  public static String fetchFrame(String videoFile,String host)
//            throws Exception {
//        String fileName = UUID.randomUUID().toString().replaceAll("-","");
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//        String dir = format.format(new Date());
//        this.StorageCasting(videoFile,fileName);
//        return "http://" + host + "/image/" + dir + "/" + fileName + ".png";
//    }
//
//    @Async
//    public void StorageCasting(String videoFile,String fileName) throws Exception {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//        String dir = format.format(new Date());
//        File f = new File("/mnt/video" + File.separator + dir);
//        if (!f.exists())f.mkdirs();
//        File targetFile = new File(f.getAbsolutePath() + File.separator + fileName + ".png");
//        if (!targetFile.exists()) targetFile.mkdirs();
//        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
//        ff.start();
//        int lenght = ff.getLengthInFrames();
//        int i = 0;
//        Frame fff = null;
//        while (i < lenght) {
//            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
//            fff = ff.grabFrame();
//            if ((i > 5) && (fff.image != null)) {
//                break;
//            }
//            i++;
//        }
//        opencv_core.IplImage img = fff.image;
//        int owidth = img.width();
//        int oheight = img.height();
//        // 对截取的帧进行等比例缩放
//        int width = 800;
//        int height = (int) (((double) width / owidth) * oheight);
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//        bi.getGraphics().drawImage(fff.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),
//                0, 0, null);
//        ImageIO.write(bi, "jpg", targetFile);
//        ff.stop();
//    }
}
