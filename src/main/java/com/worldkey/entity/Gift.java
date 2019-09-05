package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 礼物表单实体
 */
@Data
@TableName("gift")
public class Gift {
    @TableId
    private Integer giftId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String name;
    private String  imgUrl;
    private Integer jdPrice;
    private Integer zsPrice;
    private String  giftImg;
}
