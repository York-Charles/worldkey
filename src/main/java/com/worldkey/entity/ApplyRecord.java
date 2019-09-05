package com.worldkey.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.ToString;

/**
 * 乐赠模块交易记录实体
 */
@Data
@ToString
@TableName("apply_record")
public class ApplyRecord {
	@TableId
	private Integer applyRecordId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private Long usersSeller;
	private Long information;
	private Long usersBuyer;
	private String msg;
	private String headImg;
	private String petName;

}
