package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("gift_record")
public class GiftRecord {
    @TableId
    private Integer giftRecordId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Long users;
    private Long toInformation;
    private Integer giftId;

    public GiftRecord(Date createTime, Long users, Long toInformation, Integer giftId) {
        this.createTime = createTime;
        this.users = users;
        this.toInformation = toInformation;
        this.giftId = giftId;
    }

	public GiftRecord() {

	}


    
}
