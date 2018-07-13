package com.worldkey.service;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.BaseShow;
import com.worldkey.entity.BrowsingHistory;
import com.worldkey.entity.InformationAll;
import org.apache.mahout.cf.taste.impl.model.BooleanPreference;

import java.util.List;
import java.util.Set;

public interface BrowsingHistoryService {
    List<BrowsingHistory> findAll();
    int insert(BrowsingHistory vo);

    /**
     * 通过user获取User浏览过的item的ID
     * @param user 要获取item的user的ID
     * @return 该User的浏览过的Item集合
     */
    List<BooleanPreference>findByUser(Long user);

    /**
     *
     * @return 浏览过item的用户的集合
     */
    Set<Long>findUsers();


    PageInfo<InformationAll> tuijian(Long userID, Integer pageNum, Integer pageSize) throws Exception;
    PageInfo<BaseShow> recommendation(Long userID, Integer pageNum, Integer pageSize) throws Exception;

    BrowsingHistory findByUserAndItem(Long itemID, Long userID);
}