package com.huangyezhaobiao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.huangyezhaobiao.application.BiddingApplication;

/**
 * 用户的beanUtils
 * @author shenzhixin
 *
 */
public class UserUtils {
	//static String TEST_USERID ="19833833741319";// "54765741"19833833741319;
	private static final String USER_SP_NAME = "user";
	private static final String USER_ID = "userId";
	private static final String IS_SON = "isSon";
	private static final String R_BAC = "rbac";
	private static final String USER_PPU = "ppu";
	private static final String COMPANY_NAME = "companyName";
	private static final String USER_NAME = "companyName";
	private static final String HASVALIDATE = "hasValidate";
	public static String userId = "";//"30620951766535";
	private static String companyName;
	public static String userName;
	public static String isSon;
	public static String rbac;
	private static String ppu;
	public static int hasValidate = 1; //默认未认证，0代表已经认证
	/** 与passport 相关的用户信息*/
	private static String PPU_SP_NAME = "ppu_f";
	private static String SESSION_TIME = "sessionTime";
	private static long sessionTime = 0L;

	private static String ACCOUNT_NAME = "accountName";
	private static String ACCOUNT_ENCRYPT = "accountEncrypt";
	private static String accountName;
	private static String accountEncrypt;

	/** 埋点的SharedPerfrencens*/
	private static final String MOB_FILE_NAME = "hy_mob";
	private static final String HASACTION = "hasaction";
	private static final String MOB_ITEM = "mobitems";
	private static int mobItem;
	private static final String MOB_COMMON = "mobcommon";
	private static String mobCommon;
	private static final String MOB_DATA = "mobdata";
	private static String mobData;
	private static final String MOB_TIME = "mobtime";
	private static long mobTime;
	private static final String USER_APP_VERSION = "appVersion";
	private static String appVersion;
	private static final String BUSINESS_MASK = "business_mask";
	private static final String BUSINESS_CHECK = "business_check";

	/**
	 * 非强制更新判断
	 */
	private static final String USER_UPDATE_SP_NAME = "user_update";
	private static final String USER_UPDATE_NAME = "force_update";

	/** 24小时*/
	private static long AFTER_A_DAY = 24 * 60 * 60 * 1000l;
	public static void saveUser(Context context,String userId,String companyName,String userName){
		SharedPreferences sp = context.getSharedPreferences(USER_SP_NAME, 0);//用userId，来区分
		//shenzhixin add
		setUserId(context,userId);
		//shenzhixin add
		UserUtils.userId = userId;
		UserUtils.companyName = companyName;

		UserUtils.userName = userName;
		sp.edit().putString(USER_ID, userId).commit();
		sp.edit().putString(COMPANY_NAME, companyName).commit();
		sp.edit().putString(USER_NAME, userName).commit();
	}

	public static  void saveAcocuntInfo(Context context,String isSon,String rbac){
		SharedPreferences sp = context.getSharedPreferences(USER_SP_NAME, 0);//用userId，来区分
		UserUtils.isSon = isSon;
		UserUtils.rbac = rbac;
		sp.edit().putString(IS_SON, isSon).commit();
		sp.edit().putString(R_BAC, rbac).commit();
	}

//	public static void setAccountName(Context context,String accountName){
//		SharedPreferences sp = context.getSharedPreferences(PPU_SP_NAME, 0);//用userId，来区分
//		sp.edit().putString(ACCOUNT_NAME, accountName).apply();
//	}
//
//	public static String getAccountName(Context context){
//		accountName = context.getSharedPreferences(PPU_SP_NAME, 0).getString(ACCOUNT_NAME, "");
//		return accountName;
//	}


//	public static void setAccountEncrypt(Context context,String accountEncrypt){
//		SharedPreferences sp = context.getSharedPreferences(PPU_SP_NAME, 0);//用userId，来区分
//		sp.edit().putString(ACCOUNT_ENCRYPT, accountEncrypt).apply();
//	}
//
//	public static String getAccountEncrypt(Context context){
//		accountEncrypt = context.getSharedPreferences(PPU_SP_NAME, 0).getString(ACCOUNT_ENCRYPT, "");
//		return accountEncrypt;
//	}



//	public static void setPassportUserId(Context context,String userId){
//		SharedPreferences sp = context.getSharedPreferences(PPU_SP_NAME, 0);
//		sp.edit().putString(USER_ID, userId).apply();
//	}
//
//	public static String getPassportUserId(Context context){
//		return context.getSharedPreferences(PPU_SP_NAME, 0).getString(USER_ID, "");
//	}

	/**
	 * 是否从登录页面进入，提示更新
	 * @param context
	 * @return
	 */
	public static boolean isNeedUpdate(Context context) {
		SharedPreferences sp = context.getSharedPreferences(USER_UPDATE_SP_NAME, 0);
		return sp.getBoolean(USER_UPDATE_NAME, true);
	}

	public static void saveNeedUpdate(Context context,Boolean flag){
		SharedPreferences sp = context.getSharedPreferences(USER_UPDATE_SP_NAME, 0);
		sp.edit().putBoolean(USER_UPDATE_NAME, flag).commit();
	}


	/**
	 * 存储登录时间
	 * @param context
	 * @param sessionTime
	 */
	public static void setSessionTime(Context context,long sessionTime){
		SharedPreferences sp = context.getSharedPreferences(PPU_SP_NAME, 0);
		sp.edit().putLong(SESSION_TIME, sessionTime).apply();
	}

	/**
	 * 获取登录时间
	 * @param context
	 * @return
	 */
	public static long getSessionTime(Context context){
		try {
			sessionTime = context.getSharedPreferences(PPU_SP_NAME,0).getLong(SESSION_TIME,0L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessionTime;
	}

	public static boolean isHasSessionTime(Context  context){
		SharedPreferences sp = context.getSharedPreferences(PPU_SP_NAME, 0);
		return sp.contains(SESSION_TIME);
	}



	public static void setUserId(Context context,String userId){
		SharedPreferences sp = context.getSharedPreferences(USER_SP_NAME, 0);//用userId，来区分
		UserUtils.userId = userId;
		sp.edit().putString(USER_ID, userId).commit();
	}

	/**
	 * 得到用户Id
	 * @return
	 */
	public static String getUserId(){
		if(TextUtils.isEmpty(userId)){
			userId = BiddingApplication.getAppInstanceContext().getSharedPreferences(USER_SP_NAME, 0).getString(USER_ID, "");
		}
		return userId;
	}

	/**
	 * 得到用户Id
	 * @param context
	 * @return
	 */
	public static String getUserId(Context context){
		if(TextUtils.isEmpty(userId)){
			userId = context.getSharedPreferences(USER_SP_NAME, 0).getString(USER_ID, "");
		}
		return userId;
	}


	/**
	 * 手机号已验证
	 * @param context
	 */
	public static void hasValidate(Context context){
		hasValidate = 0;
		SharedPreferences sp = context.getSharedPreferences(USER_SP_NAME, 0);
		sp.edit().putInt(HASVALIDATE, hasValidate).commit();
	}
	/**
	 * 手机号是否得到验证
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


//	public static void setPPU(Context context,String ppu){
//		SharedPreferences sp = context.getSharedPreferences(PPU_SP_NAME, 0);//用userId，来区分
//		sp.edit().putString(USER_PPU, ppu).commit();
//	}
//	/**
//	 * 得到用戶PPU
//	 * @param context
//	 * @return
//	 * */
//	public static String getUserPPU(Context context){
////		if(TextUtils.isEmpty(ppu)){
//			ppu = context.getSharedPreferences(PPU_SP_NAME, 0).getString(USER_PPU, "");
////		}
//		return ppu;
//	}


	/**
	 * 得到用户公司名
	 * @param context
	 * @return
	 */
	public static String getUserCompany(Context context){
		if(TextUtils.isEmpty(companyName)){
			companyName = context.getSharedPreferences(USER_SP_NAME, 0).getString(COMPANY_NAME, "");
		}
		return companyName;
	}

	/**
	 * 得到子账号标志
	 * @param context
	 * @return
	 */
	public static String getIsSon(Context context){
		if(TextUtils.isEmpty(isSon)){
			isSon = context.getSharedPreferences(USER_SP_NAME, 0).getString(IS_SON, "");
		}
		return isSon;
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
	 * 得到子账号权限
	 * @param context
	 * @return
	 */
	public static String getRbac(Context context){
		if(TextUtils.isEmpty(rbac)){
			rbac = context.getSharedPreferences(USER_SP_NAME, 0).getString(R_BAC, "");
		}
		return rbac;
	}

	
	public static void clearUserInfo(Context context){
		context.getSharedPreferences(USER_SP_NAME, 0).edit().putString(USER_ID, "").commit();
		context.getSharedPreferences(USER_SP_NAME, 0).edit().putString(COMPANY_NAME, "").commit();
		context.getSharedPreferences(USER_SP_NAME, 0).edit().putString(IS_SON, "").commit();
		context.getSharedPreferences(USER_SP_NAME, 0).edit().putString(R_BAC, "").commit();
		context.getSharedPreferences(USER_SP_NAME, 0).edit().putInt(HASVALIDATE, 1).commit();
		userId = "";
		companyName = "";
		isSon ="";
		rbac ="";
		hasValidate= 1;

		/** clear ppu configurtaion ,added by chenguangming*/
		context.getSharedPreferences(PPU_SP_NAME, 0).edit().putString(USER_ID, "").commit();
		context.getSharedPreferences(PPU_SP_NAME, 0).edit().putString(ACCOUNT_NAME, "").commit();
		context.getSharedPreferences(PPU_SP_NAME, 0).edit().putString(ACCOUNT_ENCRYPT, "").commit();
		context.getSharedPreferences(PPU_SP_NAME, 0).edit().putString(SESSION_TIME, "").commit();
		context.getSharedPreferences(PPU_SP_NAME, 0).edit().putString(COMPANY_NAME, "").commit();
	}

	/** 判断举例上次登录是否有24小时*/
	public static boolean isOutOfDate(Context context){
		boolean outofdate = false;
		long curTime = System.currentTimeMillis();
		if(Math.abs(getSessionTime(context) - curTime) > AFTER_A_DAY){
			outofdate = true;
		} else {
			outofdate = false;
		}
		return outofdate;
	}

	/** added by chenguangming*/
	public static void setMobItem(Context context,int mobItem){
		SharedPreferences sp = context.getSharedPreferences(MOB_FILE_NAME, 0);
		sp.edit().putInt(MOB_ITEM, mobItem).apply();
	}

	public static int getMobItem(Context context){
		mobItem = context.getSharedPreferences(MOB_FILE_NAME, 0).getInt(MOB_ITEM,0);
		return mobItem;
	}

	public static boolean isHasMobItem(Context  context){
		SharedPreferences sp = context.getSharedPreferences(MOB_FILE_NAME, 0);
		return sp.contains(MOB_ITEM);
	}

	public static void setMobCommon(Context context,String mobCommon){
		SharedPreferences sp = context.getSharedPreferences(MOB_FILE_NAME, 0);
		sp.edit().putString(MOB_COMMON, mobCommon).apply();
	}

	public static String getMobCommon(Context context){
		mobCommon = context.getSharedPreferences(MOB_FILE_NAME, 0).getString(MOB_COMMON, "");
		return mobCommon;
	}

	public static void setMobData(Context context,String mobData){
		SharedPreferences sp = context.getSharedPreferences(MOB_FILE_NAME, 0);
		sp.edit().putString(MOB_DATA, mobData).apply();
	}

	public static String getMobData(Context context){
		mobData = context.getSharedPreferences(MOB_FILE_NAME, 0).getString(MOB_DATA,"");
		return mobData;
	}

	public static void setMobTime(Context context,long mobTime){
		SharedPreferences sp = context.getSharedPreferences(MOB_FILE_NAME, 0);
		sp.edit().putLong(MOB_DATA, mobTime).commit();
	}

	public static long getMobTime(Context context){
		mobTime = context.getSharedPreferences(MOB_FILE_NAME, 0).getLong(MOB_TIME, 0);
		return mobTime;
	}

	public static void setAppVersion(Context context,String appVersionCode){
		SharedPreferences sp = context.getSharedPreferences(USER_SP_NAME, 0);
		sp.edit().putString(USER_APP_VERSION, appVersionCode).commit();
	}


	public static String getAppVersion(Context context){
		appVersion = context.getSharedPreferences(USER_SP_NAME, 0).getString(USER_APP_VERSION,"");
		return appVersion;
	}

	public static void clearMob(Context context){
		context.getSharedPreferences(MOB_FILE_NAME, 0).edit().putString(mobData, "").commit();
		context.getSharedPreferences(MOB_FILE_NAME, 0).edit().putString(mobCommon, "").commit();
		context.getSharedPreferences(MOB_FILE_NAME, 0).edit().putInt(MOB_ITEM, 0).commit();
	}

	public static void setHasaction(Context context,String hasaction){
		SharedPreferences sp = context.getSharedPreferences(MOB_FILE_NAME, 0);
		sp.edit().putString(HASACTION, hasaction).commit();
	}

	public static String getHasaction(Context context){
		String hasaction = context.getSharedPreferences(MOB_FILE_NAME, 0).getString(HASACTION, "1");
		return hasaction;
	}

	public static void setBusinessMask(Context context,Boolean maskTag){
		SharedPreferences sp = context.getSharedPreferences(MOB_FILE_NAME, 0);
		sp.edit().putBoolean(BUSINESS_MASK, maskTag).commit();
	}

	public static Boolean getBusinessMask(Context context){
		Boolean isReaded = context.getSharedPreferences(MOB_FILE_NAME, 0).getBoolean(BUSINESS_MASK, false);
		return isReaded;
	}

	public static void setBusinessCheckBox(Context context,Boolean checkTag){
		SharedPreferences sp = context.getSharedPreferences(MOB_FILE_NAME, 0);
		sp.edit().putBoolean(BUSINESS_CHECK, checkTag).commit();
	}

	public static Boolean getBusinessCheckBox(Context context){
		Boolean needshow = context.getSharedPreferences(MOB_FILE_NAME, 0).getBoolean(BUSINESS_CHECK, true);
		return needshow;
	}

}
