package com.worldkey.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.ZsDetail;

public interface ZsDetailService extends IService<ZsDetail> {
    PageInfo<ZsDetail> findByUid(Integer page, Integer pageSize, Long users);
    
    //<---以下方法---薛秉承--2018.4.11写入-->
    PageInfo<ZsDetail> findByZsjust(Integer page, Integer pageSize, Long users);
}

