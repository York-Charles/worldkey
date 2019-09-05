package com.worldkey.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;
/**
 * 6.7
 * @author 北京都百01
 *
 */
@Data
public class UserRelation {

	private Integer id;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private Integer follower;
	private Integer leader;
	
	public UserRelation() {
		super();
	}
	public UserRelation(Integer id, Date createTime, Integer follower, Integer leader) {
		super();
		this.id = id;
		this.createTime = createTime;
		this.follower = follower;
		this.leader = leader;
	}

	
}
