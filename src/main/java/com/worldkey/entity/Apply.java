package com.worldkey.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.ToString;

/**
 * 乐赠模块 申请表实体
 */
@Data
@ToString
@TableName("apply")
public class Apply {
	@TableId
	private Integer applyId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private Long users;
	private Long information;
	private Integer status;
	private String headImg;
	private Integer checked;
}
