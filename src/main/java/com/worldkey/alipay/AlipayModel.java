package com.worldkey.alipay;

import com.worldkey.check.alipay.AliPayCheck;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AlipayModel {

    @NotNull(message = "订单信息不能为空",groups = AliPayCheck.class)
    private String body;
    @NotNull(message = "订单信息不能为空",groups = AliPayCheck.class)
    private String subject;
    @NotNull(message = "金额不能为空",groups = AliPayCheck.class)
    @Pattern(regexp = "^[0-9]+(.[0-9]{1,2})?$",message = "必须为数字",groups = AliPayCheck.class)
    private String totalAmount;

    private String notifyUrl;

    private String timeoutExpress;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }

    @Override
    public String toString() {
        return "AlipayModel{" +
                "body='" + body + '\'' +
                ", subject='" + subject + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", timeoutExpress='" + timeoutExpress + '\'' +
                '}';
    }

}
