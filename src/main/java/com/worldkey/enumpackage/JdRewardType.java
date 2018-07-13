package com.worldkey.enumpackage;

public enum JdRewardType {
    regReward("注册奖励金豆", 1),
    invitedReward("邀请奖励金豆", 2),
    releaseReward("发布文章奖励金豆", 3),
    giveGift("赠送礼物", 4),
    PrizeDraw("抽奖获得", 5),
    giftBag("礼包获得金豆", 6),
    signReward("签到奖励金豆", 7),
    shareInfo("转发奖励金豆", 8),
    browseAddJd("浏览获得金豆", 9);

    private String name;
    private Integer index;

    JdRewardType(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
