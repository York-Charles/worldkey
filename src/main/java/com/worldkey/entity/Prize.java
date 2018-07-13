package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 抽奖奖品表
 */
@Data
@ToString
@TableName("prize")
public class Prize {
    @TableId("prize_id")
    private Integer prizeId;
    private Integer jd;
    private Integer zs;
    private String msg;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer chance;

}
