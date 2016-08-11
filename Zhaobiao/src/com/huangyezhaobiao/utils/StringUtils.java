package com.huangyezhaobiao.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by shenzhixin on 2015/12/14.
 * 得到文字的
 */
public class StringUtils {
    public static String getStringByResId(Context context,int resId){
        return context.getString(resId);
    }

    public static boolean stringFilter(String str) throws PatternSyntaxException {
        //只允许字母，汉字
        String regEx = "[a-zA-Z\u4e00-\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();

    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("[1][358]\\d{9}");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
    /*
	  * 可接受的电话格式有：
      */
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
	/*
      * 可接受的电话格式有：
      */
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";

        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if (matcher.matches() || matcher2.matches()) {
            isValid = true;
        }
        return isValid;

    }


    public static boolean isCode(String str) {
        Pattern p = Pattern.compile("^[0-9]{6}$"); // 验证手机号
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        return b;
    }


    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        Matcher  m = p.matcher(str);
        boolean b = m.matches();
        return b;
    }
}
