package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("zs_detail")
public class ZsDetail {
    @TableId
    private Integer zsId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private  Integer zsNum;
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
    public ZsDetail(){

    }

    public ZsDetail(Date createTime, Integer zsNum, Integer type, Long users) {
        this.createTime = createTime;
        this.zsNum = zsNum;
        this.type = type;
        this.users = users;
    }
    public ZsDetail(Date createTime, Integer zsNum, Integer type, Long users,String msg) {
        this.createTime = createTime;
        this.zsNum = zsNum;
        this.type = type;
        this.users = users;
        this.msg = msg;
    }

    public ZsDetail(Date createTime, Integer zsNum, Integer type, Long users, Long users2, String users2Name) {
        this.createTime = createTime;
        this.zsNum = zsNum;
        this.type = type;
        this.users = users;
        this.users2 = users2;
        this.users2Name = users2Name;
    }
}
