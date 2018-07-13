package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Data
@TableName("kb_detail")
public class KbDetail {
    @TableId
    private Integer kbId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private BigDecimal kbNum;
    private Integer type;
    /**
     * 当前操作的人
     */
    private Long users;
    /**
     *  当前操作关联的第二人，如送礼物时收到礼物的人
     */
    private Long users2;
    private String users2Name;
    private String msg;
    public KbDetail(Date createTime, BigDecimal kbNum, Integer type, Long users, Long users2, String users2Name) {
        this.createTime = createTime;
        this.kbNum = kbNum;
        this.type = type;
        this.users = users;
        this.users2 = users2;
        this.users2Name = users2Name;
    }

    public KbDetail(Date createTime, BigDecimal kbNum, Integer type, Long users) {
        this.createTime = createTime;
        this.kbNum = kbNum;
        this.type = type;
        this.users = users;
    }

    public KbDetail(Date createTime, BigDecimal kbNum, Integer type, Long users, Long users2, String users2Name, String msg) {
        this.createTime = createTime;
        this.kbNum = kbNum;
        this.type = type;
        this.users = users;
        this.users2 = users2;
        this.users2Name = users2Name;
        this.msg = msg;
    }

    public KbDetail() {
    }
}
