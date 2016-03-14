package com.huangyezhaobiao.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.PowerManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

public class Utils {

	/**
	 * 判断是否亮屏
	 * @param context
	 * @return
	 */
	public static boolean isScreeOn(Context context){
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}
	/**
	 * 应用是否在前台
	 * @param context
	 * @return
	 */
	public static boolean isForeground(Context context){
		ActivityManager mActivityManager = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
		String mPackageName =context.getPackageName();
		List<ActivityManager.RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (mPackageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				Log.e("push","foreground");
				return true;
			}
		}
		Log.e("push","background");
		return false;
	}
	
	/**
	 * Convert Dp to Pixel
	 */
	public static int dpToPx(float dp, Resources resources){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
		return (int) px;
	}
	
	public static int getRelativeTop(View myView) {
//	    if (myView.getParent() == myView.getRootView())
	    if(myView.getId() == android.R.id.content)
	        return myView.getTop();
	    else
	        return myView.getTop() + getRelativeTop((View) myView.getParent());
	}
	
	public static int getRelativeLeft(View myView) {
//	    if (myView.getParent() == myView.getRootView())
		if(myView.getId() == android.R.id.content)
			return myView.getLeft();
		else
			return myView.getLeft() + getRelativeLeft((View) myView.getParent());
	}



	/**
	 * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
}
