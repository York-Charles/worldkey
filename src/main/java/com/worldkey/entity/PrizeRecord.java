package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ToString
@TableName("prize_record")
public class PrizeRecord {
    @TableId("prize_record_id")
    private Long prizeRecordId;
    private Long users;
    private Integer prizeId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public PrizeRecord(Long users, Integer prizeId, Date createTime) {
        this.users = users;
        this.prizeId = prizeId;
        this.createTime = createTime;
    }

    public PrizeRecord() {
    }
}
