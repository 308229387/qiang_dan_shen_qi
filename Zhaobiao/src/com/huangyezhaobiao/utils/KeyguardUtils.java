package com.huangyezhaobiao.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huangyezhaobiao.bean.push.PushBean;

/**
 * Created by 58 on 2015/8/26.
 * 解锁的相关工具类
 */
public class KeyguardUtils {

    public static final String KEYGUARD_PUSH_BEAN = "keyguard_push_bean";
    /**
     * 为true时屏幕亮
     */
   public static boolean LOCK_TO_UNLOCK = false;
    public static boolean notLock = false;
    /**
     * 为true时为屏幕是亮的
     */
    public static boolean SCREEN_ON = true;
    public static boolean onLock;
    public static boolean need_lock = false;

    /**
     * 是不是锁屏状态，return true是锁屏状态，return false为解锁状态
     *
     * @param context
     * @return
     */
    public static boolean isLockScreen(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();//api
    }


    /**
     * 跳转到锁时的activity，带一个bean
     */
    public static void goToKeyguardActivity(Context context, Class<? extends Activity> clazz, PushBean pushBean) {
        if (pushBean != null) {
            Intent intent = new Intent(context, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//新开启一个task栈，因为可能当前没有task栈，不设置会报错
            Bundle bundle = new Bundle();
            intent.putExtra(KeyguardUtils.KEYGUARD_PUSH_BEAN, pushBean);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转到锁时的activity,不带bean,自己去那边取
     */
    public static void goToKeyguardActivity(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//新开启一个task栈，因为可能当前没有task栈，不设置会报错
        context.startActivity(intent);
    }
}
