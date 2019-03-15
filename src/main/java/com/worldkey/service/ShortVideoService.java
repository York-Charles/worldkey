package com.worldkey.service;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Video;

import java.util.List;

public interface ShortVideoService {

    void AddVideo(Video v);

    Video getRandomVideo();

    PageInfo<Video> getUserVideo(Long userId,Integer pageNum,Integer pageSize);

    boolean truncVideo(Integer id);
}
