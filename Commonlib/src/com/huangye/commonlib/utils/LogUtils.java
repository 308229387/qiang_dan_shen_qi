package com.huangye.commonlib.utils;

import android.util.Log;

/**
 * ��ӡLog�Ĺ�����
 * @author admin
 *
 */
public class LogUtils {
	//��Ϊtrueʱ�ͽ���log��ӡ
	private static boolean DEBUG = true;
	
	public static void LogD(String tag,String msg){
		if(DEBUG)
			Log.d(tag, msg);
	}
	
	public static void LogE(String tag,String msg){
		if(DEBUG)
			Log.e(tag, msg);
	}
	
	
	public static void LogV(String tag,String msg){
		if(DEBUG)
			Log.v(tag, msg);
	}
}
