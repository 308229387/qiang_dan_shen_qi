package com.huangyezhaobiao.activity;


import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.huangye.commonlib.utils.UserConstans;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.fragment.home.BaseHomeFragment;
import com.huangyezhaobiao.fragment.home.BiddingFragment;
import com.huangyezhaobiao.fragment.home.MessageFragment;
import com.huangyezhaobiao.fragment.home.OrderListFragment;
import com.huangyezhaobiao.fragment.home.PersonalCenterFragment;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.log.LogInvocation;
import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.tab.MainTabFragmentAdapter;
import com.huangyezhaobiao.tab.MainTabIndicator;
import com.huangyezhaobiao.tab.MainTabIndicatorBean;
import com.huangyezhaobiao.tab.MainTabViewPager;
import com.huangyezhaobiao.utils.KeyguardUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UserUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends CommonFragmentActivity  {
    private static final String TAG = "MainActivity";
    private MainTabIndicator mIndicator;
    private MainTabViewPager mViewPager;
    private ArrayList<Fragment> mFragmentList;
    private MainTabFragmentAdapter mAdapter;
    private int mCurPageIndex = 0;


    KeyguardManager keyguardManager;
    KeyguardManager.KeyguardLock keyguardLock;

    private ScreenReceiver receiver;



    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }


    public void onEventMainThread(EventAction action) {

        switch (action.getType()) {
            case EVENT_TAB_RESET:
                LogUtils.LogV("MainActivity1","xxxxx");
                refreshTab();
                break;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserConstans.USER_ID = UserUtils.getUserId(this);
        UserUtils.getUserCompany(this);
        super.onCreate(savedInstanceState);
        keyguardManager = (KeyguardManager) getApplication().getSystemService(KEYGUARD_SERVICE);
        keyguardLock = keyguardManager.newKeyguardLock("");
        setContentView(R.layout.activity_main_page);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        getWindow().setBackgroundDrawable(null);
        initView();
        initMainNavigateTab();
        refreshTab();
        initPage();
        registerScreenOffReceiver();
        startBindService();
    }

    @Override
    protected void onResume() {
        UserConstans.USER_ID = UserUtils.getUserId(this);
        EventbusAgent.getInstance().register(this);
        refreshTab();
        super.onResume();

        if(BidSuccessActivity.isReset){
            BidSuccessActivity.isReset = false;
            initPage();
        }

    }


    public void initView() {
        mIndicator = (MainTabIndicator) this.findViewById(R.id.id_indicator);
        mViewPager = (MainTabViewPager) this.findViewById(R.id.id_pager);
    }

    /**
     * 开启myService的进程
     */
    private void startBindService() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }


    /**
     * 注册屏幕暗时的广播接收者
     */
    private void registerScreenOffReceiver() {
        if (receiver == null) {
            receiver = new ScreenReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            registerReceiver(receiver, filter);
        }

    }


    /**
     * 解绑
     */
    private void unregisterScreenOffReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }

    }

    private class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent == null) return;
            if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                KeyguardUtils.SCREEN_ON = true;
                KeyguardUtils.need_lock = false;

            } else {
                KeyguardUtils.need_lock = true;
                KeyguardUtils.SCREEN_ON = false;
                KeyguardUtils.notLock = false;
                openKeyguard();
                closeKeyguard();
                BiddingApplication biddingApplication = (BiddingApplication) getApplication();
                if (biddingApplication.activity instanceof LockActivity) {
                    LockActivity lockActivity = (LockActivity) biddingApplication.activity;
                    if (lockActivity != null) {
                        lockActivity.closeLock();
                    }
                }
            }
        }
    }


    /**
     * 开锁
     */
    private void openKeyguard() {
        keyguardLock.disableKeyguard();
    }

    /**
     * 锁屏代码
     */
    private void closeKeyguard() {
        keyguardLock.reenableKeyguard();
    }

    private void initMainNavigateTab() {
        mFragmentList = new ArrayList(4);
        mFragmentList.add(new BiddingFragment());
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new OrderListFragment());
        mFragmentList.add(new PersonalCenterFragment());
        mAdapter = new MainTabFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setScanScroll(false);
        mIndicator.setNavigateTab(new MainTabIndicatorBean());
        mIndicator.setOnTabSelectedListener(new MainTabIndicator.OnTabSelectedListener() {

            @Override
            public void onTabReselected(int paramInt) {
                procTabClick(paramInt);
            }

        });

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int tabIndex = position;
//                if (position >= AppConstants.HOME_TAB_POST) {
//                    tabIndex = position + 1;
//                }
                mIndicator.setCurrentTab(tabIndex);
                AppConstants.HOME_PAGE_INDEX = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        tab_post_icon.setOnClickListener(this);
//        if (AppPrefersUtil.ins().isFirstEntryHomePage()) {
//            /*floating_layer_ll.setVisibility(View.VISIBLE);
//            floating_layer_ll.setOnClickListener(this);
//            close_floating_layer_iv.setOnClickListener(this);*/
//        }
    }

    private void procTabClick(int paramInt) {
        int pageIndex = paramInt;
//        if (paramInt > AppConstants.HOME_TAB_POST) {
//            pageIndex = paramInt - 1;
//        }
//        if (pageIndex == AppConstants.HOME_PAGE_MESSAGE) {
//            if (!PersonalPrefersUtil.ins().isCompleteLogin()) {
//                PageSwitchHelper.gotoCompleteLoginPage(MainActivity.this);
//                return;
//            }
//        }
//        if (pageIndex == AppConstants.HOME_PAGE_PERSONAL_CENTER) {
//            AppPrefersUtil.ins().deleteHomeCenterVersionUpdateFlag();
//        }

        mIndicator.setCurrentTab(paramInt);
        if (mViewPager != null && pageIndex < mViewPager.getAdapter().getCount()) {
            mViewPager.setCurrentItem(pageIndex, false);
            int counts = mFragmentList.size();
            BaseHomeFragment fragment;
            for (int index = 0; index < counts; index++) {
                fragment = (BaseHomeFragment) mFragmentList.get(index);
                if (pageIndex == index) {
                    fragment.OnFragmentSelectedChanged(true);
                    BaseHomeFragment.current_index = index;
                } else if (AppConstants.HOME_PAGE_INDEX == index) {
                    fragment.OnFragmentSelectedChanged(false);
                }
            }
        }
        AppConstants.HOME_PAGE_INDEX = pageIndex;
    }

    private void initPage() {
        int pageIndex = mCurPageIndex;
        setViewPage(pageIndex);
    }

    public void setViewPage(int paramInt) {
        if (paramInt >= 0) {
            try {
                int tabIndex = paramInt;
//                if (paramInt >= AppConstants.HOME_TAB_POST) {
//                    tabIndex = paramInt + 1;
//                }
                if ((mIndicator == null) || (mIndicator.getMainNavigateTab() == null)
                        || (tabIndex < mIndicator.getMainNavigateTab().getTabParams().size())) {
                    mIndicator.onClickSelectedTab(tabIndex);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void refreshTab() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int num0 = UnreadUtils.getNewOrder(MainActivity.this);
                if (num0 > 0) {
                    mIndicator.showNewTag(AppConstants.HOME_TAB_BIDDING, num0);
                } else {
                    mIndicator.hideNewTag(AppConstants.HOME_TAB_BIDDING);
                }

                int num1 = UnreadUtils.getAllNum(MainActivity.this);
                if (num1 > 0) {
                    mIndicator.showNewTag(AppConstants.HOME_TAB_MESSAGE, num1);
                } else {
                    mIndicator.hideNewTag(AppConstants.HOME_TAB_MESSAGE);
                }

                int num2 = UnreadUtils.getQDResult(MainActivity.this);
                if (num2 > 0) {
                    mIndicator.showNewTag(AppConstants.HOME_TAB_ORDER, num2);
                } else {
                    mIndicator.hideNewTag(AppConstants.HOME_TAB_ORDER);
                }


            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        EventbusAgent.getInstance().unregister(this);
    }

    @Override
    protected void onDestroy() {
        unregisterScreenOffReceiver();
        super.onDestroy();

        releaseSource();
        System.gc();
    }

    private void releaseSource() {
        //释放数据
        LogInvocation.destroy();
    }


}
