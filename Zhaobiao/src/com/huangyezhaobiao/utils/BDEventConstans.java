package com.huangyezhaobiao.utils;

/**
 * Created by 58 on 2016/2/15.
 * 百度统计自定义事件的Id常量
 */
public interface BDEventConstans {
    /**
     * 退单页面点击了提交按钮的事件id
     */
    String EVENT_ID_REFUND_PAGE_SUBMIT = "submitRefund";

    /**
     * 退单页面点击了+号按钮
     */
    String EVENT_ID_REFUND_PAGE_ADD_PHOTO = "addPhotoInRefund";

    /**
     * 更改手机号页面点击了提交按钮
     */
    String EVENT_ID_CHANGE_MOBILE_PAGE_SUBMIT = "submitInChangePage";

    /**
     * 更改手机号页面点击了获取验证码按钮
     */
    String EVENT_ID_CHANGE_MOBILE_PAGE_GET_CODE = "getCodeInChangePage";

    /**
     * 点击了退出登录按钮
     */
    String EVENT_ID_LOGOUT = "logout";

    /**
     * 点击了刷新余额按钮
     */
    String EVENT_ID_MANUAL_REFRESH_BALANCE = "manualRefreshBalance";

    /**
     * 点击了已抢详情的电话按钮
     */
    String EVENT_ID_ORDER_DETAIL_PAGE_PHONE = "phoneDetail";

    /**
     * 点击了已抢详情的退单入口
     */
    String EVENT_ID_ORDER_DETAIL_REFUND = "refundDetail";

    /**
     * 点击了已抢列表的电话按钮
     */
    String EVENT_ID_ORDER_LIST_PHONE = "phoneList";

    /**
     * 点击了已结束的tab
     */
    String EVENT_ID_DONE_SERVICE_TAB = "doneServiceTab";

    /**
     * 点击了服务中的tab
     */
    String EVENT_ID_IN_SERVICE_TAB = "InServiceTab";

    /**
     * 点击了待服务的tab
     */
    String EVENT_ID_UN_SERVICE_TAB = "unServiceTab";

    /**
     * 点击了我的抢单按钮
     */
    String EVENT_ID_MY_BIDDING = "myBusiness";

    /**
     * 抢单成功页面点击了查看抢单按钮
     */
    String EVENT_ID_SUCCESS_PAGE_LOOK_BIDDING = "lookBidding";

    /**
     * 抢单成功页面点击了继续抢单按钮
     */
    String EVENT_ID_SUCCESS_PAGE_CONTINUE_BIDDING = "continueBidding";

    /**
     * 锁屏或者在应用外点击了抢单按钮
     */
    String EVENT_ID_OUT_WINDOW_PAGE_BIDDING = "biddingWindowOutPage";

    /**
     * 弹窗页点击关闭按钮
     */
    String EVENT_ID_WINDOW_PAGE_CLOSE ="closeWindow";

    /**
     * 弹窗页点击了下一条按钮
     */
    String EVENT_ID_WINDOW_PAGE_NEXT = "nextWindow";

    /**
     * 弹窗页点击了声音按钮
     */
    String EVENT_ID_WINDOW_PAGE_VOLUME = "volumeWindow";

    /**
     * 应用内弹窗点击了抢单按钮
     */
    String EVENT_ID_WINDOW_PAGE_BIDDING = "biddingWindowInPage";

    /**
     * 商机不可抢的前提下由列表页进入详情页
     */
    String EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING = "goDetailCannotBid";

    /**
     * 商机可抢的前提下由列表页进入详情页
     */
    String EVENT_ID_BIDDING_LIST_TO_DETAIL_ENABLE_BIDDING = "goDetailCanBid";

    /**
     * 在商机详情页点击抢单按钮
     */
    String EVENT_ID_BIDDING_DETAIL_PAGE_BIDDING = "biddingDetail";

    /**
     * 在商机列表页点击抢单按钮
     */
    String EVENT_ID_BIDDING_LIST_PAGE_BIDDING = "biddingList";

    /**
     * 商机列表页主动下拉刷新
     */
    String EVENT_ID_BIDDING_LIAT_PAGE_MANUAL_REFRESH = "manualRefresh";

    /**
     * 点击了应用内显示的消息bar
     */
    String EVENT_ID_MESSAGE_BAR = "messageBar";

    /**
     * 手机绑定页面点击了提交按钮
     */
    String EVENT_ID_MOBILE_BIND_PAGE_SUBMIT = "submitInBindPage";

    /**
     * 手机绑定页面点击了获取验证码按钮
     */
    String EVENT_ID_MOBILE_BIND_PAGE_GETCODE = "getCodeInBindPage";

    /**
     * 点击了登录按钮
     */
    String EVENT_ID_LOGIN = "login";

    /**
     * 点击了服务模式按钮
     */
    String EVENT_ID_SERVICE_MODE = "serviceMode";

    /**
     * 点击了休息模式按钮
     */
    String EVENT_ID_REST_MODE = "restMode";
}
