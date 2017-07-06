package com.tongyx.plugin.favorites.utils;

public class StringUtil {
	public static boolean isEmpty(String s){
		return s==null || s.length()==0;
	}
	
	public static boolean isBlank(String s){
		return s==null || s.trim().length()==0;
	}
	
	public static String nvl(String str){
		return nvl(str,"",false);
	}
	
	public static String nvl(String str,boolean trim){		
		return nvl(str,"",trim);
	}
	
	public static String nvl(String str, String defaultStr){
		return nvl(str,defaultStr,false);
	}
	
	public static String nvl(String str, String defaultStr,boolean trim){
		String s = str==null?defaultStr:str;
		if(trim)return s.trim();
		return s;
	}

}
