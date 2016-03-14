package com.huangyezhaobiao.url;

import android.content.Context;

import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
import com.huangyezhaobiao.utils.PhoneUtils;
import com.huangyezhaobiao.utils.UserUtils;

import java.util.Date;

/**
 * 获得各个链接的后缀名
 * 
 * @author 58
 * 
 */
public class UrlSuffix {
	/**
	 * 返回app前缀的UserId userid/212111
	 * 
	 * @return
	 */
	public static String getAppUserId() {
		return URLConstans.APP_USER_ID + URLConstans.APP_CHA_EQUALS + UserUtils.userId;
	}
	
	/**
	 * 返回api前缀的userId userId
	 * @return
	 */
	public static String getApiUserId(){
		return URLConstans.API_USER_ID + URLConstans.APP_CHA_EQUALS + UserUtils.userId;
	}

	/**
	 * 返回app前缀的orderState
	 * 
	 * @return
	 */
	public static String getAppCenterOrderState() {
		return URLConstans.APP_ORDER_STATE + URLConstans.APP_CHA_EQUALS
				+ QiangDanBaseFragment.orderState;
	}

	/**
	 * 返回app前缀的pageNum
	 * 
	 * @param pageNum
	 * @return
	 */
	public static String getPageNum(String pageNum) {
		return  URLConstans.APP_PAGE_NUM
				+ URLConstans.APP_CHA_EQUALS + pageNum;
	}

	public static String getNormalPageNum(String pageNum){
		return URLConstans.APP_NORMAL_PAGE_NUM+ URLConstans.APP_CHA_EQUALS + pageNum;
	}

	/**
	 * 返回token
	 * 
	 * @return
	 */
	public static String getToken() {
		return URLConstans.APP_TOKEN + URLConstans.APP_CHA_EQUALS +  new Date().getTime();
	}

	/**
	 * 返回orderId
	 */
	public static String getApiOrderId(String orderId){
		return URLConstans.ORDER_ID + URLConstans.APP_CHA_EQUALS +  orderId;
	}
	
	/**
	 * 得到
	 * @return
	 */
	public static String getAppOrderId(String orderId){
		return URLConstans.APP_ORDER_ID + URLConstans.APP_CHA_EQUALS + orderId;
	}
	
	
	public static String getAppCenterDetailsSuffix(Context context ,String orderId){
		return getAppOrderId(orderId) + URLConstans.AND + getCommonSuffix(context);
	}
	
	
	/**
	 * 得到订单中心的后缀
	 * 
	 * @return
	 */
	public static String getAppCenterSuffix(Context context,String pageNum) {
		return getAppUserId() + URLConstans.AND + getAppCenterOrderState()
				+ URLConstans.AND + getPageNum(pageNum) + URLConstans.AND
				+ getCommonSuffix(context);
	}
	
	/**
	 * 得到余额的后缀
	 * @return
	 */
	public static String getApiBalance(Context context){
		return getAppUserId() + URLConstans.AND + getCommonSuffix(context);
	}

	
	
	
	/**
	 * 获取余额链接的后缀名
	 * 
	 * @return
	 */
	public static String getYuESuffix() {
		return URLConstans.USER_ID + UserUtils.userId + URLConstans.TOKEN
				+ "111";
	}

	/**
	 * 获取消息中心的后缀名
	 */
	public static String getMessageCentrSuffix() {
		return getYuESuffix();
	}

	/**
	 * 获取订单中心的后缀名
	 * 
	 * @return
	 *//*
	public static String getOrderCenterSuffix(String state) {
		return URLConstans.USER_ID + "1234" + "&" + URLConstans.ORDER_STATE
				+ state + URLConstans.TOKEN + "111";
	}*/
	
	/**
	 * 获取帮助页面的后缀名
	 * @return
	 */
	public static String getHelpSuffix(){
		return getApiUserId() + URLConstans.AND + getToken() ;
	}
	
	/**
	 * 获取产品介绍页面的后缀名
	 * @return
	 */
	public static String getIntroductionSuffix(){
		return getApiUserId()+ URLConstans.AND + getToken() ;
	}


	/**
	 * 返回退出登录的后缀名
	 * @return
	 */
	public static String getLogoutSuffix(Context context){
		return getApiUserId() + URLConstans.AND + getToken() + URLConstans.AND + getLoginToken(context) + URLConstans.AND + getCommonSuffix(context);
	}

	/**
	 * 获得登录时获取的token
	 * @param context
	 * @return
	 */
	private static String getLoginToken(Context context){
		return URLConstans.LOGIN_TOKEN+URLConstans.APP_CHA_EQUALS+SharedPreferencesUtils.getUserToken(context);
	}

	/**
	 *获得手机绑定号码改变页面的初始电话的后缀名
	 * @param context
	 * @return
	 */
	public static String getMobileChangeOriMobileSuffix(Context context){
		return getToken() + URLConstans.AND + getUserId(context) + URLConstans.AND + getCommonSuffix(context);
	}


	private static String getUserId(Context context){
		return URLConstans.API_USER_ID + URLConstans.APP_CHA_EQUALS + UserUtils.getUserId(context);
	}

	private static String getMobile(String mobile){
		return URLConstans.OLD_PHONE + URLConstans.APP_CHA_EQUALS + mobile;
	}


	private static String getNewPhone(String newPhone){
		return URLConstans.MOBILE + URLConstans.APP_CHA_EQUALS + newPhone;
	}

	private static String getCode(String code){
		return URLConstans.CODE + URLConstans.APP_CHA_EQUALS + code;
	}
	/**
	 * 手机绑定页面的提交
	 * 	http://serverdomain/api/updatePhoneNumber
	 * ?userId=  &mobile=&newPhone=&code=&token=
	 * @return
	 */
	public static String getMobileChangeSubmitSuffix(Context context,String mobile,String newPhone,String code){
		return getUserId(context) + URLConstans.AND + getMobile(mobile) + URLConstans.AND + getNewPhone(newPhone) + URLConstans.AND +getCode(code) + URLConstans.AND + getCommonSuffix(context) ;
	}


	/**
	 * 自定义设置界面的后缀名
	 * @return
	 */
	public static String getAutoSettingSuffix(Context context){
		return getUserId(context)  ;
	}

	/**
	 * userId= &orderId= &token=
	 * @param context
	 * @param orderId
	 * @return
	 */
	public static String getRefundCloseTimeSuffix(Context context,String orderId){
		return getUserId(context) + URLConstans.AND + getApiOrderId(orderId) +  URLConstans.AND + UrlSuffix.getCommonSuffix(context);
	}

	/**
	 * userId=&pageNum=&token=
	 * @return
	 */
	public static String getConsumptionSuffix(Context context,String pageNum){
		return getUserId(context) + URLConstans.AND + getNormalPageNum(pageNum) + URLConstans.AND +getCommonSuffix(context);
	}

	/**
	 *
	 * http://serverdomain/app/expire?userId=&token=
	 * @param context
	 * @return
	 */
	public static String getAccountExpireSuffix(Context context){
		return getUserId(context) + URLConstans.AND + getCommonSuffix(context);
	}

	/**
	 * 返回系统通知的后缀名
	 * @param context
	 * @param pageNum
	 * @return
	 */
	public static String getSysListSuffix(Context context,String pageNum){
		return getConsumptionSuffix(context, pageNum);
	}

	/**
	 * 获取Imei号
	 * @param context
	 * @return
	 */
	public static String getIMEISuffix(Context context){
		return URLConstans.UUID + URLConstans.APP_CHA_EQUALS + PhoneUtils.getIMEI(context);
	}


	public static String getPlatform(){
		return URLConstans.PLATFORM +URLConstans.APP_CHA_EQUALS+"1";
	}

	public static String getVersion(){
		//2.2版本就是2
		return URLConstans.VERSION + URLConstans.APP_CHA_EQUALS + "2";
	}

	/**
	 * 所有的Url接口的后缀名
	 * 	token/platform/UUID/version
	 * @return
	 */
	public static String getCommonSuffix(Context context){
		return getToken() + URLConstans.AND + getPlatform() +URLConstans.AND + getIMEISuffix(context) + URLConstans.AND + getVersion();
	}


	private static String getSourceSuffix(String source){
		return URLConstans.TELPHONE_SOURCE + URLConstans.APP_CHA_EQUALS + source;
	}

	/**
	 * userId= &orderId= &source=
	 * 打电话的url的后缀
	 * @param context
	 * @return
	 */
	public static String getTelephoneSuffix(Context context,String orderId,String source){
		return getUserId(context) + URLConstans.AND + getApiOrderId(orderId) + URLConstans.AND + getSourceSuffix(source) + URLConstans.AND + getCommonSuffix(context);
	}

	/**userId= &platform= &UUID= &version= &token=
	 * 从后台到前台的后缀
	 *
	 * @param context
	 * @return
	 */
	public static String getBackToForeSuffix(Context context){
		return getUserId(context) + URLConstans.AND + getCommonSuffix(context);
	}


	/**
	 * 得到个推的后缀名
	 * userId= & bidId= & bidType = & platform= &UUID= &version= &token=
	 * @param context
	 * @return
	 */
	public static String getGePushSuffix(Context context,String bidId,String bidType){
		return getUserId(context) + URLConstans.AND + getBidId(bidId) + URLConstans.AND + getBidType(bidType) + URLConstans.AND + getCommonSuffix(context);
	}

	public static String getBidId(String bidId){
		return URLConstans.BID_ID + URLConstans.APP_CHA_EQUALS +bidId;
	}

	public static String getBidType(String bidType){
		return URLConstans.BID_TYPE + URLConstans.APP_CHA_EQUALS +bidType;
	}
}
