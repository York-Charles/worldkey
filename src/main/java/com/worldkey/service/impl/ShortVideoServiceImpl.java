package com.worldkey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Video;
import com.worldkey.mapper.ShortVideoMapper;
import com.worldkey.service.ShortVideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShortVideoServiceImpl implements ShortVideoService {

    @Resource
    ShortVideoMapper shortVideoMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void AddVideo(Video v){
        this.shortVideoMapper.addVideo(v);
    }

    @Override
    public PageInfo<Video> getUserVideo(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Video> v = this.shortVideoMapper.getUserVideo(userId);
        return new PageInfo<>(v);
    }


    @Override
    public boolean truncVideo(Integer id) {
        return this.shortVideoMapper.truncVideo(id);
    }

    @Override
    public String getRandomVideo() {
        return this.shortVideoMapper.getRandomVideo();
    }

	@Override
	public PageInfo<Video> getAll(String q,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Video> list = this.shortVideoMapper.getAll(q);
		return new PageInfo<>(list);
	}

	@Override
	public Integer checked(Integer checked, Integer id) {
		return this.shortVideoMapper.checked(checked,id);
	}
	
	@Override
	public Integer stick(Integer id) {
		return this.shortVideoMapper.stick(id);
	}

	@Override
	public Integer todel(Integer id) {
		return this.shortVideoMapper.todel(id);
	}

	@Override
	public PageInfo<Video> getAllRecycle(String q,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Video> list = this.shortVideoMapper.getAllRecycle(q);
		return new PageInfo<>(list);
	}
	
	@Override
	public Integer del(Integer id) {
		return this.shortVideoMapper.del(id);
	}
}
