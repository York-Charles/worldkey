package com.worldkey.alipay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.worldkey.alipay.AliPayService;
import com.worldkey.alipay.AlipayModel;
import com.worldkey.config.SystemProperties;
import com.worldkey.entity.*;
import com.worldkey.enumpackage.ZsObtainType;
import com.worldkey.exception.Z406Exception;
import com.worldkey.mapper.TransferMapper;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.TradingRecordService;
import com.worldkey.service.UsersService;
import com.worldkey.service.ZsDetailService;
import io.rong.util.GsonUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@ConfigurationProperties(prefix = "alipay")
@Data
public class AliPayServiceImpl implements AliPayService {
    private static Logger log = LoggerFactory.getLogger(AliPayServiceImpl.class);
    /**
     * 应用的APP_ID
     */
    private String appID;
    /**
     * 应用的APP_PRIVATE_KEY
     */
    private String appPrivateKey;
    /**
     * 应用的ALIPAY_PUBLIC_KEY
     */
    private String alipayPublicKey;
    /**
     * 应用的CHARSET
     */
    private String charSet;

    private String returnUrl;
    /**
     * alipay服务网关
     */
    private String url;
    /**
     * 打赏的佣金收取比例
     */
    private double brokerage;
    @Resource
    private UsersService usersService;
    @Resource
    private TradingRecordService tradingRecordService;
    @Resource
    private InformationAllService informationAllService;
    @Resource
    private TransferMapper transferMapper;
    @Resource
    private SystemProperties systemProperties;
    @Resource
    private ZsDetailService zsDetailService;

    /**
     * 转账  提现功能
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String transfer(String token, BigDecimal amount, String payeeAccount) throws Exception {
        //提现的用户的ID，测试写死，生产环境以token获取   获取当前用户
        Users byToken = usersService.findByToken(token);
        //未登录
        if (byToken == null) {
            throw new Exception("用户未登录");
        }
        AlipayClient alipayClient = this.getAlipayClient();
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String outBizNo = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        Transfer transfer = new Transfer();
        transfer.setAmount(amount.setScale(2, BigDecimal.ROUND_DOWN));
        transfer.setOutBizNo(outBizNo);
        transfer.setRemark("世界钥匙提现");
        //"ortktd9043@sandbox.com"  提现的支付宝帐号，可以是邮箱或手机号
        transfer.setPayeeAccount(payeeAccount);
        request.setBizContent(transfer.getJson());
        AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
        log.error("转账结果:"+GsonUtil.toJson(response,response.getClass()));
        //调用查询接口逻辑  掉单
        if (Objects.equals(response.getCode(), "40004") || Objects.equals(response.getCode(), "20000") || Objects.equals("SYSTEM_ERROR", response.getSubCode())) {
            AlipayFundTransOrderQueryRequest requestQuery = new AlipayFundTransOrderQueryRequest();
            request.setBizContent("{\"out_biz_no\":\"" + outBizNo + "\",}");
            AlipayFundTransOrderQueryResponse responseQuery = alipayClient.execute(requestQuery);
            if (responseQuery.isSuccess() && responseQuery.getOrderId() != null) {
                response = alipayClient.execute(request);

            } else {
                log.error("alipay单笔转账掉单查询失败");
            }
        }
        if (response.isSuccess()) {
            //转账成功时的操作
            transfer.setCreateTime(new Date());
            transfer.setOrderId(response.getOrderId());
            transfer.setPayDate(response.getPayDate());
            transfer.setUserId(byToken.getId());
            transferMapper.insert(transfer);
            //减少用户余额
            BigDecimal multiply = amount.abs().multiply(new BigDecimal(-1).setScale(2, BigDecimal.ROUND_DOWN));
            byToken.setBalance(multiply);
            BigDecimal balanceNow = usersService.findBalanceByID(byToken.getId());
            //余额不足
            boolean b = balanceNow.compareTo(amount) < 0;
            if (b) {
                throw new Z406Exception("余额不足：not sufficient funds");
            }
            int i = this.usersService.addBalance(byToken);
            if (i != 1) {
                throw new Z406Exception("转账失败,数据添加失败");
            }
            //返回结果
            return "转账成功";
        } else {
            return response.getSubMsg();
        }

    }

    @Override
    public AlipayClient getAlipayClient() {
        return new DefaultAlipayClient(getUrl(), getAppID(), getAppPrivateKey(), "json", "UTF-8", getAlipayPublicKey(), "RSA2");
    }

    /**
     * 只是做字符串的拼接和转码，调用SDK方法整合，传递给前台 阿里定义方式
     * alipay 签名方法
     *
     * @return 方法生成的字符串，可直接返回给前台
     */
    @Override
    public String createPayOrderInfo(AlipayModel alipayModel) throws AlipayApiException {
        //实例化客户端
        AlipayClient alipayClient = getAlipayClient();
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        UUID uuid = UUID.randomUUID();
        String outtradeno = uuid.toString();
        //客户端获取,订单详情描述信息
        model.setBody(alipayModel.getBody());
        //客户端获取 订单标题
        model.setSubject(alipayModel.getSubject());
        //订单唯一标识
        model.setOutTradeNo(outtradeno);
        //客户端获取  订单支付时间
        model.setTimeoutExpress("30m");
        //客户端获取  订单支付金额
        model.setTotalAmount(alipayModel.getTotalAmount());
        //固定值
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        //服务端接收异步通知的接口
        request.setNotifyUrl(alipayModel.getNotifyUrl());

        //这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        return response.getBody();
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean daShang(Map<String, String> params) throws Exception {
        String body = params.get("body");
        log.error("body:" + body);
        String[] split = body.split(",");
        //当前登录用户的token
        String token = split[0];
        TradingRecord tradingRecord = new TradingRecord();
        Long user;
        Users byToken = usersService.findByToken(token);
        //若为打赏，用户未登录时，打赏人ID设为0
        if (Objects.equals(byToken, null)) {
            if (Objects.equals(split[2], "打赏")) {
                user = 0L;
            } else {
                throw new Z406Exception("未登录");
            }
        } else {
            user = byToken.getId();
        }
        //交易类型  打赏或充值
        tradingRecord.setPayType(split[2]);
        tradingRecord.setUser(user);
        //交易主题
        tradingRecord.setSubject(params.get("subject"));
        //订单创建时间
        tradingRecord.setGmtCreate(params.get("gmt_create"));
        tradingRecord.setAppId(params.get("app_id"));
        tradingRecord.setBuyerId(params.get("buyer_id"));
        tradingRecord.setBuyerLogonId(params.get("buyer_logon_id"));
        tradingRecord.setSellerEmail(params.get("seller_email"));
        tradingRecord.setSellerId(params.get("seller_id"));
        //支付宝交易流水号
        tradingRecord.setTradeNo(params.get("trade_no"));
        tradingRecord.setCreateTime(new Date());
        //交易总资金
        BigDecimal bigDecimalAmount = new BigDecimal(params.get("total_amount")).setScale(2, BigDecimal.ROUND_HALF_UP);
        bigDecimalAmount = bigDecimalAmount.multiply(new BigDecimal(1 - this.brokerage)).setScale(2, BigDecimal.ROUND_HALF_UP);
        tradingRecord.setTotalAmount(bigDecimalAmount);
        //打赏的商品的ID
        if (Objects.equals(split[2], "打赏")) {
            Long itemID = Long.valueOf(split[1]);
            InformationAll info = informationAllService.info(itemID);
            tradingRecord.setAuthor(info.getUsers().getId());
            tradingRecord.setItemID(itemID);
            //添加用户余额
            int i = usersService.addBalance(new Users(info.getUsers().getId(), bigDecimalAmount));
            if (i != 1) {
                throw new Exception("打赏失败");
            }
        }
        //充值时的操作
        log.error("充值的用户的账号："+byToken.getLoginName());
        if (Objects.equals("充值", split[2])) {
            //1.未用户添加钻石；2.添加钻石明细；
            //1
            Integer zsNum = bigDecimalAmount.multiply(new BigDecimal(systemProperties.getRmb2zs())).intValue();
           log.error("充值的钻石数额："+zsNum);
            usersService.addOrSubZs(zsNum, user);
            //2
            ZsDetail zsDetail = new ZsDetail(new Date(), zsNum, ZsObtainType.RechargeReward.getIndex(), user, "充值" + zsNum + "钻石");
            zsDetailService.insert(zsDetail);
        }
        if (Objects.equals("个人品牌", split[2])) {
        	Integer updatePersonalBrand = usersService.updatePersonalBrand(user);
        	log.debug(">>>>>>>>>>>>>>>"+updatePersonalBrand);
        }
        int j = tradingRecordService.add(tradingRecord);
        log.error("充值的人民币数额："+tradingRecord.getTotalAmount());
        if (j != 1) {
            throw new Exception("打赏失败");
        }
        return true;
    }
}
