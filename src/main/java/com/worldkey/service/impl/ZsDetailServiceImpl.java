package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.ZsDetail;
import com.worldkey.mapper.ZsDetailMapper;
import com.worldkey.service.ZsDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ZsDetailServiceImpl extends ServiceImpl<ZsDetailMapper, ZsDetail> implements ZsDetailService {
    @Resource
    private ZsDetailMapper zsDetailMapper;

    @Override
    public PageInfo<ZsDetail> findByUid(Integer page, Integer pageSize, Long users) {
        PageHelper.startPage(page, pageSize, true);
        List<ZsDetail> zsDetails = zsDetailMapper.fingByUid(users);
        return new PageInfo<>(zsDetails);
    }
    
    //<---以下方法---薛秉臣--2018.4.11写入-->
    @Override
	public PageInfo<ZsDetail> findByZsjust(Integer page, Integer pageSize, Long users) {
		PageHelper.startPage(page, pageSize, true);
        List<ZsDetail> zsDetails = zsDetailMapper.fingByZsjust(users);
		return new PageInfo<>(zsDetails);
	}

}
