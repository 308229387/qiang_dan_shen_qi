package com.huangyezhaobiao.utils;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2015/9/7.
 * 百度统计的封装类
 */
public class BDMob {
    private static final String USER_BIGBANG120 = "15293583575559";
    private static final String USER_BIGBANG130 = "15296406114055";
    private static final String USER_BIGBANG140 = "33232546531082";
    private static final String USER_OTHER      = "54765741";
    private static  BDMob bdMob = new BDMob();
    private static List<String> userIds = new ArrayList<String>();
    static {
        //加入不需要统计的userId
        userIds.add(USER_BIGBANG120);
        userIds.add(USER_BIGBANG130);
        userIds.add(USER_BIGBANG140);
        userIds.add(USER_OTHER);
    }

    public static BDMob getBdMobInstance(){
        return bdMob;
    }

    private BDMob(){
    }

    /**
     * activity间的OnResume
     * @param context
     */
    public void onResumeActivity(Context context){
        if(!notNeedMob(context)){
           // Log.e("shen","onResumeBD");
            StatService.onResume(context);
        }
    }

    /**
     * activity间的onPause
     * @param context
     */
    public void onPauseActivity(Context context){
        if(!notNeedMob(context)){
            StatService.onPause(context);
        }
    }


    /**
     * 需要统计返回false,不统计返回true
     * @param context
     * @return
     */
    private boolean notNeedMob(Context context){
        return userIds.contains(UserUtils.getUserId(context)) || TextUtils.isEmpty(UserUtils.getUserId(context));
    }


    /**
     * 自定义事件的处理
     * @param context
     * @param eventId
     */
    public void onMobEvent(Context context,String eventId){
       StatService.onEvent(context,eventId,"event_label");
    }

}
