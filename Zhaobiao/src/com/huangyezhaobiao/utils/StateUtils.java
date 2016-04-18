package com.huangyezhaobiao.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * 状态控制类
 * 
 * @author linyueyang
 *
 */
public class StateUtils {

	public static void setState(int inputStatr){
		state = inputStatr;
	}
	
	
	public static int state = 0; //服务模式为1 休息模式为2  后台运行为0
	
	
	/**
	 * 判断程序是否在后台运行
	 * @param context
	 * @return 是不是后台运行
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					Log.i("后台", appProcess.processName);
					return true;
				} else {
					Log.i("前台", appProcess.processName); 
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取状态 
	 * @param context
	 * @return 服务模式为1 休息模式为2  后台运行为0
	 */
	public static int getState(Context context){
		
		/*if(isBackground(context)){
			state = 0;
		}*/
		Log.e("ssssssssss", "get:"+state);
		return state;
	}
	
	
	

}
