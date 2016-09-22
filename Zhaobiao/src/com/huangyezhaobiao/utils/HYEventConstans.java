package com.huangyezhaobiao.utils;

/**
 * author keyes
 * time 2016/5/16 15:55
 * email：1175426782@qq.com
 * param：
 * descript：
 */
public interface HYEventConstans {
    /**
     * 我的定单详情页---（不用切换）
     */
    String PAGE_MY_ORDER_DETAIL = "369";
    /**
     * 我的订单列表页---（不用切换）
     */
    String PAGE_MY_ORDER_LIST = "368";
    /**
     * 抢单详情页（可抢/已抢完）
     */
    String PAGE_BINDING_DETAIL = "367";
    /**
     * 抢单列表页（休息）
     */
    String PAGE_BINDING_LIST_REST = "366";
    /**
     * 抢单列表页（服务）
     */
    String PAGE_BINGING_LIST_SERVICE ="365";
    /**
     *抢单神器当日被打开
     */
    String EVENT_ID_APP_OPEND = "357";
    /**
     * 退单申诉：提交按钮
     */
    String EVENT_ID_REFUND_PAGE_SUBMIT = "329";

    /**
     * 退单申诉：选择图片“ ”按钮（第二次只可以加图片）
     */
    String EVENT_ID_REFUND_PAGE_ADD_PHOTO = "328";

    /**
     * 自定义接单设置页-各个完成按钮（统计内容）
     */
    String EVENT_ID_CUSTOMSETTING_PAGE = "327";

    /**
     * 更改手机绑定页-提交按钮
     */
    String EVENT_ID_CHANGE_MOBILE_PAGE_SUBMIT = "326";

    /**
     * 更改手机绑定页-获取验证码按钮
     */
    String EVENT_ID_CHANGE_MOBILE_PAGE_GET_CODE = "325";

    /**
     * 退出登录按钮
     */
    String EVENT_ID_LOGOUT = "324";

    /**
     * 余额刷新按钮
     */
    String EVENT_ID_MANUAL_REFRESH_BALANCE = "323";

    /**
     * 已抢详情通话按钮
     */
    String EVENT_ID_ORDER_DETAIL_PAGE_PHONE = "322";

    /**
     * 已抢列表通话按钮
     */
    String EVENT_ID_ORDER_DETAIL_REFUND = "321";

    /**
     * 已抢详情退单入口(只有待服务、1-7天可以退单)
     */
    String EVENT_ID_ORDER_LIST_PHONE = "320";

    /**
     * 待服务/服务中/已结束切换
     */
    String EVENT_ID_DONE_SERVICE_TAB = "315";


    /**
     * 抢单成功页面点击了查看抢单按钮
     */
    String EVENT_ID_SUCCESS_PAGE_LOOK_BIDDING = "314";

    /**
     * 抢单成功页面点击了继续抢单按钮
     */
    String EVENT_ID_SUCCESS_PAGE_CONTINUE_BIDDING = "313";

    /**
     * 弹窗页点击关闭按钮
     */
    String EVENT_ID_WINDOW_PAGE_CLOSE = "312";

    /**
     * 弹窗页点击了下一条按钮
     */
    String EVENT_ID_WINDOW_PAGE_NEXT = "311";

    /**
     * 弹窗页点击了声音按钮
     */
    String EVENT_ID_WINDOW_PAGE_VOLUME = "310";

    /**
     * 弹窗抢单按钮（服务模式、推送） 锁屏或者在应用外点击了抢单按钮
     */
    String EVENT_ID_WINDOW_PAGE_BIDDING = "308";

    /**
     * 商机不可抢的前提下由列表页进入详情页
     * 列表页item进入详情页（需分别记录是可抢状态下进去还是已抢完状态下进去）
     */
    String EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING = "307";

    /**
     * 详情页抢单按钮（服务、休息模式下）
     */
    String EVENT_ID_BIDDING_DETAIL_PAGE_BIDDING = "306";

    /**
     * 点击了我的抢单按钮
     */
    String EVENT_ID_MY_BIDDING = "305";

    /**
     * 个人中心按钮
     */
    String EVENT_ID_BIDDINGLIST_TO_PERSONAL = "304";

    /**
     * 列表抢单按钮（服务、休息模式下）
     */
    String EVENT_ID_BIDDING_LIST_PAGE_BIDDING = "303";

    /**
     * 主动拉取刷新
     */
    String EVENT_ID_BIDDING_LIAT_PAGE_MANUAL_REFRESH = "302";

    /**
     * 点击了应用内显示的消息bar
     */
    String EVENT_ID_MESSAGE_BAR = "301";

    /**
     * 点击了模式切换条
     */
    String EVENT_ID_CHANGE_MODE = "300";

    /**
     * 手机绑定页面点击了提交按钮
     */
    String EVENT_ID_MOBILE_BIND_PAGE_SUBMIT = "299";

    /**
     * 手机绑定页面点击了获取验证码按钮
     */
    String EVENT_ID_MOBILE_BIND_PAGE_GETCODE = "298";

    /**
     * 会员申请引导---打电话
     */
    String EVENT_ID_VIPREQUEST_GUIDE_CALL = "297";

    /**
     * 会员申请引导---成功注册58账号
     */
    String EVENT_ID_VIPREQUEST_SUCCESS_REGISTER = "296";

    /**
     * 会员申请引导---注册58账号按钮
     */
    String EVENT_ID_VIPREQUEST_REGISTER = "295";


    /**
     * 点击了2步成为抢单神器会员
     */
    String EVENT_ID_BECOME_TO_VIP = "294";

    /**
     * 点击了登录按钮
     */
    String EVENT_ID_LOGIN = "293";

    /**
     * 引导页
     */
    String GUIDE_PAGE = "397";
    /**
     * 使用协议
     */
    String USEAGE_PROTOCOL ="398";

    /**
     *导航栏-抢单
     */
    String INDICATOR_BIDDING_LIST_PAGE = "399";
    /**
     * 导航栏-消息
     */
    String INDICATOR_MESSAGE_PAGE = "400";
    /**
     * 导航栏-订单
     */
    String INDICATOR_ORDER_PAGE = "402";

    /**
     * 导航栏-我
     */
    String INDICATOR_PERSONAL_PAGE = "403";

    /**
     * 抢单成功页面
     */
    String  EVENT_ID_SUCCESS_PAGE = "404";

    /**
     * 抢单失败页面
     */
    String EVENT_ID_FAILURE_PAGE = "405";

    /**
     * 消息-抢单结果提醒入口
     */
    String EVENT_ID_RESULT_ALERT = "406";

    /**
     *结果提醒中“查看订单”入口
     */
    String EVENT_ID_CHECK_ORDER = "407";

    /**
     * 消息-客户联系提醒入口
     */
    String EVETN_ID_CONTACT_CLIENT = "408";

    /**
     *消息-通知资讯入口
     */
    String EVENT_SYSTEM_NOTICE = "409";

    /**
     * 订单信息展示区块（点击进入订单详情页）
     */
    String EVENT_ID_ORDER_DETAIL_PAGE = "410";

    /**
     * 筛选器按钮
     */
    String EVENT_ID_FILTER = "411";

    /**
     * 筛选器内重置按钮
     */
    String EVENT_ID_FILTER_RESET ="412";

    /**
     * 筛选器内确定按钮
     */
    String EVENT_ID_FILTER_CONFIRM = "413";

    /**
     * 相机按钮
     */
    String EVENT_ID_CAMERA = "414";

    /**
     * 已抢详情页短信发送按钮
     */
    String EVENT_ID_ORDER_DETAIL_PAGE_MESSAGE = "415";

    /**
     * 我的钱包入口
     */
    String EVENT_ID_MY_WALLET = "416";

    /**
     * 消费记录入口
     */
    String EVENT_ID_CONSUMPTION = "417";

    /**
     * 设置入口
     */
    String EVENT_ID_SETTING = "418";

    /**
     * 修改绑定手机号入口
     */
    String EVENT_ID_BIND_MOBILE = "419";

    /**
     * 自定义接单设置入口
     */
    String EVENT_ID_AUTO_SETTING ="420";

    /**
     * 帮助入口
     */
    String EVENT_ID_HELP = "421";

    /**
     * 关于入口
     */
    String EVENT_ID_ABOUT = "422";

    /**
     * 功能介绍页入口
     */
    String EVENT_ID_INTRODUCTION = "423";

    /**
     * 主列表停留时间
     */
    String PAGE_BINGING_LIST = "424";

    /**
     * 弹窗停留时间
     */
    String PAGE_POP_WINDOW = "425";


    /**
     * 使用另一个号码按钮点击
     */
    String EVENT_CHANGE_NUMBER = "427";

//    /**
//     * 更换号码成功之后的接听
//     */
//     String EVENT_ANOTHER_NUMBER_RECEIVE = "428";

    /**
     * 呼叫弹窗取消按钮点击
     */
    String EVENT_CALL_CANCEL = "429";

    /**
     * 呼叫弹窗接听按钮点击
     */
    String EVENT_CALL_CONFIRM = "430";

    /**
     * 打电话提示接听弹窗展现时间
     */
    String PAGE_DIALOG_CALL = "431";

    /**
     * 修改接听号码弹窗展现时间
     */
    String PAGE_DIALOG_INPUT = "432";

    /**
     * 退单申请页
     */
    String PAGE_REFUND = "433";

    /**
     * 个人中心首页
     */
    String PAGE_PERSONAL ="434";

    /**
     * 我的钱包
     */
    String PAGE_WALLET = "435";

    /**
     * 消费记录
     */
    String PAGE_RECORD_EXPENSES = "436";

    /**
     * 设置列表
     */
    String PAGE_SETTING_LIST ="437";

    /**
     * 自定义设置页面
     */
    String PAGE_AUTO_SETTING = "438";

    /**
     * 更改手机绑定页面
     */
    String PAGE_BIND_MOBILE = "439";

    /**
     * 消息中心首页列表
     */
    String PAGE_MESSAGE_LIST = "440";

    /**
     * 抢单结果提醒页
     */
    String PAGE_RESULT_ALERT = "441";

    /**
     * 客户联系提醒页
     */
    String PAGE_CONTACT_CLIENT = "442";

    /**
     * 系统通知
     */
    String PAGE_SYSTEM_NOTICE= "443";

    /**
     * 帮助页面
     */
    String PAGE_HELP ="444";

    /**
     * 关于列表
     */
    String PAGE_ABOUT= "445";

    /**
     * 功能介绍页
     */
    String PAGE_INTRODUCTION ="446";

    /**
     * 商家协议页
     */
    String PAGE_PROTOCOL ="447";

    /**
     * 登陆页面
     */
    String PAGE_LOGIN = "448";

    /**
     * 引导页
     */
    String PAGE_GUIDE = "449";

    /**
     * 手机绑定页面
     */
    String PAGE_FIRST_BIND_MOBILE = "450";

    /**
     * 子账号管理入口
     */
    String EVENT_ID_ACCOUNT = "504";

    /**
     * 子账号页面停留时间
     */
    String PAGE_ACCOUNT_MANANGE = "506";

    /**
     * 点击编辑按钮
     */
    String EVENT_ACCOUNT_EDIT = "507";

    /**
     * 点击完成按钮
     */
    String EVENT_ACCOUNT_FINISH = "508";

    /**
     * 点击子账号按钮
     */
    String EVENT_ACCOUNT_ADD = "509";

    /**
     * 增加子账号界面
     */
    String PAGE_ACCOUNT_ADD = "510";

    /**
     * 点击问号
     */
    String EVENT_ACCOUNT_HELP ="511";

    /**
     * 点击保存
     */
    String EVENT_ADD_ACCOUNT_SAVE ="512";

    /**
     * 修改子账号界面
     */
    String PAGE_ACCOUNT_EDIT = "513";

    /**
     * 点击保存
     */
    String EVENT_UPDATE_ACCOUNT_SAVE = "514";

    /**
     * 点击删除
     */
    String EVENT_ACCOUNT_DELETE = "515";

    /**
     * 点击修改
     */
    String EVENT_ACCOUNT_MODIFY = "516";

    /**
     * 商机列表停留时间
     */
    String PAGE_BUSINESS_OPPORTUNITY = "545";

    /**
     * 商机列表刷新按钮
     */
    String EVENT_BUSINESS_REFRESH = "546";

    /**
     * 商机购物车清空按钮
     */
    String EVENT_BUSINESS_CLEAR = "547";

    /**
     *商机购物车结算按钮
     */
    String EVENT_BUSINESS_SETTLEMENT = "548";

    /**
     * 结算提示弹窗停留时间
     */
    String PAGE_SETTLE_DIALOG ="549";

    /**
     * 结算提示弹窗确定按钮
     */
    String EVENT_SETTLE_DIALOG_CONFIRM = "550";

    /**
     *结算提示弹窗取消按钮
     */
    String EVENT_SETTLE_DIALOG_CANCEL = "551";

    /**
     * 结算弹窗勾选
     */
    String EVENT_SETTLE_DIALOG_CHECKED = "552";

    /**
     * 商机列表城市筛选
     */
    String EVENT_CITY_SELECT = "553";

    /**
     *商机列表时间筛选
     */
    String EVENT_TIME_SELECT = "554";

    /**
     * 购买成功页停留时间
     */
    String PAGE_PURCHASE_SUCCESS = "555";

    /**
     * 购买成功页继续购买按钮
     */
    String EVENT_GOON_PURCHASE = "556";

    /**
     * 购买成功页查看订单按钮
     */
    String EVENT_GOTOORDERLIST = "557";

    /**
     * 购买失败页停留时间
     */
    String PAGE_PURCHASE_FAILURE = "558";

    /**
     * 订单列表筛选器类别按钮
     */
    String EVENT_ORDER_CATEGORY = "559";

    /**
     *订单列表筛选器状态按钮
     */
    String EVENT_ORDER_STATE = "560";

    /**
     * 进入抢单订单详情
     */
    String EVENT_ORDER_DETAIL = "561";

    /**
     * 进入商机订单详情
     */
    String EVENT_BUSINESS_DETAIL = "562";

    /**
     * 商机订单详情页停留时间
     */
    String PAGE_BUSINESS_DETAIL = "563";

    /**
     * 联系记录弹窗停留时间
     */
    String PAGE_CONTACT_STYLE = "564";

    /**
     * 联系状态按钮
     */
    String EVENT_CONTACT_STATE ="565";

    /***
     * 商机分类提交按钮
     */
    String EVENT_CONTACT_SUBMIT = "566";
}