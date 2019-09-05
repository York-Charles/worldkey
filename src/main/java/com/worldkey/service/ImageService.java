package com.worldkey.service;

import com.worldkey.entity.Image;

import java.util.List;

public interface ImageService {
    void imageHandle(String info, String host);
    void imageUploadHandle(String realPath, String host);
    int insert(Image image);
    List<Image> findBySelective();
    int deleteUnused();
}
