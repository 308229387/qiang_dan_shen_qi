package com.huangyezhaobiao.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	/**
	 * 手机号验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false; 
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}
	
	public static void main(String[] a){
		if(isMobile("16555555555")){
			System.out.println("haha");
		}
		
		
	}
	
}
