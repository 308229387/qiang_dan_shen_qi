package com.huangyezhaobiao.utils;

import android.content.Context;

/**
 * Created by shenzhixin on 2015/12/14.
 * 得到文字的
 */
public class StringUtils {
    public static String getStringByResId(Context context,int resId){
        return context.getString(resId);
    }
}
