package com.worldkey.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Comment;
import com.worldkey.entity.CommentApp;
import com.worldkey.entity.PraiseApp;

public interface CommentService {

    int deleteByPrimaryKey(Long commentId, Integer information);

    Integer insert(Comment record) throws Exception;

    /*int insertSelective(Comment record);
    int updateByPrimaryKeySelective(Comment record);
    int updateByPrimaryKey(Comment record);*/

    Comment selectByPrimaryKey(Long commentId);

    PageInfo<Comment> selectByInformationOrderByIdDesc(Long id, Integer pageNum, Integer pageSize);
    PageInfo<Comment> selectByInformationOrderByIdDesc1(Long id, Integer pageNum, Integer pageSize,String token);
    PageInfo<Comment> selectByInformationOrderByIdDesc2(Long id, Integer pageNum, Integer pageSize,String token);

    /**
     * 添加回复
     */
    int addReply(Comment comment, String token) throws Exception;
    
    int addReply1(Comment comment, String token) throws Exception;

    PageInfo getReply(Long comment, Integer pageNum, Integer pageSize);
    
    PageInfo getReply1(Long comment, Integer pageNum, Integer pageSize);
    
    PageInfo getReplyTime(Long comment, Integer pageNum, Integer pageSize,String token);
    
    PageInfo getReplyPraise(Long comment, Integer pageNum, Integer pageSize,String token);
    
    
    //5.18根据ID查找所有文章的所有操作
    List<CommentApp> comment(Long userId);
    
    int deleteComment(Long comment);
    
    int deleteCommentHistory(Long id);

}
