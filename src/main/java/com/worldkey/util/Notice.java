package com.worldkey.util;

import io.rong.messages.BaseMessage;
import io.rong.util.GsonUtil;

public class Notice extends BaseMessage {

    private String message;
    private String content;
    private transient static final String TYPE = "DB:notice";

    public Notice(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        return GsonUtil.toJson(this,Notice.class);
    }
}
