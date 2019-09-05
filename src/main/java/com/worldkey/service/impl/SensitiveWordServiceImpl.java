package com.worldkey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.SensitiveWord;
import com.worldkey.mapper.SensitiveWordMapper;
import com.worldkey.service.SensitiveWordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {
    @Resource
    private SensitiveWordMapper sensitiveWordMapper;

    @Override
    public PageInfo<SensitiveWord> search(Integer pageNum, Integer pageSize, String search) {
        PageHelper.startPage(pageNum,pageSize,true);
        return new PageInfo<>(sensitiveWordMapper.search(search));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer del(Integer id) {
        return sensitiveWordMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer insertStopWord(SensitiveWord word) {
        word.setIsWord(false);
        return sensitiveWordMapper.insertSelective(word);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer insertWord(SensitiveWord word) {
        word.setIsWord(true);
        return sensitiveWordMapper.insertSelective(word);
    }
}
