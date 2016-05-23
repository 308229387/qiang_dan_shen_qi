package com.huangyezhaobiao.url;

/**
 * 接口的链接
 * 
 * @author shenzhixin 把后缀加入到方法里去写
 * 
 */
public interface URLConstans {
	/**
	 * 斜线
	 */
	String LINE = "/";

	/**
	 * 与符号
	 */
	String AND = "&";

	/**
	 * 问号
	 */
	String CHA_ASK = "?";

	/**
	 * 等于号
	 */
	String APP_CHA_EQUALS = "=";

	/**
	 * 最基类的链接，所有链接的基础 http://192.168.118.41/api/
	 * http://192.168.118.41/
	 * http://zhaobiao.58.com/
	 * http://10.252.152.201/
	 */
	String BASE_URL = com.huangye.commonlib.constans.URLConstans.BASE_URL;

	/**
	 * api的字段
	 */
	String API = "api/";

	/**
	 * app的字段
	 */
	String APP = "app/";

	/**
	 * order的字段
	 */
	String APP_ORDER = "order";

	/**
	 * app前缀的orderlist的字段
	 */
	String APP_ORDER_LIST = "orderlist";

	/**
	 * app前缀的orderdetails的字段
	 */
	String APP_ORDER_DETAIL = "orderdetail";

	/**
	 * app前缀的orderid的字段
	 */
	String APP_ORDER_ID = "orderId";

	/**
	 * app前缀的userid
	 */
	String APP_USER_ID = "userId";

	/**
	 * api前缀的userId
	 */
	String API_USER_ID = "userId";

	/**
	 * app前缀的orderState
	 */
	String APP_ORDER_STATE = "orderState";

	/**
	 * app前缀的token
	 */
	String APP_TOKEN = "token";

	/**
	 * orderId
	 */
	String ORDER_ID = "orderId";

	/**
	 * app前缀的pageNum
	 */
	String APP_PAGE_NUM = "pageNum";

	String APP_NORMAL_PAGE_NUM = "pageNum";

	/**
	 * api前缀的getBalance
	 */
	String APP_GET_BALANCE = "balance";

	String APP_STATIC_BASE = "helper";
	String MOBILE          = "mobile";
	String NEW_PHONE       = "newPhone";
	String CODE            = "code";
	String OLD_PHONE ="oldPhone";
	String VERSION = "version";
	/**
	 * app前缀的帮助页面
	 */
	String APP_HELP = APP_STATIC_BASE+LINE+"help?";
	String APP_INTRODUCTION = APP_STATIC_BASE + LINE + "introduction?";


	/**
	 * loginToken
	 */
	String LOGIN_TOKEN = "loginToken";

	/**
	 * http://192.168.118.41/app/
	 */
	String BASE_URL_APP = BASE_URL + APP;

	/**
	 * http://192.168.118.41/api/
	 */
	String BASE_URL_API = BASE_URL + API;

	/**
	 * 帮助页面的url
	 */
	String HELP_URL = BASE_URL_APP + APP_HELP ;

	String HELP_PAGE_URL = URLConstans.BASE_HTML_URL + "help.html";

	/**
	 * 功能介绍页面的url
	 */
	String INTRODUCTION_URL = BASE_URL_APP + APP_INTRODUCTION;

	String INTRODUCTION_PAGE_URL = URLConstans.BASE_HTML_URL + "introduction.html";

	/**
	 * 查询余额的url "http://192.168.118.41/app/order/balance?userid=24454277549825&token=1"
	 * /app/userinfo?queryKey=balance|userinfo
	 */
	String APP_USERINFO = "userinfo";
	String GET_BALANCE_API = BASE_URL_APP + APP_USERINFO;
	

	/**
	 * 新的抢单中心的url "http://192.168.118.41/app/order/orderlist?
	 * userid=24454277549826&orderstate=
	 * "+QiangDanBaseFragment.orderState+"&pageNum=1&token=1"
	 */
	String MESSAGE_CENTER_URL = BASE_URL_APP + APP_ORDER + LINE
			+ APP_ORDER_LIST + CHA_ASK;

	/**
	 * 新的抢单详情的url
	 * "http://192.168.118.41/app/order/orderdetail?orderid="+orderId+"&token=1"
	 */
	String MESSAGE_CENTER_DETEAILS_URL = BASE_URL_APP + APP_ORDER + LINE
			+ APP_ORDER_DETAIL + CHA_ASK;

	String GET_USER_MOBILE ="getUserMobile";

	String UPDATE_PHONE_NUMBER = "updatePhoneNumber";
	/**
	 *http://serverdomain/api/getUserMobile? token=&userId=
	 */
	String MOBILE_CHANGE_GET_CODE_URL = BASE_URL_API + GET_USER_MOBILE + CHA_ASK;

	/**
	 * http://serverdomain/api/updatePhoneNumber?
	 * userId=  &mobile=&newPhone=&code=&token=
	 */
	String MOBILE_CHANGE_SUBMIT_URK =BASE_URL_API + UPDATE_PHONE_NUMBER + CHA_ASK;



	/**
	 * 版本更新的链接
	 * http://192.168.118.41/app/getversion/apk?appId=1"
	 */
	String UPDATE_APP_URL = BASE_URL_APP+"getversion/apk?appId=1";

	String LOGOUT_API_URL = BASE_URL_API+"logout?";

	String SOFTWARE_USAGE = BASE_URL_APP + "usageProtocol";

	String SOFTWARE_USEAGE_PROTOCOL = URLConstans.BASE_HTML_URL + "usageProtocol.html";
	/**
	 * created by chenguangming 2016/03/14
	 * 申请成为抢单神器会员
	 */
	String HOW_TO_BECOME_VIP = BASE_URL_APP + "howToLogin";
	//String HOW_TO_BECOME_VIP = "http://192.168.118.132:9090/app/howToLogin";

	String HOW_TO_BECOME_VIP_MEMBER = URLConstans.BASE_HTML_URL + "qdsq.html";
	/**
	 * 版本字段
	 */
	String PLATFORM = "platform";

	/**
	 * 时间相关字段
	 */
	String TOKEN = "&token=";

	/**
	 * userId
	 */
	String USER_ID = "userId=";

	/**
	 * order_state
	 */
	String ORDER_STATE = "orderState=";

	/**
	 * 加了一个标识符,暂时由这个来处理
	 */
	String BASE_URL_DE = BASE_URL + "api";


	String UUID = "UUID";

	/**
	 * 登录的链接Url
	 */
	String URL_LOGIN_POST = BASE_URL_DE + "/login";

	/**
	 * App更新的链接Url
	 * http://192.168.118.41/API/getVersion?platform=android&token=1112
	 */
	String URL_APP_UPDATE_GET = BASE_URL_DE + "/getVersion?" + PLATFORM + TOKEN;

	/**
	 * 58余额的链接url http://192.168.118.41
	 */
	String URL_YUE_GET = BASE_URL_DE + "/getBalance?";

	/**
	 * 订单中心的url
	 */
	String URL_ORDER_CENTER_GET = BASE_URL_DE + "/getOrders?";

	/**
	 * 消息中心的url http://192.168.118.41/API/getMessage?userId= &token=
	 */
	String URL_MESSAGE_CENTER_GET = BASE_URL_DE + "/getMessage?";


	/**
	 * 自定义接单设置的url
	 * http://10.252.156.46/api/business/setting?userId=27574722556934&cateId=4058
	 */
	String AUTO_SETTINGS = BASE_URL_DE + "/business/setting?";

	/**
	 * 退单的接收数据展示的url
	 * 都用这一个,服务端根据状态不同返回不同的值
	 * /app/cancelOrder?userId= &orderId= &token=
	 */
	String URL_REFUND_NOT_OPEN = BASE_URL_APP + "cancelOrder?";

	/**
	 * 消费记录接口
	 * http://serverdomain/app/costRecord?userId=&pageNum=&token=
	 */
	String URL_CONSUMPTION = BASE_URL_APP + "costRecord?";

	/**
	 * 系统通知的列表接口
	 * http://serverdomain/app/sysNotice/list?userId=&pageNum=&token=
	 */
	String URL_SYS_LIST = BASE_URL_APP + "sysNotice/list?";


	/**
	 * 首次提交退单的接口
	 */
	String URL_FIRST_SUBMIT_REFUND = BASE_URL_APP + "submitCancelOrder?";

	/**
	 * 补交退单的接口
	 */
	String URL_ADD_SUBMIT_REFUND = BASE_URL_APP + "addEvidenceOrder?";

	/**
	 * 网灵通是否过期的接口
	 * http://serverdomain/app/expire?userId=&token=
	 */
	String URL_APP_ACCOUNT_EXPIRE = BASE_URL_APP + "expire?";

	/**
	 * 订单列表页和详情页打电话的接口
	 * http://serverdomain/app/callPhone?userId= &platform= &UUID= &version= &token= &orderId= &source=
	 */
	String URL_APP_TELEPHONE = BASE_URL_APP + "order/callPhone?";

	/**
	 * 应用从后台到前台的链接
	 * http://serverdomain/app/appEnterForeground?userId= &platform= &UUID= &version= &token=
	 */
	String URL_BACKGROUND_TO_FOREGROUND = BASE_URL_APP + "appEnterForeground";

	/**
	 * source字段，表示来源
	 */
	String TELPHONE_SOURCE = "source";

	String BID_ID   = "bidId";
	String BID_TYPE = "bidType";
	/**
	 * 收到个推后的网络接口请求
	 *http://serverdomain/app/receiveGetPush?
	 * userId= & bidId= & bidType = &
	 * platform= &UUID= &version= &token=
	 */
	String URL_RECEIVE_GE_PUSH = BASE_URL_APP + "receiveGetPush?";

	String URL_GLOBAL_CONFIG = BASE_URL_APP +"global/params";

	//2016.05.03.add
	/**
	 * 静态页面公共URL
	 */
	String BASE_HTML_URL ="http://img.58cdn.com.cn/ds/zhaobiao/app_zhaobiao/html/";
	//2016.05.03.add end

	//  上传logger
	String UPLOAD_URL = BASE_URL + "log/upload";
}
