package com.worldkey.scheduled;

import com.worldkey.entity.Image;
import com.worldkey.service.impl.ImageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 图片模快删除
 */
/*@Service*/
public class ImageScheduling {
	
	private static Logger log=LoggerFactory.getLogger(ImageScheduling.class);
	@Resource
	private ImageServiceImpl imageServiceImpl;
	//每5秒钟执行一次
	/*@Scheduled(fixedRate = 5000)*/

	//每天01点执行
	/*@Scheduled(cron="0 0 01 ? * *")*/
	public void delImage(){
		log.info("图片删除定时器启动：");
		List<Image>list= imageServiceImpl.findBySelective();
		File f;
		int count=0;
		for (Image i : list) {
			f=new File(i.getLocation());
			if (f.exists()) {
				boolean delete = f.delete();
				if (delete)
				log.info("删除文件 ："+f.getName());
				count++;
			}
		}
		 this.imageServiceImpl.deleteUnused();
		log.info("删除了image表中未使用图片的记录："+count+" 条");
	}
	
}
