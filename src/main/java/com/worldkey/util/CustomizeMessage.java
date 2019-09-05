package com.worldkey.util;

import com.worldkey.entity.Users;
import io.rong.messages.BaseMessage;
import io.rong.util.GsonUtil;

public class CustomizeMessage extends BaseMessage {

    private String content;
    /**
     * 获取操作名。
     */
    private String operation = "";
    /**
     * 附加信息(如果开发者自己需要，可以自己在 App 端进行解析)。
     */
    private String extra = "";
    /**
     * 请求者或者响应者的 UserId。
     */
    private String sourceUserId = "";
    /**
     * 被请求者或者被响应者的 UserId。
     */
    private String targetUserId = "";
    /**
     * 请求或者响应消息。
     */
    private String message = "";

    private Users user;

    private transient static final String TYPE="DB:friend";



    public CustomizeMessage(String operation, String extra, String sourceUserId, String targetUserId, String message) {
        super();
        this.operation = operation;
        this.extra = extra;
        this.sourceUserId = sourceUserId;
        this.targetUserId = targetUserId;
        this.message = message;
    }



    public Users getUser() {
        return user;
    }



    public void setUser(Users user) {
        this.user = user;
    }



    public String getContent() {
        return content;
    }



    public void setContent(String content) {
        this.content = content;
    }



    public CustomizeMessage() {
        super();
    }


    public String getOperation() {
        return operation;
    }



    public void setOperation(String operation) {
        this.operation = operation;
    }



    public String getExtra() {
        return extra;
    }



    public void setExtra(String extra) {
        this.extra = extra;
    }



    public String getSourceUserId() {
        return sourceUserId;
    }



    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }



    public String getTargetUserId() {
        return targetUserId;
    }



    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }



    public String getMessage() {
        return message;
    }



    public void setMessage(String message) {
        this.message = message;
    }



    @Override
    public String getType() {
        return TYPE;
    }



    @Override
    public String toString() {
        return GsonUtil.toJson(this, CustomizeMessage.class);
    }


}