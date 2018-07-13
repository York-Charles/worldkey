package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wu
 * 分享文章记录实体
 */
@Data
@ToString
@TableName("share_info_record")
public class ShareInfoRecord {
    @TableId
    private Integer shareInfoId;
    private Long users;
    private Long information;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
