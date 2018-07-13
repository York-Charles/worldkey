package com.worldkey.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.worldkey.alipay.AliPayService;
import com.worldkey.alipay.AlipayModel;
import com.worldkey.check.alipay.AliPayCheck;
import com.worldkey.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("aliPay")
/*
  @author HP
 */
public class AliPayController {
    @Resource
    private AliPayService aliPayService;
    private Logger log = LoggerFactory.getLogger(AliPayController.class);

    /**
     * 生成订单详细信息的接口
     * APP支付时使用生成签名
     *
     * @param alipayModel AlipayModel的对象  必传参数 body subject totalAmount
     * @return ResultUtil 包含生成的payOrderInfo字符串
     */
    @PostMapping("payOrderInfo")
    public ResultUtil createPayOrderInfo(@Validated(value = AliPayCheck.class) AlipayModel alipayModel, @RequestHeader("host") String host) throws AlipayApiException {
        //回调接口
        alipayModel.setNotifyUrl("http://" + host + "/aliPay/callBack");
        log.error("回调host:http://" + host + "/aliPay/callBack");
        return new ResultUtil(200, "ok", this.aliPayService.createPayOrderInfo(alipayModel));
    }

    /**
     * APP版支付的回调接口
     */
    @RequestMapping("callBack")
    public String callBack(HttpServletRequest request) throws Exception {
        //验证交易状态 交易成功状态
        String trade_success = "TRADE_SUCCESS";
        String trade_status = request.getParameter("trade_status");
        log.error("验证交易状态:" + trade_status);
        boolean a = Objects.equals(trade_success, trade_status);
        if (!a) {
            log.error("支付失败");
            return "支付失败";
        }
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>(16);
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        //支付宝公钥
        String aliPay = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmLU6MbkLKZ7D2C5v37HwkTuKXUPbRXzIeoZXDgjfmTh7u4nQY02n/0S8FbAW6khJMgTVCgrb60wbqoFuWUDweUD/WtDvFuIGHtelGr8w59nztn/mA5ju5G8xjMrY4ffHLqZ6Bsp/IjDL90ajiKQyy/FUiRt2Q+eeD15jqSCEtYDrP8GnNpJ+TaQe63LS5xFHUO+ntkaLQXkYAhK3vP3Y707wpO397riDVqnYBfTOSf/kxhRQLUrPiYzZZL6GUbNaYhRP7XkzrwuZ25Swh9qPWBiBaJ8nq11TCNWLrPb3jif03UPKxKCyfE0mxY4eNcDKR+/mmOGTepaNrudxaaLJbwIDAQAB";
        //是支付宝公钥不是应用公钥  切记切记
        boolean flag = AlipaySignature.rsaCheckV1(params, aliPay, "UTF-8", "RSA2");
        log.error("支付宝返回验证："+flag);
        if (flag) {
            boolean b = this.aliPayService.daShang(params);
            log.error("打赏方法调用："+b);
            if (b) {
                return "success";
            }
        }
        return "支付失败";
    }

    /**
     * 网页版支付接口
     */
    @RequestMapping("pay")
    public void pay(HttpServletRequest request, HttpServletResponse response,
                    Long information, String type, String token,
                    @RequestHeader("host") String host) throws UnsupportedEncodingException {
        if (request.getParameter("WIDout_trade_no") != null) {
            // 商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = request.getParameter("WIDout_trade_no");
            // 订单名称，必填
            String subject = request.getParameter("WIDsubject");
            System.out.println(subject);
            // 付款金额，必填
            String total_amount = request.getParameter("WIDtotal_amount");
            // 商品描述，可空
            String body = token + "," + information + "," + type;
            // 超时时间，可空
            String timeout_express = "2m";
            // 销售产品码 必填
            String product_code = "QUICK_WAP_PAY";
            // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
            //调用RSA签名方式
            AlipayClient client = new DefaultAlipayClient(aliPayService.getUrl(), aliPayService.getAppID(), this.aliPayService.getAppPrivateKey(), "json", "UTF-8", aliPayService.getAlipayPublicKey(), "RSA2");
            AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

            // 封装请求支付信息
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(out_trade_no);
            model.setSubject(subject);
            model.setTotalAmount(total_amount);
            model.setBody(body);
            model.setTimeoutExpress(timeout_express);
            model.setProductCode(product_code);
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl("http://" + host + "/aliPay/callBack");
            // 设置同步地址
            alipay_request.setReturnUrl("http://" + host + "/aliPay/returnUrl");

            // form表单生产
            String form;
            try {
                // 调用SDK生成表单
                form = client.pageExecute(alipay_request).getBody();
                response.setContentType("text/html;charset=UTF-8");
                //直接将完整的表单html输出到页面
                response.getWriter().write(form);
                response.getWriter().flush();
                response.getWriter().close();
                System.out.println(form);
            } catch (AlipayApiException | IOException e) {
                e.printStackTrace();
            }
        }
        //return "pay";
    }

    /**
     * 网页版支付接口的回调，只是做了信息的获取显示，未做数据保存
     */
    @RequestMapping("returnUrl")
    @ResponseBody
    public Map<String, String> returnUrl(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>(16);
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
            System.out.println(name + " : " + valueStr);
        }
        return params;
    }

    /**
     * 支付宝转账接口
     * token 获取用户ID，余额
     * amount 提现的金额
     * payeeAccount 提现的支付宝账户，考虑是否绑定到都百号之后再确定实现方式
     */
    @PostMapping("transfer")
    public ResultUtil transfer(String token, BigDecimal amount, String payeeAccount) throws Exception {
        String transfer = this.aliPayService.transfer(token, amount, payeeAccount);
        if (transfer == null) {
            return new ResultUtil(200, "ok", "失败");
        }
        return new ResultUtil(200, "ok", transfer);
    }
}
