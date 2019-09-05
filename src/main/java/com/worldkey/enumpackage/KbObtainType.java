package com.worldkey.enumpackage;

public enum KbObtainType {
    getGift("收礼物",1),PurchaseZs("购买钻石",2);
    private  String name;
    private Integer index;

    KbObtainType(String name, Integer index) {
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
