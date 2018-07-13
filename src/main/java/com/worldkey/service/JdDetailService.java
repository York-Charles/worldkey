package com.worldkey.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.JdDetail;

import java.util.List;

public interface JdDetailService extends IService<JdDetail> {

    List<JdDetail> findByUsers(Long users);

    PageInfo<JdDetail> findByUid(Integer page, Integer pageSize, Long users);
    
    //<---以下方法---薛秉承--2018.4.11写入-->
    PageInfo<JdDetail> findByjust(Integer page, Integer pageSize, Long users);
}
