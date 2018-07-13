package com.worldkey.service.impl;

import com.worldkey.entity.TwoType;
import com.worldkey.mapper.TwoTypeMapper;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.TwoTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class TwoTypeServiceImpl implements TwoTypeService {
    @Resource
    private TwoTypeMapper twoTypeMapper;
    @Resource
    private InformationAllService informationAllService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer del(Integer id) {
        return this.twoTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer update(TwoType two) {
        return this.twoTypeMapper.updateByPrimaryKeySelective(two);
    }

    @Override
    public List<TwoType> findByOne(Integer id) {
        return this.twoTypeMapper.selectByOne(id);
    }

    /**
     * 通过Id获取
     */
    @Override
    public TwoType findById(Integer id) {
        return this.twoTypeMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取全部二级分类
     */
    @Override
    public List<TwoType> findAll() {
        return this.twoTypeMapper.selectAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer add(TwoType two) {
        return this.twoTypeMapper.insertSelective(two);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer replace(Integer id, Integer replace) {
        int c = this.twoTypeMapper.replaceId(id, replace);
        informationAllService.replaceType(id, replace);
        log.debug("替换的TypeID  id:{}--replace:{}",id,replace);
        return c;
    }

    @Override
    public TwoType findByTypeName(String typeName) {
        return  this.twoTypeMapper.selectByTypeName(typeName);
    }

	@Override
	public TwoType findByThree(Integer id) {
		// TODO Auto-generated method stub
		return this.twoTypeMapper.selectByThreeType(id);
	}


}
