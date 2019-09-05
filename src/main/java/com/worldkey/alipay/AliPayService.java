package com.worldkey.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author HP
 */
public interface AliPayService {
    /**
     * 支付宝转账接口
     */
    String transfer(String token, BigDecimal amount, String payeeAccount) throws Exception;

    /**
     * alipay初始化
     * @return
     */
    AlipayClient getAlipayClient();


    String createPayOrderInfo(AlipayModel model) throws AlipayApiException;

    boolean daShang(Map<String, String> params) throws Exception;
    String getAppID();

    void setAppID(String appID);

    String getAppPrivateKey();

    void setAppPrivateKey(String appPrivateKey);

    String getAlipayPublicKey();

    void setAlipayPublicKey(String alipayPublicKey);

    String getCharSet();

    void setCharSet(String charSet);



    String getUrl();

    void setUrl(String url);
}
