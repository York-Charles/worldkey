package com.worldkey.util;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public class VideoCastUtil {

    public static String image_type="jpg";

    public String fetchFrame(String videoFile,String host,String fileAfter)
            throws Exception {
        String fileName = UUID.randomUUID().toString().replaceAll("-","");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dir = format.format(new Date());
        this.StorageCasting(videoFile,fileName,fileAfter);
        return "http://" + host + "/image/" + dir + "/" + fileName + "." +image_type;
//测试
//        return "http://" + host + "/image-test/" + dir + "/" + fileName + "." +image_type;
    }

    @Async
    public void StorageCasting(String videoFile,String fileName,String fileAfter) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dir = format.format(new Date());
        File f = new File("/mnt/image" + File.separator + dir);
//测试
//        File f = new File("/mnt/image-test" + File.separator + dir);
        if (!f.exists())f.mkdirs();
        File targetFile = new File(f.getAbsolutePath() + File.separator + fileName + ".jpg");
        if (!targetFile.exists()) targetFile.createNewFile();
        log.info(targetFile.getAbsolutePath());
        File f1 = new File("/mnt/video" + File.separator + dir);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(f1.getAbsolutePath() +
                File.separator + videoFile + fileAfter);
        ff.start();
        String rotate = ff.getVideoMetadata("rotate");
        log.info(rotate+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        int length = ff.getLengthInFrames();
        int i = 0;
        Frame fff = null;
        while (i < length) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            fff = ff.grabFrame();
            if ((i > 5) && (fff.image != null)) {
                break;
            }
            i++;
        }
//        opencv_core.IplImage img = fff.image;
        int owidth = ff.getImageWidth();
        int oheight = ff.getImageHeight();
        // 对截取的帧进行等比例缩放
//        int width = 800;
//        int height = (int) (((double) width / owidth) * oheight);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage fetchedImage = converter.getBufferedImage(fff);
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//        bi.getGraphics().drawImage(fetchedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH),
//                0, 0, null);
        BufferedImage bi = new BufferedImage(owidth, oheight, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fetchedImage.getScaledInstance(owidth, oheight, Image.SCALE_SMOOTH),
                0, 0, null);
        ImageIO.write(bi, image_type, targetFile);
        ff.stop();
        if (rotate != null && !rotate.isEmpty()) {
            //旋转图片
            rotatePhonePhoto(targetFile.getAbsolutePath()
                    , Integer.parseInt(rotate));
        }
    }
    public static String rotatePhonePhoto(String fullPath, int angel) {
        BufferedImage src;
        try {
            src = ImageIO.read(new File(fullPath));
            int src_width = src.getWidth(null);
            int src_height = src.getHeight(null);

            int swidth = src_width;
            int sheight = src_height;

            if (angel == 90 || angel == 270) {
                swidth = src_height;
                sheight = src_width;
            }
            Rectangle rect_des = new Rectangle(new Dimension(swidth, sheight));
            BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();
            g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
            g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
            g2.drawImage(src, null, null);
            ImageIO.write(res,image_type, new File(fullPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullPath;
    }
}
