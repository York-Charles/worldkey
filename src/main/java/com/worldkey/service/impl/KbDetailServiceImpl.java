package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.KbDetail;
import com.worldkey.mapper.KbDetailMapper;
import com.worldkey.service.KbDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KbDetailServiceImpl extends ServiceImpl<KbDetailMapper, KbDetail> implements KbDetailService{
    @Resource
    private KbDetailMapper kbDetailMapper;

    @Override
    public PageInfo<KbDetail> findByUid(Integer page, Integer pageSize, Long users) {
        PageHelper.startPage(page, pageSize, true);
        List<KbDetail> kbDetails = kbDetailMapper.findByUid(users);
        return new PageInfo<>(kbDetails);
    }
}
