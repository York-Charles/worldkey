package com.worldkey.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Praise;
import com.worldkey.entity.PraiseApp;

/**
 * @author DB161
 * 定义点赞方法
 */
public interface PraiseService {
    /**
     * 添加赞
     */
    int  addPraise(String token, Integer informationId) throws Exception;

    int addCommentPraise(String token,Integer commentId) throws Exception;
    /**
     *显示赞数
     */
    int   showPraise(Integer informationId)throws Exception;

    /**
     * 点赞状态
     */
    boolean statusPraise(String token, Integer informationId) throws Exception;
    /*
    Praise selectByPrimaryKey(Long praiseId);
    Integer delete(Praise record)throws Exception;
    int selectByPrimaryKeySelective(Long praiseId, Integer information) ;
    int deleteByPrimaryKey(Long praiseId,  Integer information);
    Integer insert(Praise record) throws Exception;
    */
    
    //5.18根据ID查找所有文章的所有操作
    List<PraiseApp> praise(Long userId);

}