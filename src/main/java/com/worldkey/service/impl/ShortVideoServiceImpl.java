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
    public Video getRandomVideo() {
        return this.shortVideoMapper.getRandomVideo();
    }
}
