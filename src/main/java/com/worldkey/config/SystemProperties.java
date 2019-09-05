package com.worldkey.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "money")
public class SystemProperties {
    /**
     * 注册奖励金豆数
     */
    private Integer regRewardJd;
    /**
     * 邀请好友奖励金豆数
     */
    private Integer invitedRewardJd;
    /**
     * 每天发布奖励金豆可获得奖励的次数
     */
    private Integer releaseAwardsFrequency;
    /**
     * 每天发布获得金豆奖励时每次获得的金豆奖励数
     */
    private Integer releaseAwardsJd;
    private Integer jd2kb;
    private Integer zs2kb;
    private Integer kb2zs;
    private Integer prizeDraw;
    private Integer rmb2zs;

    /**
     * 每日签到奖励金豆数
     */
    private Integer dailySignJd;

    /**
     * 新手礼包获得钻石数量，新手礼包获得金豆数量
     * 新手礼包钻石 noviceGiftBagZs
     * 新手礼包金豆 noviceGiftBagJd
     */
    private Integer noviceGiftBagZs;
    private Integer noviceGiftBagJd;

    /**
     * 周礼包钻石和金豆数量
     * 周礼包钻石数量 weekGiftBagZs
     * 周礼包金豆数量 weekGiftBagJd
     */
    private Integer weekGiftBagZs;
    private Integer weekGiftBagJd;

    /**
     * 新春礼包A获得金豆数量
     */
    private Integer activityGiftBagJdA;
    /**
     * 新春礼包B获得金豆数量
     */
    private Integer activityGiftBagJdB;

    /**
     * 转发文章每人每次获得 2金豆
     */
    private Integer transpondJd;
    /**
     * 浏览，每人每天获得3金豆 每天一次
     */
    private Integer browseJd;

}
