package com.worldkey.util;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;

public class SendMsgUtil {
	
	  public static final String alicode="SMS_95425067";	//统一验证码模板
	
	//public static final String register="SMS_71685163";	//用户注册验证码模板
	//public static final String updateTel="SMS_71685160";	//信息变更
	//public static final String changePwd="SMS_71685161";	//修改密码
    	
	/**
	 * @param code  随机生成的6位验证码
	 * @param phone 接收验证码的手机号,多个手机号用英文逗号隔开
	 * @param temlateCode 短信模板
	 * 
	 **/
	public static void SMS(String code,String phone , String temlateCode) {
		
		/*
         * Step 1. 获取主题引用
         */
        CloudAccount account = new CloudAccount("LTAIZCzuRy8QLVf6", "Bej2utf6t99PXiYODi0Kz6p89lb6iu", "http://1060492502057647.mns.cn-beijing.aliyuncs.com");
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef("sms.topic-cn-beijing");

        /*
         * Step 2. 设置SMS消息体（必须）
         *
         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("worldkey");

        /*
         * Step 3. 生成SMS消息属性
         */
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 设置发送短信的签名（SMSSignName）
        batchSmsAttributes.setFreeSignName("世界钥匙");
        // 3.2 设置发送短信使用的模板（SMSTempateCode）
        batchSmsAttributes.setTemplateCode(temlateCode);	//变量1短信模板
        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsReceiverParams.setParam("code", code);		//变量2产生随机验证码
        smsReceiverParams.setParam("product", "世界钥匙");
        // 3.4 增加接收短信的号码
       
        batchSmsAttributes.addSmsReceiver(phone, smsReceiverParams);//变量3接收短信的手机号
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);

       try {
            /*
             * Step 4. 发布SMS消息
             */
            @SuppressWarnings("unused")
			TopicMessage ret = topic.publishMessage(msg, messageAttributes);
            //System.out.println("MessageId: " + ret.getMessageId());
            //System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
        } catch (ServiceException se) {
            System.out.println(se.getErrorCode() + se.getRequestId());
            System.out.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.close();
	}
	
		/**
		 * 随机生成6位数字	
		 */
		public static String getRandom(){
		    /*
		    改为StringBuilder，减少内存消耗
		     */
		    /*String num="";*/
			StringBuilder num = new StringBuilder();
			for (int i = 0;i < 6;i++){
                int code =(int) Math.floor(Math.random() * 9 + 1);
                num.append(String.valueOf(code));
				/*num = num + String.valueOf((int)Math.floor(Math.random()*9 + 1));*/
			}
			return num.toString();
		}
}
