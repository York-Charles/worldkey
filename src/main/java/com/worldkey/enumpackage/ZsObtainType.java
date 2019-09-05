package com.worldkey.enumpackage;

public enum ZsObtainType {
    RechargeReward("现金充值", 1),
    giveGift("赠送礼物", 2),
    KbRecharge("Kb充值", 3),
    UsePrizeDraw("抽奖花费", 4),
    GetPrizeDraw("抽奖获得", 5),
    buyGiftBag("购买礼包花费", 6),
    getGiftBag("礼包获得钻石", 7);
    private String name;
    private Integer index;

    ZsObtainType(String name, Integer index) {
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
