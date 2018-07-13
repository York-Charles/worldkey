package com.worldkey.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	
	public static String getTime(Date date){
		long ts = date.getTime();
        long now = new Date().getTime();
        long i = now - ts;
        String a="";
        if(i<3600000){
        	i = i/60/1000;
        	a = i+"分钟前";
        }else if(i<86400000){
        	i = i/60/60/1000;
        	a = i+"小时前";
        }else if(i<172800000){
        	i = i/24/60/60/1000;
        	a = i+"天前";
        }else{
        	SimpleDateFormat stf=new SimpleDateFormat("MM-dd");
        	a=stf.format(date);
        }
		return a;	
	}

}
