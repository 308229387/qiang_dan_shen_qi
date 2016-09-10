package com.huangyezhaobiao.inter;

public interface Constans {

	/**
	 * 已完成
	 */
	String DONE_FRAGMENT = "3";

	/**
	 * 待服务
	 */
	String READY_SERVICE = "1";
	
	/**
	 * 服务中
	 */
	String ON_SERVICE = "2";
	
	/**
	 * 已完成---已服务
	 */
	String DONE_FRAGMENT_FINISH="31";
	
	/**
	 * 已完成--已取消
	 */
	String DONE_FRAGMENT_CANCEL = "32";

	/**
	 * 商机--未分类
	 */
    String BUSINESS_NOT_CALSSFY = "4";

	/**
	 * 商机--待跟进
	 */
    String BUSINESS_WAIT_FOLLOW = "5";

	/**
	 * 商机--已完结
	 */
	String BUSINESS_ALREADY_FINISH = "6";


	/**
	 * orderId的key----bean-->fetchDetailsActivity的传递key
	 */
	String ORDER_ID = "orderId";

	/**
	 * 抢单结果
	 */
	int QD_RESULT = 101;
	
	/**
	 * 抢单倒计时提醒
	 */
	int QD_DAOJISHI = 102;
	
	/**
	 * 系统通知
	 */
	int SYS_NOTI = 103;

	/**
	 * sharedPreference的名字
	 */
	String APP_SP = "app";

	/**
	 * 版本号
	 */
	String VERSION_NAME = "versionName";
	
	/**
	 * 数据库查找等于
	 */
	String DB_OPERATION_EQUAL = "=";
	
	/**
	 * 测试的userid
	 */
	//String USER_ID = "24454277549825";
	
	/**
	 * 测试的token
	 */
	String USER_TOKEN = "AND_32904932706561";
	
	/**
	 * 客服电话
	 */
	String HELP_TEL = "4008589114";

	/**
	 * 为true时才进行开锁操作，否则什么都不做
	 */
	boolean LOCK_TO_UNLOCK = false;


	/**
	 * 进行的是登录的请求
	 */
	String REQUEST_LOGIN = "1";

	/**
	 *进行的是一般的请求
	 */
	String REQUEST_NORMAL = "0";


	String CHILD_ACCOUNT_ID = "account_id";

	String CHILD_ACCOUNT_NAME = "account_name";

	String CHILD_ACCOUNT_PHONE = "account_phone";

	String CHILD_ACCOUNT_AUTHORITY = "account_authority";
}



