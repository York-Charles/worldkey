package com.worldkey.search;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.InformationAll;
import com.worldkey.mapper.SearchMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wu
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Resource
    private SearchMapper searchMapper;

    /**
     * 通过关键字进行搜索
     */
    @Override
    public PageInfo<InformationAll> findByKeyWords(Integer pageNum, Integer pageSize, String keyWords) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<InformationAll> list = this.searchMapper.searchInfo(keyWords);
        return new PageInfo<>(list);
    }
}
