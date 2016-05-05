package com.huangyezhaobiao.utils;

import android.util.Log;

import java.util.Date;

/**
 * com.huangyezhaobiao.utils
 * Created by shenzhixin on 2016/5/3 18:12.
 */
public final class TimeUtils {
    /**
     * 判断两个时间戳是否超过了24小时
     * @return
     */
    public static boolean beyond24Hour(long currentTime,long lastTime){
        Log.e("ashen","currentTime:"+currentTime+",lastTime:"+lastTime);
        Date currentDate = new Date(currentTime);
        Date lastDate    = new Date(lastTime);
        Log.e("ashen","ct:"+currentDate.getTime()+",lt:"+lastDate.getTime());
        long hour = (currentDate.getTime()-lastDate.getTime())/1000/60/60;
        Log.e("ashen","hour:"+hour);
        return hour>24;
    }
}
