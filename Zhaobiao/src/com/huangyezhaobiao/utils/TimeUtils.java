package com.huangyezhaobiao.utils;

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
        Date currentDate = new Date(currentTime);
        Date lastDate    = new Date(lastTime);
        long hour = (currentDate.getTime()-lastDate.getTime())/1000/60/60;
        return hour>24;
    }
}
