package com.worldkey.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.KbDetail;

public interface KbDetailService extends IService<KbDetail> {
    PageInfo<KbDetail> findByUid(Integer page, Integer pageSize, Long users);
}
