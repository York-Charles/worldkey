package com.worldkey.service;

import com.baomidou.mybatisplus.service.IService;
import com.worldkey.entity.Prize;
import com.worldkey.entity.Users;

import java.util.List;

public interface PrizeService extends IService<Prize> {
    List<Prize> selectAll();
    Prize prizeDraw(Users users);
}
