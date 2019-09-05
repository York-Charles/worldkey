package com.worldkey.service.impl;

import com.worldkey.entity.Image;
import com.worldkey.mapper.ImageMapper;
import com.worldkey.service.ImageService;
import com.worldkey.service.SystemConfigService;
import com.worldkey.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author HP
 */
@Service
public class ImageServiceImpl implements ImageService{
	private ImageMapper imageMapper;
	private SystemConfigService systemConfigService;
	/**
	 * 将info中使用的文件修改为正在使用
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void imageHandle(String info,String host){
		if (info==null){
			return;
		}
		List<String>list=StringUtil.info2ImageList(info, host);
		for (String string : list) {
			Image record1=new Image();
			record1.setUrl(string);
			record1.setUsed(1);
			imageMapper.updateByUrl(record1);
		}
		
	}
	/**
	 * 富文本进行文件上传时存入数据库，默认为未使用
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void imageUploadHandle(String realPath,String host){
		
		Image record=new Image();
	    record.setUrl(realPath);
	    String loc= realPath.replace("http://"+host+"/image",systemConfigService.find().getFilesrc());
	    record.setLocation(loc);
	    record.setUsed(0);
	    imageMapper.insertSelective(record);
	}
	/**
	 * 添加
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int insert(Image image){
	   return imageMapper.insertSelective(image);
	}
	/**
	 * 根据条件查询
	 */
	@Override
    public List<Image>findBySelective(){
		return this.imageMapper.selectBySelective();
	}
	/**
	 * 删除数据库空未使用图片的记录
	 */ 
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int deleteUnused(){
		return this.imageMapper.deleteUnused();
	}
	@Autowired
	public void setImageMapper(ImageMapper imageMapper) {
		this.imageMapper = imageMapper;
	}
	@Autowired
	public void setSystemConfigService(SystemConfigService systemConfigService) {
		this.systemConfigService = systemConfigService;
	}
}
