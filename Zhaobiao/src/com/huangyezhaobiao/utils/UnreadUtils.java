package com.huangyezhaobiao.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 未读信息的工具类
 * 要以userId来进行区分...来进行不同账户的区分
 * @author shenzhixin
 *
 */
public class UnreadUtils {
	private static final String NAME = "unread";


	/**
	 * 存储新订单信息
	 * @param context
	 */
	public static void saveNewOrder(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		int value = sp.getInt("newOrder", 0);
		value = value + 1;
		sp.edit().putInt("newOrder", value).commit();

	}
	/**
	 * 返回新订单的未读条数
	 * @param context
	 * @return
	 */
	public static int getNewOrder(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		int value = sp.getInt("newOrder", 0);
		return value;
	}

	public static boolean isHasNewOrder(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME + UserUtils.getUserId(context), Context.MODE_PRIVATE);
		return sp.contains("newOrder");
	}

	/**
	 * 清除新订单的未读数
	 * @param context
	 */
	public static void clearNewOder(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME + UserUtils.getUserId(context), Context.MODE_PRIVATE);
		sp.edit().putInt("newOrder", 0).commit();
	}
	/**
	 * 存储抢单结果的未读信息
	 * @param context
	 */
	public static void saveQDResult(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		int value = sp.getInt("result", 0);
		value = value + 1;
		sp.edit().putInt("result", value).commit();
		
	}
	/**
	 * 返回抢单结果的未读条数
	 * @param context
	 * @return
	 */
	public static int getQDResult(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		int value = sp.getInt("result", 0);
		return value;
	}
	public static boolean isHasQDResult(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		return sp.contains("result");
	}
	
	/**
	 * 清除抢单结果的未读数
	 * @param context
	 */
	public static void clearQDResult(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME + UserUtils.getUserId(context), Context.MODE_PRIVATE);
		sp.edit().putInt("result", 0).commit();
	}
	
	/**
	 * 存储倒计时的未读信息
	 * @param context
	 */
	public static void saveDaoJiShi(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		int value = sp.getInt("daojishi", 0);
		value = value + 1;
		sp.edit().putInt("daojishi", value).commit();
	}
	
	/**
	 * 得到倒计时的未读信息数量
	 * @param context
	 * @return
	 */
	public static int getDaoJiShi(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		int value = sp.getInt("daojishi", 0);
		return value;
	}
	
	
	/**
	 * 清除倒计时的未读数
	 * @param context
	 */
	public static void clearDaoJiShiResult(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME + UserUtils.getUserId(context), Context.MODE_PRIVATE);
		sp.edit().putInt("daojishi", 0).commit();
	}
	
	/**
	 * 存储系统通知的未读信息
	 * @param context
	 */
	public static void saveSysNoti(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		int value = sp.getInt("sysno", 0);
		value = value + 1;
		sp.edit().putInt("sysno", value).commit();
	}
	
	/**
	 * 得到存储系统通知的未读信息数量
	 * @param context
	 * @return
	 */
	public static int getSysNotiNum(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		int value = sp.getInt("sysno", 0);
		return value;
	}
	
	/**
	 * 清楚系统通知的未读信息
	 * @param context
	 */
	public static void clearSysNotiNum(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME+UserUtils.getUserId(context), Context.MODE_PRIVATE);
		sp.edit().putInt("sysno", 0).commit();
	}




	/**
	 * 得到所有的未读信息的数量
	 * @param context
	 * @return
	 */
	public static int getAllNum(Context context){
		
		return getDaoJiShi(context)+getQDResult(context)+getSysNotiNum(context);
	}
}
