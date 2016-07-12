package com.huangyezhaobiao.tab;

import com.huangyezhaobiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2016/6/16.
 */
public class MainTabIndicatorBean {

    private final List<TabParam> pageParams = new ArrayList<>();

    public MainTabIndicatorBean() {
        initHomeTabs();
    }

    public void initHomeTabs(){
        this.pageParams.clear();
        TabParam localTabParam1 = new TabParam();
        localTabParam1.title = "抢单";
        localTabParam1.iconResId = R.drawable.main_btn_bid;
        localTabParam1.iconSelectedResId = R.drawable.main_btn_bid_pressed;
        this.pageParams.add(localTabParam1);

        TabParam localTabParam2 = new TabParam();
        localTabParam2.title = "消息";
        localTabParam2.iconResId = R.drawable.main_btn_message;
        localTabParam2.iconSelectedResId = R.drawable.main_btn_message_pressed;
        this.pageParams.add(localTabParam2);

        TabParam localTabParam3 = new TabParam();
        localTabParam3.title = "订单";
        localTabParam3.iconResId = R.drawable.main_btn_order;
        localTabParam3.iconSelectedResId = R.drawable.main_btn_order_pressed;
        this.pageParams.add(localTabParam3);

        TabParam localTabParam4 = new TabParam();
        localTabParam4.title = "我";
        localTabParam4.iconResId = R.drawable.main_btn_personal;
        localTabParam4.iconSelectedResId = R.drawable.main_btn_personal_pressed;
        this.pageParams.add(localTabParam4);
    }

    public List<TabParam> getTabParams() {
        return this.pageParams;
    }

    public static class TabParam {
        public int backgroundColor = R.color.white;
        public int iconResId; //未选中状态图标
        public int iconSelectedResId; //选中状态图标
        public int tabViewResId;
        public String title;  //标题
    }
}
