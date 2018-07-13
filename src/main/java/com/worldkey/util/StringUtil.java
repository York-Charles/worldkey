package com.worldkey.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 非负整数
	 */
	public static final String num= "^[0-9]*[1-9][0-9]*$";
	/**
	 * 正则匹配验证
	 * @param str 被验证字符串
	 * @param regex 正则表达式，StringUtil中有静态规则
	 */
	public static boolean regexString(String str,String regex){
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(str);
		return matcher.matches();
	}
	
	
	public static List<String> info2ImageList(String info,String host){
		List<String>list=new ArrayList<>();
		int a=0;
		int leng=63+host.length();
		while (a!=-1) {
			if (a!=0) {
				a=info.indexOf("<img",a+1);
			}else{
				a=info.indexOf("<img",a);
			}
			if (a!=-1) {
				String sb=info.substring(a, info.indexOf(">", a));
				if (sb.contains(host)) {
					int b=sb.indexOf("http://"+host+"/image");
					if (b!=-1) {
						sb=sb.substring(b,b+leng);
						list.add(sb);
					}
				}
			}
		}
		return list;
	}
	
}
