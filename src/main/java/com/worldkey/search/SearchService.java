package com.worldkey.search;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.InformationAll;
/**
 * @author wu
 */
public interface SearchService {
    /**
     * 根据关键字进行查询
     * @param pageNum   页数
     * @param pageSize  每页数量
     * @param keyWords  关键字
     * @return          列表
     */
    PageInfo<InformationAll> findByKeyWords(Integer pageNum, Integer pageSize, String keyWords);
}
