package com.worldkey.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class History {
	private Integer id;
	private String groupName;
	private Long userId;//被推送（被评论）
	private String userName;
	private String groupImg;
	private String petName;
	private Long information;
	private Date createTime;
	private String info;
	private List<String> giftName;
	private String loginName;
	//0-点赞 1-评论 2-礼物 3-转发 4-小组记录5-评价推送6-评论点赞7-评论回复8-三级回复
	private Integer classify;
	private String headImg;
	private String title;
	private String titleImg;
	private String webUrl;
	private Integer groupId;
	private String commentInfo;//评论内容 点赞时候为被点赞的内容
	private Integer toCommentId;
	private String aCommentInfo;//被评论内容
	private Long toUserId;//评论人ID
	
	private Integer mark;
	private Integer status;
	private Integer praiseNum;
	private String toPetName;
	private String commentCreateTime;

	public History(Integer id, String groupName, Long userId, String userName, String groupImg,
			String petName, Long information, Date createTime, String info, List<String> giftName, String loginName,
			Integer classify, String headImg, String title, String titleImg, String webUrl,Integer groupId,String commentInfo,Integer toCommentId,String aCommentInfo,Long toUserId) {
		super();
		this.id = id;
		this.groupName = groupName;
		this.userId = userId;
		this.userName = userName;
		this.groupImg = groupImg;
		this.petName = petName;
		this.information = information;
		this.createTime = createTime;
		this.info = info;
		this.giftName = giftName;
		this.loginName = loginName;
		this.classify = classify;
		this.headImg = headImg;
		this.title = title;
		this.titleImg = titleImg;
		this.webUrl = webUrl;
		this.groupId = groupId;
		this.commentInfo = commentInfo;
		this.toCommentId = toCommentId;
		this.aCommentInfo = aCommentInfo;
		this.toUserId = toUserId;
	}
	public History() {
		super();
	}

	
	

}
