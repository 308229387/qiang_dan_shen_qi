package com.huangyezhaobiao.utils;

import android.app.ActivityManager;
import android.content.Context;
import java.util.List;

/**
 * Created by 58 on 2016/3/24.
 */
public class AndroidUtils {
    /**
     * 获取当前正在运行的进程名称
     * @return
     */
    public static String getCurrentProcessName(Context context,int pid){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        if(processes==null)
            return "";
        for(ActivityManager.RunningAppProcessInfo processInfo:processes){
            if(processInfo.pid == pid){
                return processInfo.processName;
            }
        }
        return "";
    }

}
