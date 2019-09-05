package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.JdDetail;
import com.worldkey.mapper.JdDetailMapper;
import com.worldkey.service.JdDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JdDetailServiceImpl extends ServiceImpl<JdDetailMapper, JdDetail> implements JdDetailService {
    @Resource
    private JdDetailMapper jdDetailMapper;

    @Override
    public List<JdDetail> findByUsers(Long users) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("users", users);
        return jdDetailMapper.selectByMap(map);
    }

    @Override
    public PageInfo<JdDetail> findByUid(Integer page, Integer pageSize, Long users) {
        PageHelper.startPage(page, pageSize, true);
        List<JdDetail> jdDetails = jdDetailMapper.findByUid(users);
        return new PageInfo<>(jdDetails);
    }
    
    //<---以下方法---薛秉承--2018.4.11写入-->
    @Override
	public PageInfo<JdDetail> findByjust(Integer page, Integer pageSize, Long users) {
		PageHelper.startPage(page, pageSize, true);
        List<JdDetail> jdDetails = jdDetailMapper.findByjust(users);
		return new PageInfo<>(jdDetails);
	}
    
    
    
}
