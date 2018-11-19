package com.worldkey.mapper;

import com.worldkey.entity.Comment;
import com.worldkey.entity.Praise;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    Comment selectByPrimaryKey1(Long commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectByInformationOrderByIdDesc(Long information);

    @Update("UPDATE `comment` SET reply_count=reply_count+1 " +
            "WHERE comment_id=#{comment}")
    void addReplyCount(Long comment);
    
    
    @Select("SELECT comment FROM comment WHERE comment_id = #{comment}")
    Long comment(Long comment);
    
    
    @Select("select " +
            "c.comment_id,c.info,c.users,c.gmt_create,c.comment,c.reply_count,p.praise_num AS praiseNum "  +
            "from comment AS c " +
            "left join praise_comment_num AS p on p.comment = c.comment_id " +
            "where c.comment = #{comment} " +
            "AND type=1")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByComment(Long comment);
    
    @Select("select " +
            "c.comment_id,c.info,c.users,c.gmt_create,c.comment,c.reply_count,p.praise_num AS praiseNum "  +
            "from comment AS c " +
            "left join praise_comment_num AS p on p.comment = c.comment_id " +
            "where c.comment = #{comment} " +
            "AND type=2")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByComment1(Long comment);
    
    
    
    @Select(" SELECT  c.comment_id,c.info,c.users,c.gmt_create,c.comment,c.type,c.reply_count,p.praise_num AS praiseNum,u.pet_name AS toPetName  FROM `comment` as c LEFT JOIN "+
    		" (SELECT cc.comment_id,cc.users FROM comment as cc WHERE cc.comment=#{comment}) AS ccc "+
    		" ON ccc.comment_id = c.comment "+
    		" LEFT JOIN praise_comment_num AS p on p.comment = c.comment_id "+
    		" left join users as u on u.id = ccc.users "+
    		" WHERE c.comment=#{comment} or c.comment = ccc.comment_id order by c.gmt_create desc") 
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> getReplyTime(Long comment);
    
    @Select(" SELECT  c.comment_id,c.info,c.users,c.gmt_create,c.comment,c.type,c.reply_count,p.praise_num AS praiseNum,u.pet_name AS toPetName   FROM `comment` as c LEFT JOIN "+
    		" (SELECT cc.comment_id,cc.users FROM comment as cc WHERE cc.comment=#{comment}) AS ccc "+
    		" ON ccc.comment_id = c.comment "+
    		" LEFT JOIN praise_comment_num AS p on p.comment = c.comment_id "+
    		" left join users as u on u.id = ccc.users "+
    		" WHERE c.comment=#{comment} or c.comment = ccc.comment_id order by praiseNum desc") 
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> getReplyPraise(Long comment);
   
    @Select("select * from comment ORDER BY comment_id desc LIMIT 1")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectMaxComment();
    

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
    @Select("select comment_id,info,users,gmt_create,comment.comment,comment.reply_count,praise_comment_num.praise_num AS praiseNum from comment left join praise_comment_num on praise_comment_num.comment = comment.comment_id where comment.type = 0 AND comment.information = #{information} ORDER BY gmt_create DESC")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByInformationOrderByIdDesc1(Long information);
    //按点赞量
    @Select("select comment.comment_id,comment.info,comment.users,comment.gmt_create,comment.comment,comment.reply_count,praise_comment_num.praise_num AS praiseNum from comment left join praise_comment_num on praise_comment_num.comment = comment.comment_id where comment.type = 0 AND comment.information = #{information} ORDER BY praise_comment_num.praise_num DESC")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByInformationOrderByIdDesc2(Long information);
    
    @Select("select MAX(comment_id) from comment")
    Long selectCommentIds();
    
    @Select("select MAX(gmt_create) from comment")
    Date s();
    
    
    @Select("select " +
            "c.comment_id,c.info,c.users,c.gmt_create,c.comment,c.reply_count,p.praise_num AS praiseNum "  +
            "from comment AS c " +
            "left join praise_comment_num AS p on p.comment = c.comment_id " +
            "where c.comment = #{comment} " +
            "AND type=1 " +
            "order by c.gmt_create desc "+
            "limit 1")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByComments(Long comment);
    
    @Select("select " +
            "c.comment_id,c.info,c.users,c.gmt_create,c.comment,c.reply_count,p.praise_num AS praiseNum "  +
            "from comment AS c " +
            "left join praise_comment_num AS p on p.comment = c.comment_id " +
            "where c.comment = #{comment} " +
            "AND type=1 " +
            "order by praiseNum desc "+
            "limit 1")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> selectByCommentsPraise(Long comment);
    
    
    @Select("select * from comment where comment_id = #{comment}")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> select1(Long comment);
    
    @Select("select info,users from comment ORDER BY comment_id desc LIMIT 1")
    @ResultMap("com.worldkey.mapper.CommentMapper.BaseResultMap")
    List<Comment> select2();
    
    
    @Select("select status from praise_comment where users=#{userId} and comment=#{comment}")
    Integer status(@Param("userId")Long userId,@Param("comment")Long comment);
    
    @Select("select praise_num from praise_comment_num where comment=#{comment}")
    Integer praiseNum(Long comment);
    
    @Select("select comment_id from comment where information=#{id}")
    List<Long> selectInformation(Long id);
}