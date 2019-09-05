package com.worldkey.entity;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class FriendExample implements Serializable {
	private Integer sex;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;

	private String headImg;

	private String telNum;
	private String email;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createDate;
	private String petName;
	private String note;
	private String loginName;
	private String signature;
	
	private Long userId;

	public FriendExample(Integer sex, Date birthday, String headImg, String telNum, String email, Date createDate,
			String petName, String note, String loginName,String signature) {
		super();
		this.sex = sex;
		this.birthday = birthday;
		this.headImg = headImg;
		this.telNum = telNum;
		this.email = email;
		this.createDate = createDate;
		this.petName = petName;
		this.note = note;
		this.loginName = loginName;
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public FriendExample() {
		super();
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
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

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
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
		return "FriendExample [sex=" + sex + ", birthday=" + birthday + ", headImg=" + headImg + ", telNum=" + telNum
				+ ", email=" + email + ", createDate=" + createDate + ", petName=" + petName + ", note=" + note
				+ ", loginName=" + loginName + "]";
	}

	 

}
