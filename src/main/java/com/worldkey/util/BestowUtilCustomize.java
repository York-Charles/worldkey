package com.worldkey.util;

import io.rong.messages.BaseMessage;
import io.rong.util.GsonUtil;

import java.io.Serializable;

public class BestowUtilCustomize extends BaseMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer code;
    private String msg;
    private Object result;
    private String content;
    private transient static final String TYPE = "DB:Bestow";

    public BestowUtilCustomize(Integer code, String msg, Object result) {
        super();
        this.code = code;
        this.msg = msg;
        this.result = result;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Object getResult() {
        return result;
    }
    public void setResult(Object result) {
        this.result = result;
    }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this,BestowUtilCustomize.class);
    }
}
