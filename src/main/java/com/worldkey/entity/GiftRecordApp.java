package com.worldkey.entity;

import java.util.Date;
import java.util.List;

public class GiftRecordApp extends GiftRecord{
	
	private String petName;
	private Long userId;
	private List<String> giftName;
	private String loginName;
	
	public GiftRecordApp() {

	}


	public GiftRecordApp(Date createTime, Long users, Long toInformation, Integer giftId, String petName, Long userId,List<String> giftName,String loginName) {
		super(createTime, users, toInformation, giftId);
		this.petName = petName;
		this.userId = userId;
		this.giftName = giftName;
		this.loginName = loginName;
	}


	public List<String> getGiftName() {
		return giftName;
	}


	public void setGiftName(List<String> giftName) {
		this.giftName = giftName;
	}


	public String getPetName() {
		return petName;
	}


	public void setPetName(String petName) {
		this.petName = petName;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}




}
