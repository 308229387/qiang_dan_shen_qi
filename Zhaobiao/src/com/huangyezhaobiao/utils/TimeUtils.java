package com.huangyezhaobiao.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * com.huangyezhaobiao.utils
 * Created by shenzhixin on 2016/5/3 18:12.
 */
public final class TimeUtils {
    /**
     * 判断两个时间戳是否超过了24小时
     *
     * @return
     */
    public static boolean beyond24Hour(long currentTime, long lastTime) {
        Log.e("ashen", "currentTime:" + currentTime + ",lastTime:" + lastTime);
        Date currentDate = new Date(currentTime);
        Date lastDate = new Date(lastTime);
        Log.e("ashen", "ct:" + currentDate.getTime() + ",lt:" + lastDate.getTime());
        long hour = (currentDate.getTime() - lastDate.getTime()) / 1000 / 60 / 60;
        Log.e("ashen", "hour:" + hour);
        return hour > 24;
    }

    /**
     * 判断两个时间戳是否超过了2小时
     *
     * @return
     */
    public static boolean beyond2Hour(Date currentTime, Date lastTime) {

//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
//            Date d1 = df.parse(currentTime);
//            Date d2 = df.parse(lastTime);
            long diff = currentTime.getTime() - lastTime.getTime();
            long hours = diff / (1000 * 60 * 60 );

            return hours > 2;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * 判断两个时间戳是否超过了13天
     *
     * @return
     */
    public static boolean beyond13Days(long currentTime, long lastTime) {
        Log.e("ashen", "currentTime:" + currentTime + ",lastTime:" + lastTime);
        Date currentDate = new Date(currentTime);
        Date lastDate = new Date(lastTime);
        Log.e("ashen", "ct:" + currentDate.getTime() + ",lt:" + lastDate.getTime());
        long day = (currentDate.getTime() - lastDate.getTime()) / 1000 / 60 / 60 / 24;
        Log.e("ashen", "day:" + day);
        return day > 13;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatDateTime(String time) {

        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time == null || "".equals(time)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set( Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        current.setTime(date);

        if(current.after(today)){
            SimpleDateFormat fat = new java.text.SimpleDateFormat("HH:mm");
            return fat.format(date);
//            return time.split(" ")[1];
        }else{
            SimpleDateFormat fat = new java.text.SimpleDateFormat("MM-dd");
            return fat.format(date);
//            int index = time.indexOf("-")+1;
//            return time.substring(index, time.length());
        }
    }


}
