package com.worldkey.mapper;

import com.worldkey.entity.Comment;
import com.worldkey.entity.Praise;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author HP
 */
@Repository
public interface CommentMapper {

    int deleteByPrimaryKey(Long commentId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long commentId);
    
    @Select(" select "+
    			"comment_id, information, info,type, users, gmt_create, author, comment , reply_count "+
    			"from comment "+
    			"where comment_id = #{commentId,jdbcType=BIGINT}")	
    Comment selectByPrimaryKey1(Long commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectByInformationOrderByIdDesc(Long information);

    @Update("UPDATE `comment` SET reply_count=reply_count+1 " +
            "WHERE comment_id=#{comment}")
    void addReplyCount(Long comment);
    @Select("select " +
            "comment_id,info,users,gmt_create,comment.comment,comment.reply_count,praise_comment_num.praise_num AS praiseNum" +
            "from comment " +
            "left join praise_comment_num on praise_comment_num.comment = comment.comment " +
            "where comment.comment = #{comment} " +
            "AND type=1")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByComment(Long comment);
    
    @Select("select " +
            "comment_id,info,users,gmt_create,comment.comment，comment.reply_count,praise_comment_num.praise_num AS praiseNum" +
            "from comment " +
            "left join praise_comment_num on praise_comment_num.comment = comment.comment " +
            "where comment.comment = #{comment} " +
            "AND type=2")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByComment1(Long comment);

    List<Comment> selectByCommentWithReply(Long comment);
    
    
  //5.18
    @Select("SELECT comment.information,comment.gmt_create,comment.info "+
    		"from comment "+
    		"INNER JOIN information_all on comment.information = information_all.id  "+
    		"INNER JOIN users ON users.id = information_all.users "+
    		"WHERE users.id=#{userId} order by comment.gmt_create desc")
    List<Comment> Comment(Long userId);
    
    @Select("select comment.users from comment "+
    		"INNER JOIN information_all on comment.information = information_all.id "+
    		"where information_all.users=#{userId} order by comment.gmt_create desc")
    List<Long> chooseUsers(Long userId);
    
    
    //7.4
    @Delete("delete from comment where comment_id=#{commentId} or comment=#{commentId}")
    int delete(Long commentId);
    
    @Select("select comment_id from `comment` where comment in (SELECT comment_id FROM `comment` WHERE `comment` =#{commentId})")
    List<Long> selectCommentId(Long commentId);
    
    @Delete("delete from comment where comment_id=#{commentId}")
    int delete2(Long commentId);
    
    //按时间
    @Select("select comment_id,info,users,gmt_create,comment.comment,comment.reply_count,praise_comment_num.praise_num AS praiseNum from comment left join praise_comment_num on praise_comment_num.comment = comment.comment_id where comment.type = 0 AND comment.information = #{information} ORDER BY comment_id DESC")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByInformationOrderByIdDesc1(Long information);
    //按点赞量
    @Select("select comment_id,info,users,gmt_create,comment.comment,comment.reply_count,praise_comment_num.praise_num AS praiseNum from comment left join praise_comment_num on praise_comment_num.comment = comment.comment_id where comment.type = 0 AND comment.information = #{information} ORDER BY praise_comment_num.praise_num DESC")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByInformationOrderByIdDesc2(Long information);

}