package com.worldkey.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Transfer implements Serializable {
    private String outBizNo;

    private Long userId;

    private BigDecimal amount=new BigDecimal(12.23);

    private String payeeType="ALIPAY_LOGONID";

    private String orderId;

    private String payDate;

    private String remark="世界钥匙提现";

    private String payeeAccount="ortktd9043@sandbox.com";
    @DateTimeFormat(pattern = "YYYY:MM:dd HH:mm:ss")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo == null ? null : outBizNo.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate == null ? null : payDate.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount == null ? null : payeeAccount.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public String getJson() {
        return"{    \"out_biz_no\":\""+outBizNo+"\"," +
                "    \"payee_type\":\""+payeeType+"\"," +
                "    \"payee_account\":\""+payeeAccount+"\"," +
                "    \"amount\":\""+amount+"\"," +
                "    \"remark\":\""+remark+"\"" +
                "  }";

        //"{" +
        //        "    \"out_biz_no\":\""+out_biz_no+"\"," +
               /*
                * 收款方账户类型。可取值：  1、ALIPAY_USERID：支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成。
                       2、ALIPAY_LOGONID：支付宝登录号，支持邮箱和手机号格式。
                */
        //        "    \"payee_type\":\"ALIPAY_LOGONID\"," +
                /*
                收款方账户。与payee_type配合使用。付款方和收款方不能是同一个账户。
                 */
         //       "    \"payee_account\":\"ortktd9043@sandbox.com\"," +
         //       "    \"amount\":\"12.23\"," +
               /* "    \"payer_show_name\":\"上海交通卡退款\"," +*//*
        //收款方的真实姓名，可选，若填写会验证帐号和姓名是否匹配
               *//* "    \"payee_real_name\":\"沙箱环境\"," +*/
         //       "    \"remark\":\"世界钥匙提现\"" +
         //       "  }"
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "outBizNo='" + outBizNo + '\'' +
                ", userId=" + userId +
                ", amount=" + amount +
                ", payeeType='" + payeeType + '\'' +
                ", orderId='" + orderId + '\'' +
                ", payDate='" + payDate + '\'' +
                ", remark='" + remark + '\'' +
                ", payeeAccount='" + payeeAccount + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}