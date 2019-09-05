package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("jd_detail")
public class JdDetail {
    @TableId
    private Integer jdId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private  Integer jdNum;
    private Integer type;
    private String msg;
    /**
     * 当前操作的人
     */
    private Long users;
    /**
     *  当前操作关联的第二人，如邀请好友是所邀请的人
     */
    private Long users2;
    private String users2Name;

    public JdDetail(Date createTime, Integer jdNum, Integer type, Long users,Long users2,String users2Name) {
        this.createTime = createTime;
        this.jdNum = jdNum;
        this.type = type;
        this.users = users;
        this.users2 = users2;
        this.users2Name = users2Name;
    }
    public JdDetail(Date createTime, Integer jdNum, Integer type, Long users,Long users2,String users2Name,String msg) {
        this.createTime = createTime;
        this.jdNum = jdNum;
        this.type = type;
        this.users = users;
        this.users2 = users2;
        this.users2Name = users2Name;
        this.msg = msg;
    }
    public JdDetail(Date createTime, Integer jdNum, Integer type, Long users) {
        this.createTime = createTime;
        this.jdNum = jdNum;
        this.type = type;
        this.users = users;
    }

    public JdDetail(Date createTime, Integer jdNum, Integer type, String msg, Long users) {
        this.createTime = createTime;
        this.jdNum = jdNum;
        this.type = type;
        this.msg = msg;
        this.users = users;
    }

    public JdDetail() {
    }
}
