package com.worldkey.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
/**
 * 基础展示模块
 *
 */
@Data
public class BaseShow {
    private  String title;
    private  String titleImg;
    private  String info;
    private  String author;
    private String webUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    private  Integer praiseNum=0;
    private  Integer commentNum=0;
    private  Long id;
    private  Long usersId;
    private String loginName;
    private String headImg;
    private Integer applyRecordId;
    private Integer imgNum;
    private Integer isPraise;
    private Integer competitive;
    private String petName;
}
