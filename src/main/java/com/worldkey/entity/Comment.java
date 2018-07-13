package com.worldkey.entity;

import com.worldkey.check.comment.Reply;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author HP
 */
@Data
public class Comment implements Serializable {
    private Long commentId;
    /**
     * 评论的主题的ID
     */
    @NotNull(message = "不能为空",groups = {com.worldkey.check.comment.Comment.class})
    @Min(value = 1, message = "不能为负值")
    private Long information;
    /**
     * 评论或回复的主体
     */
    @NotNull(message = "不能为空",groups = {com.worldkey.check.comment.Comment.class,Reply.class})
    @Size(max = 255, message = "长度小于255")
    private String info;

    /**
     * 做出评论或回复的用户
     */
    private Users users;
    

    private Date gmtCreate;

    /**
     * 评论时对应的主题的作者ID
     */
    private Long author;

    /**
     * 记录类型 0为评论，1为回复 默认为0
     */
    private Byte type =0;
    /**
     * 回复对应的评论的ID
     */
    @NotNull(message = "不能为空",groups = Reply.class)
    private Long comment;
    /**
     * 评论回复的总数量
     */
    private Integer replyCount;
    
    private Integer praiseNum;
    


    public Comment() {
		super();
	}

	public Comment(Long commentId, Long information, String info, Users users, Date gmtCreate, Long author, Byte type,
			Long comment, Integer replyCount, Integer praiseNum, List<Comment> replys) {
		super();
		this.commentId = commentId;
		this.information = information;
		this.info = info;
		this.users = users;
		this.gmtCreate = gmtCreate;
		this.author = author;
		this.type = type;
		this.comment = comment;
		this.replyCount = replyCount;
		this.praiseNum = praiseNum;
		this.replys = replys;
	}


	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}

	private List<Comment> replys;

    private static final long serialVersionUID = 1L;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getInformation() {
        return information;
    }

    public void setInformation(Long information) {
        this.information = information;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public List<Comment> getReplys() {
        return replys;
    }

    public void setReplys(List<Comment> replys) {
        this.replys = replys;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", information=" + information +
                ", info='" + info + '\'' +
                ", users=" + users +
                ", gmtCreate=" + gmtCreate +
                ", author=" + author +
                ", type=" + type +
                ", comment=" + comment +
                ", replyCount=" + replyCount +
                ", replys=" + replys +
                '}';
    }
}