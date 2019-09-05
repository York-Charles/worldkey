package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wu
 */
@Data
@ToString
@TableName("browse_addjd")
public class BrowseAddJd {
    @TableId
    private Integer browseAddjdId;
    private Long information;
    private Long users;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String msg;
}
