package com.huangyezhaobiao.gtui;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.PushManager;

/**
 * Created by 58 on 2016/1/7.
 * 个推的代理类，里面进行个推的一些东西
 */
public class GePushProxy {
    /**
     * 初始化个推应用，一般是在activity中进行
     * context传入的必须是ApplicationContext
     */
    public static void initliazePush(Context context){
        PushManager.getInstance().initialize(context);
    }


    /**
     * 绑定别名的设置
     * @param context 全局的context
     * @return
     */
    public static boolean bindPushAlias(Context context,String alias){
        //Log.e("shenzhixinuuu","alias:"+alias);
        return PushManager.getInstance().bindAlias(context,alias);
    }

    /**
     * 解绑别名的设置
     * @param context 全局的context
     * @return
     */
    public static boolean unBindPushAlias(Context context,String alias){
        return PushManager.getInstance().unBindAlias(context,alias,true);
    }
}
