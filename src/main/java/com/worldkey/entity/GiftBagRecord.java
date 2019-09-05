package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wu
 */
@Data
@ToString
@TableName("gift_bag_record")
public class GiftBagRecord implements Serializable {
    @TableId
    private Integer giftBagRecordId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 购买礼包用户的id
     */
    private Long users;

    /**
     * 对应的被购买的礼包的id
     */
    private Integer giftBagId;


    public GiftBagRecord(Long users, Integer giftBagId, Date createTime) {
        this.users = users;
        this.giftBagId = giftBagId;
        this.createTime = createTime;
    }

    /**
     * 无参构造函数
     */

    public GiftBagRecord() {

    }
}
