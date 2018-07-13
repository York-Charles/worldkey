package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wu
 * 每日签到实体
 */
@Data
@TableName("sign")
public class Sign {
    @TableId
    private Integer signId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Long users;

    /**
     * 连续签到次数
     */
    private Integer signCount;
}
