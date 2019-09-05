package com.worldkey.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
@Data
public class PraiseComment implements Serializable{
	/**
     * 点赞实体ID
     */
    private Long id;

    /**
     * 点赞用户
     */
    private Users users;

    /**
     * 被点赞文章
     */
    private Integer comment;

    /**
     * 点赞状态 0为未点赞，1为已点赞。默认为0
     */
    private Integer status;


    /**
     * 点赞时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

	public PraiseComment(Long id, Users users, Integer comment, Integer status, Date createTime) {
		super();
		this.id = id;
		this.users = users;
		this.comment = comment;
		this.status = status;
		this.createTime = createTime;
	}

	public PraiseComment() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Integer getComment() {
		return comment;
	}

	public void setComment(Integer comment) {
		this.comment = comment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    

}
