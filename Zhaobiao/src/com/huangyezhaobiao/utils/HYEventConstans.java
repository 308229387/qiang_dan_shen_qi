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
     * 点击了服务中的tab
     */
//    int EVENT_ID_IN_SERVICE_TAB = "InServiceTab";

    /**
     * 点击了待服务的tab
     */
//    int EVENT_ID_UN_SERVICE_TAB = "unServiceTab";



    /**
     * 抢单成功页面点击了查看抢单按钮
     */
    String EVENT_ID_SUCCESS_PAGE_LOOK_BIDDING = "314";

    /**
     * 抢单成功页面点击了继续抢单按钮
     */
    String EVENT_ID_SUCCESS_PAGE_CONTINUE_BIDDING = "313";

    /**
     * 锁屏或者在应用外点击了抢单按钮
     */
//    int EVENT_ID_OUT_WINDOW_PAGE_BIDDING = "biddingWindowOutPage";

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
     * 弹窗抢单按钮（服务模式、推送）
     */
    String EVENT_ID_WINDOW_PAGE_BIDDING = "308";

    /**
     * 商机不可抢的前提下由列表页进入详情页
     * 列表页item进入详情页（需分别记录是可抢状态下进去还是已抢完状态下进去）
     */
    String EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING = "307";

    /**
     * 商机可抢的前提下由列表页进入详情页
     */
//    int EVENT_ID_BIDDING_LIST_TO_DETAIL_ENABLE_BIDDING = "goDetailCanBid";

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
     * 点击了服务模式按钮
     */
//    int EVENT_ID_SERVICE_MODE = "serviceMode";

    /**
     * 点击了休息模式按钮
     */
//    int EVENT_ID_REST_MODE = "restMode";


}