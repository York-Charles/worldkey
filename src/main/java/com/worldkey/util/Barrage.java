package com.worldkey.util;

import io.rong.messages.BaseMessage;
import io.rong.util.GsonUtil;

public class Barrage extends BaseMessage {

    private String message;
    private String icon;
    private String content;
    private transient static final String TYPE = "DB:barrage";

    public Barrage(String message, String icon) {
        this.message = message;
        this.icon = icon;
    }

    public Barrage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this, Barrage.class);
    }
}
