package com.worldkey.jdpush;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.http.util.TextUtils;
import org.apache.jasper.compiler.JspConfig;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Users;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.mapper.PraiseNumMapper;
import com.worldkey.service.PraiseService;
import com.worldkey.service.UsersService;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;


public class Jdpush {

	//点赞推送
	public static void jpushAndriod(String Name, String s,HashMap<String, String> map){
//		String appKey = "30c46a4377d8c45879e00e41";
//	    String masterSecret = "3fd34692df36ac31480d6d56";
	    String appKey = "9296bdfa2513fc48ed2a1973";
	    String masterSecret = "9f6d6683b67db93d926b91c0";
	    int maxtrytimes = 3;
	    JPushClient jpushClient = new JPushClient(masterSecret, appKey,maxtrytimes);
	    
	    PushPayload payload = PushPayload.newBuilder()
	    	
	            .setPlatform(Platform.android())//指定android平台的用户
	         
	            .setAudience(Audience.alias(s))//你项目中的所有用户 1  alias别名
	     
	            //.setNotification(Notification.android("给你点了一个赞",Name, null))
	            .setMessage(Message.newBuilder()
	            		.setMsgContent(Name+"给您点了一个赞")
	            		.setTitle(Name+"给您点了一个赞")
	            		.addExtras(map)
	            		.build())
//	            .setNotification(Notification.newBuilder()
//	            		.addPlatformNotification(AndroidNotification.newBuilder()
//	            				.setAlert(Name+"给您点了一个赞")
//	            				.addExtras(map)
//	            				.build())
//	            		.build())
	            //发送内容,从controller层中拿过来的参数)1
	            
	            .setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(86400).build())
	            .build();
	
	    try {
            PushResult pu = jpushClient.sendPush(payload); 
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }    
	  
	}
	//评论推送
	public static void jpushAndriod1(String Name, String s,String a,HashMap<String, String> map){
		String appKey = "30c46a4377d8c45879e00e41";
	    String masterSecret = "3fd34692df36ac31480d6d56";
	    int maxtrytimes = 3;
	    JPushClient jpushClient = new JPushClient(masterSecret, appKey,maxtrytimes);
	    
	    PushPayload payload = PushPayload.newBuilder()
	    	
	            .setPlatform(Platform.android())//指定android平台的用户
	         
	            .setAudience(Audience.alias(s))//你项目中的所有用户 1  alias别名
	 
	            .setNotification(Notification.newBuilder()
	            		.addPlatformNotification(AndroidNotification.newBuilder()
//	            				.setAlert(Name+"给您评论"+":"+a)
	            				.setTitle(Name+"给您评论"+":"+a)
	            				.addExtras(map)
	            				.build()).build())
	            //发送内容,从controller层中拿过来的参数)1
	            
	            .setOptions(Options.newBuilder().setApnsProduction(false).build())
	            .build();
	
	    try {
            PushResult pu = jpushClient.sendPush(payload); 
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }    
	  
	}
	//送礼推送
	public static void jpushAndriod2(String Name, String s,String a,HashMap<String, String> map){
		String appKey = "30c46a4377d8c45879e00e41";
	    String masterSecret = "3fd34692df36ac31480d6d56";
	    int maxtrytimes = 3;
	    JPushClient jpushClient = new JPushClient(masterSecret, appKey,maxtrytimes);
	    
	    PushPayload payload = PushPayload.newBuilder()
	    	
	            .setPlatform(Platform.android())//指定android平台的用户
	         
	            .setAudience(Audience.alias(s))//你项目中的所有用户 1  alias别名
	 
	            .setNotification(Notification.newBuilder()
	            		.addPlatformNotification(AndroidNotification.newBuilder()
	            				.setAlert(Name+"送给您一个"+":"+a)
	            				.addExtras(map)
	            				.build()).build())
	            //发送内容,从controller层中拿过来的参数)1
	            
	            .setOptions(Options.newBuilder().setApnsProduction(false).build())
	            .build();
	
	    try {
            PushResult pu = jpushClient.sendPush(payload); 
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }    
	  
	}

	//分享推送
	public static void jpushAndriod3(String Name, String s,String a,HashMap<String, String> map){
		String appKey = "30c46a4377d8c45879e00e41";
	    String masterSecret = "3fd34692df36ac31480d6d56";
	    int maxtrytimes = 3;
	    JPushClient jpushClient = new JPushClient(masterSecret, appKey,maxtrytimes);
	    
	    PushPayload payload = PushPayload.newBuilder()
	    	
	            .setPlatform(Platform.android())//指定android平台的用户
	         
	            .setAudience(Audience.alias(s))//你项目中的所有用户 1  alias别名
	 
	            .setNotification(Notification.newBuilder()
	            		.addPlatformNotification(AndroidNotification.newBuilder()
	            				.setAlert(Name+"转发了您的文章"+":"+a)
	            				.addExtras(map)
	            				.build()).build())
	            //发送内容,从controller层中拿过来的参数)1
	            
	            .setOptions(Options.newBuilder().setApnsProduction(false).build())
	            .build();
	
	    try {
            PushResult pu = jpushClient.sendPush(payload); 
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }    
	  
	}
	//创建小组推送
	public static void jpushAndriod4(String loginName, String groupName,HashMap<String, String> map){
		String appKey = "30c46a4377d8c45879e00e41";
	    String masterSecret = "3fd34692df36ac31480d6d56";
	    int maxtrytimes = 3;
	    JPushClient jpushClient = new JPushClient(masterSecret, appKey,maxtrytimes);
	    
	    PushPayload payload = PushPayload.newBuilder()
	    	
	            .setPlatform(Platform.android())//指定android平台的用户
	         
	            .setAudience(Audience.alias(loginName))//你项目中的所有用户 1  alias别名
	 
	            .setNotification(Notification.newBuilder()
	            		.addPlatformNotification(AndroidNotification.newBuilder()
	            				.setAlert("【审核通过】恭喜您创建小组"+groupName+"成功!")
	            				.addExtras(map)
	            				.build()).build())
	            //发送内容,从controller层中拿过来的参数)1
	            
	            .setOptions(Options.newBuilder().setApnsProduction(false).build())
	            .build();
	
	    try {
            PushResult pu = jpushClient.sendPush(payload); 
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }    
	  
	}
	//评价推送
	public static void jpushAndriod5(String Name, String s,String a,HashMap<String, String> map){
		String appKey = "30c46a4377d8c45879e00e41";
	    String masterSecret = "3fd34692df36ac31480d6d56";
	    int maxtrytimes = 3;
	    JPushClient jpushClient = new JPushClient(masterSecret, appKey,maxtrytimes);
	    
	    PushPayload payload = PushPayload.newBuilder()
	    	
	            .setPlatform(Platform.android())//指定android平台的用户
	         
	            .setAudience(Audience.alias(s))//你项目中的所有用户 1  alias别名
	 
	            .setNotification(Notification.newBuilder()
	            		.addPlatformNotification(AndroidNotification.newBuilder()
	            				.setAlert(Name+"给您评价"+":"+a)
	            				.addExtras(map)
	            				.build()).build())
	            //发送内容,从controller层中拿过来的参数)1
	            
	            .setOptions(Options.newBuilder().setApnsProduction(false).build())
	            .build();
	
	    try {
            PushResult pu = jpushClient.sendPush(payload); 
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }    
	  
	}
	//评论点赞推送 
	public static void jpushAndriod6(String Name, String s,String s1,HashMap<String, String> map){
		String appKey = "30c46a4377d8c45879e00e41";
	    String masterSecret = "3fd34692df36ac31480d6d56";
	    int maxtrytimes = 3;
	    JPushClient jpushClient = new JPushClient(masterSecret, appKey,maxtrytimes);
	    
	    PushPayload payload = PushPayload.newBuilder()
	    	
	            .setPlatform(Platform.android())//指定android平台的用户
	         
	            .setAudience(Audience.alias(s))//你项目中的所有用户 1  alias别名
	     
	            //.setNotification(Notification.android("给你点了一个赞",Name, null))
	            
	            .setNotification(Notification.newBuilder()
	            		.addPlatformNotification(AndroidNotification.newBuilder()
	            				.setAlert(Name+"给您的评论"+s1+"点了一个赞")
	            				.addExtras(map)
	            				.build())
	            		.build())
	            //发送内容,从controller层中拿过来的参数)1
	            
	            .setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(86400).build())
	            .build();
	
	    try {
            PushResult pu = jpushClient.sendPush(payload); 
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }    
	  
	}
	//评论回复推送
	public static void jpushAndriod7(String Name, String s,String s1,String a,HashMap<String, String> map){
		String appKey = "30c46a4377d8c45879e00e41";
	    String masterSecret = "3fd34692df36ac31480d6d56";
	    int maxtrytimes = 3;
	    JPushClient jpushClient = new JPushClient(masterSecret, appKey,maxtrytimes);
	    
	    PushPayload payload = PushPayload.newBuilder()
	    	
	            .setPlatform(Platform.android())//指定android平台的用户
	         
	            .setAudience(Audience.alias(s))//你项目中的所有用户 1  alias别名
	 
	            .setNotification(Notification.newBuilder()
	            		.addPlatformNotification(AndroidNotification.newBuilder()
	            				.setAlert(Name+"给您的评论"+s1+"评论了"+":"+a)
	            				.addExtras(map)
	            				.build()).build())
	            //发送内容,从controller层中拿过来的参数)1
	            
	            .setOptions(Options.newBuilder().setApnsProduction(false).build())
	            .build();
	
	    try {
            PushResult pu = jpushClient.sendPush(payload); 
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }    
	  
	}
	//三级回复推送
	public static void jpushAndriod8(String Name, String s,String s1,String a,HashMap<String, String> map){
		String appKey = "30c46a4377d8c45879e00e41";
	    String masterSecret = "3fd34692df36ac31480d6d56";
	    int maxtrytimes = 3;
	    JPushClient jpushClient = new JPushClient(masterSecret, appKey,maxtrytimes);
	    
	    PushPayload payload = PushPayload.newBuilder()
	    	
	            .setPlatform(Platform.android())//指定android平台的用户
	         
	            .setAudience(Audience.alias(s))//你项目中的所有用户 1  alias别名
	 
	            .setNotification(Notification.newBuilder()
	            		.addPlatformNotification(AndroidNotification.newBuilder()
	            				.setAlert(Name+"给您的评论"+s1+"评论了"+":"+a)
	            				.addExtras(map)
	            				.build()).build())
	            //发送内容,从controller层中拿过来的参数)1
	            
	            .setOptions(Options.newBuilder().setApnsProduction(false).build())
	            .build();
	
	    try {
            PushResult pu = jpushClient.sendPush(payload); 
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }    
	  
	}


}
