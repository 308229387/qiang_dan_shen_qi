package com.huangyezhaobiao.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shenzhixin on 2015/8/31.
 * <p/>
 * 存储一般性的sp文件
 */
public class SPUtils {
    private static final String BACKGROUND ="background" ;
    private static String SP_NAME = "zhaobiao";
    private static String KEY_FIRST_MAIN = "isFirstMain_haha1";
    private static String KEY_FIRST_UPDATE = "isFirstUpdate";
    private static String KEY_NEED_AUTO_SETTING = "isAutoSetting";
    private static String KEY_SERVICE_STATE     = "serviceState";


    /**
     * 设置服务状态
     * @param context
     * @param state
     */
    public static void setServiceState(Context context,String state){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,0);
        sp.edit().putString(KEY_SERVICE_STATE,state).commit();
    }

    /**
     * 获得服务状态
     * @param context
     * @return
     */
    public static String getServiceState(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,0);
        return sp.getString(KEY_SERVICE_STATE,"1");
    }

    /**
     * 是否第一次进入主界面
     *
     * @param context
     * @return
     */
    public static boolean isFirstMainActivity(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getBoolean(KEY_FIRST_MAIN, true);
    }

    /**
     * 从后台进入的
     * @return
     */
    public static boolean fromBackground(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,0);
        return sp.getBoolean(BACKGROUND, false);
    }

    /**
     * 进入后台
     * @param context
     */
    public static void toBackground(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,0);
        sp.edit().putBoolean(BACKGROUND,true).commit();
    }

    /**
     * 进入了前台
     * @param context
     */
    public static void toForeground(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,0);
        sp.edit().putBoolean(BACKGROUND,false).commit();
    }

    /**
     * 存储成为不是第一次进入主界面
     *
     * @param context
     * @return
     */
    public static void saveAlreadyMainActivity(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putBoolean(KEY_FIRST_MAIN, false).commit();
    }

    /**
     * 是不是第一次更新时进入的
     * @param context
     * @return
     */
    public static boolean isFirstUpdate(Context context) {
        //以spName和版本号来保存
        SharedPreferences sp = context.getSharedPreferences(SP_NAME+"24", 0);
        return sp.getBoolean(KEY_FIRST_UPDATE, true);
    }

    /**
     * 更新后已经进入
     * @param context
     */
    public static void saveAlreadyFirstUpdate(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME+"24", 0);
        sp.edit().putBoolean(KEY_FIRST_UPDATE, false).commit();
    }

    /**
     * 是不是需要显示自动设置界面
     * @param context
     * @return
     */
    public static boolean isAutoSetting(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,0);
        return sp.getBoolean(KEY_NEED_AUTO_SETTING,true);
    }

    /**
     * 存储成不需要显示自动设置这块
     */
    public static void saveAutoSetting(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,0);
        sp.edit().putBoolean(KEY_NEED_AUTO_SETTING,false).commit();

    }
}
