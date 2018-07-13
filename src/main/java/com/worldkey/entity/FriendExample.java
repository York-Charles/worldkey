package com.worldkey.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class FriendExample implements Serializable {
	private Integer sex;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;

	private String headimg;

	private String telNum;
	private String email;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createDate;
	private String usersname;
	private String note;
	private String usersId;

	public FriendExample(Integer sex, Date birthday, String headImg, String telNum, String email, Date createDate,
			String usersname, String note, String usersId) {
		super();
		this.sex = sex;
		this.birthday = birthday;
		this.headimg = headImg;
		this.telNum = telNum;
		this.email = email;
		this.createDate = createDate;
		this.usersname = usersname;
		this.note = note;
		this.usersId = usersId;
	}

	public FriendExample() {
		super();
	}

	public String getUsersname() {
		return usersname;
	}

	public void setUsersname(String usersname) {
		this.usersname = usersname;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUsersId() {
		return usersId;
	}

	public void setUsersId(String usersId) {
		this.usersId = usersId;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headImg) {
		this.headimg = headImg;
	}

	public String getTelNum() {
		return telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "FriendExample [sex=" + sex + ", birthday=" + birthday + ", headImg=" + headimg + ", telNum=" + telNum
				+ ", email=" + email + ", createDate=" + createDate + ", usersname=" + usersname + ", note=" + note
				+ ", usersId=" + usersId + "]";
	}

	 

}
