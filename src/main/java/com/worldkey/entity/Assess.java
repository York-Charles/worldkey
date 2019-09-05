package com.worldkey.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Assess {
	private Integer id;
	private Integer userId;//被评论
	private Integer assessId;//评论
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String conTent;
	private String petName;
	private String headImg;
	private String time;
 	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getAssessId() {
		return assessId;
	}
	public void setAssessId(Integer assessId) {
		this.assessId = assessId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getConTent() {
		return conTent;
	}
	public void setConTent(String conTent) {
		this.conTent = conTent;
	}
	
	public String getPetName() {
		return petName;
	}
	public void setPetName(String petName) {
		this.petName = petName;
	}
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public Assess(Integer id, Integer userId, Integer assessId, Date createTime, String conTent,String petName) {
		super();
		this.id = id;
		this.userId = userId;
		this.assessId = assessId;
		this.createTime = createTime;
		this.conTent = conTent;
		this.petName = petName;
	}
	public Assess() {
		super();
	}
	
	
	

}
