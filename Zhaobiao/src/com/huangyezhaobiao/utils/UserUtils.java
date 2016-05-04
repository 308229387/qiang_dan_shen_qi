package com.huangyezhaobiao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 用户的beanUtils
 * @author shenzhixin
 *
 */
public class UserUtils {
	//static String TEST_USERID ="19833833741319";// "54765741"19833833741319;
	private static final String USER_SP_NAME = "user";
	private static final String USER_ID = "userId";
	private static final String USER_PPU = "ppu";
	private static final String COMPANY_NAME = "companyName";
	private static final String USER_NAME = "companyName";
	private static final String HASVALIDATE = "hasValidate";
	public static String userId = "";//"30620951766535";
	private static String companyName;
	public static String userName;
	private static String ppu;
	public static int hasValidate = 1; //默认未认证，0代表已经认证

	private static String PPU_SP_NAME = "ppu_f";

	public static void saveUser(Context context,String userId,String companyName,String userName){
		SharedPreferences sp = context.getSharedPreferences(PPU_SP_NAME, 0);//用userId，来区分
		UserUtils.userId = userId;
		UserUtils.companyName = companyName;
		UserUtils.userName = userName;
		sp.edit().putString(USER_ID, userId).commit();
		sp.edit().putString(COMPANY_NAME, companyName).commit();
		sp.edit().putString(USER_NAME, userName).commit();
	}

	public static void setPassportUserId(Context context,String ppu){
		SharedPreferences sp = context.getSharedPreferences(PPU_SP_NAME, 0);//用userId，来区分
		sp.edit().putString(USER_ID, ppu).commit();
	}

	public static String getPassportUserId(Context context){
//		if(TextUtils.isEmpty(userId)){
			userId = context.getSharedPreferences(PPU_SP_NAME, 0).getString(USER_ID, "");
//		}
		return userId;
	}

	public static void setUserId(Context context,String userId){
		SharedPreferences sp = context.getSharedPreferences(USER_SP_NAME, 0);//用userId，来区分
		UserUtils.userId = userId;
		sp.edit().putString(USER_ID, userId).commit();
	}

	public static void hasValidate(Context context){
		hasValidate = 0;
		SharedPreferences sp = context.getSharedPreferences(USER_SP_NAME, 0);
		sp.edit().putInt(HASVALIDATE, hasValidate).commit();
	}
	
	/**
	 * 得到用户Id
	 * @param context
	 * @return
	 */
	public static String getUserId(Context context){
		/*boolean flag = true;
		if(flag){
			return "36374346077713";
		}*/
		if(TextUtils.isEmpty(userId)){
			userId = context.getSharedPreferences(USER_SP_NAME, 0).getString(USER_ID, "");
		}
		return userId;
	}


	public static void setPPU(Context context,String ppu){
		SharedPreferences sp = context.getSharedPreferences(PPU_SP_NAME, 0);//用userId，来区分
		sp.edit().putString(USER_PPU, ppu).commit();
	}

	/**
	 * 得到用戶PPU
	 * @param context
	 * @return
	 * */
	public static String getUserPPU(Context context){
//		if(TextUtils.isEmpty(ppu)){
			ppu = context.getSharedPreferences(PPU_SP_NAME, 0).getString(USER_PPU, "");
//		}
		return ppu;
	}

	/**
	 * 得到用户公司名
	 * @param context
	 * @return
	 */
	public static String getUserCompany(Context context){
		if(TextUtils.isEmpty(companyName)){
			companyName = context.getSharedPreferences(USER_SP_NAME, 0).getString(COMPANY_NAME, "");
		}
		LogUtils.LogE("shenzhixinui","companyName:"+companyName+",userName:"+getUserName(context));
		return companyName;
	}
	
	/**
	 * 得到用户名
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context){
		if(TextUtils.isEmpty(userName)){
			userName = context.getSharedPreferences(USER_SP_NAME, 0).getString("userName", "");
		}
		return userName;
	}
	
	/**
	 * 得到是否认证
	 * @param context
	 * @return
	 */
	public static boolean isValidate(Context context){
		if(hasValidate==1){
			hasValidate = context.getSharedPreferences(USER_SP_NAME, 0).getInt(HASVALIDATE, 1);
		}
		if(hasValidate == 0){
			return true;
		}
		return false;
	}
	
	
	
	public static void clearUserInfo(Context context){
		context.getSharedPreferences(USER_SP_NAME, 0).edit().putString(USER_ID, "").commit();
		context.getSharedPreferences(USER_SP_NAME, 0).edit().putString(COMPANY_NAME, "").commit();
		context.getSharedPreferences(USER_SP_NAME, 0).edit().putInt(HASVALIDATE, 1).commit();
		userId = "";
		companyName = "";
		hasValidate= 1;
	}
}
