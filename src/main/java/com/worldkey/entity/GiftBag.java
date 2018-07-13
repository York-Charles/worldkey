package com.worldkey.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wu
 */
@Data
@TableName("gift_bag")
public class GiftBag {
    @TableId
    private Integer giftBagId;
    /**
     * 礼包的创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 礼物名字
     */
    private String name;
    /**
     * 图片链接
     */
    private String imgUrl;

    /**
     * jdNum,zsNum,kbNum,
     * 分别表示为礼包内部包含的金豆，钻石，K币数量
     */
    private Integer jdNum;
    private Integer zsNum;
    private Integer kbNum;

    /**
     * jdPrice,zsPrice,kbPrice,rmbPrice,
     * 分别表示购买礼包需要支付的货币数量。
     */
    private Integer jdPrice;
    private Integer zsPrice;
    private Integer kbPrice;
    private BigDecimal rmbPrice;


    /**
     * 活动礼包开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    /**
     * 活动礼包结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date overTime;

    /**
     * 礼包备注
     */
    private String notes;



}
