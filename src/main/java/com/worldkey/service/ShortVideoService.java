package com.worldkey.service;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Video;

import java.util.List;

public interface ShortVideoService {

    void AddVideo(Video v);

    String getRandomVideo();

    PageInfo<Video> getUserVideo(Long userId,Integer pageNum,Integer pageSize);

    boolean truncVideo(Integer id);
    
    PageInfo<Video> getAll(String q,Integer pageNum, Integer pageSize);
    
    PageInfo<Video> getAllRecycle(String q,Integer pageNum, Integer pageSize);
    
    Integer checked(Integer checked, Integer id);
    
    Integer stick(Integer id);
    
    Integer todel(Integer id);
    
    Integer del(Integer id);
}
