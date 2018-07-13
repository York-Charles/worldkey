package com.worldkey.entity;

public class PraiseApp extends Praise {
	
	private String petName;
	private Long userId;
	private String loginName;
	private String headImg;
	private String title;
	private String titleImg;
	private String webUrl;
	public PraiseApp(String petName, Long userId,String loginName,String headImg,String title,String titleImg,String webUrl) {
		super();
		this.petName = petName;
		this.userId = userId;
		this.loginName = loginName;
		this.headImg = headImg;
		this.title = title;
		this.titleImg = titleImg;
		this.webUrl = webUrl;
	}
	public PraiseApp() {
		super();
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
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleImg() {
		return titleImg;
	}
	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	
	
	

	
}
