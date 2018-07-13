package com.worldkey.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class GiftUsers {
	private Integer id;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	private Long users;
    private Long toUsers;
    private Integer giftId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUsers() {
		return users;
	}
	public void setUsers(Long users) {
		this.users = users;
	}
	public Long getToUsers() {
		return toUsers;
	}
	public void setToUsers(Long toUsers) {
		this.toUsers = toUsers;
	}
	public Integer getGiftId() {
		return giftId;
	}
	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}
	public GiftUsers( Date createTime, Long users, Long toUsers, Integer giftId) {
		super();
		this.createTime = createTime;
		this.users = users;
		this.toUsers = toUsers;
		this.giftId = giftId;
	}
	public GiftUsers() {
		super();
	}
    

}
