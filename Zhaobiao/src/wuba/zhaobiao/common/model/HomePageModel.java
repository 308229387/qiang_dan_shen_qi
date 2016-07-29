package wuba.zhaobiao.common.model;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.BidSuccessActivity;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.fragment.home.BaseHomeFragment;
import com.huangyezhaobiao.fragment.home.BiddingFragment;
import com.huangyezhaobiao.fragment.home.MessageFragment;
import com.huangyezhaobiao.fragment.home.OrderListFragment;
import com.huangyezhaobiao.fragment.home.PersonalCenterFragment;
import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.tab.MainTabFragmentAdapter;
import com.huangyezhaobiao.tab.MainTabIndicator;
import com.huangyezhaobiao.tab.MainTabIndicatorBean;
import com.huangyezhaobiao.tab.MainTabViewPager;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.UnreadUtils;

import java.util.ArrayList;

import wuba.zhaobiao.common.activity.HomePageActivity;
import wuba.zhaobiao.config.ScreenReceiver;

/**
 * Created by SongYongmeng on 2016/7/29.
 */
public class HomePageModel extends BaseModel {
    public HomePageActivity context;
    private MainTabIndicator mIndicator;
    private MainTabViewPager mViewPager;
    private ArrayList<Fragment> mFragmentList;
    private MainTabFragmentAdapter mAdapter;
    private ScreenReceiver receiver;

    private int DEFALT_FRAGMENT_NUM = 0;

    public void registEvenBus() {
        EventbusAgent.getInstance().register(context);
    }

    public void startBindService() {
        Intent intent = new Intent(context, MyService.class);
        context.startService(intent);
    }

    public void eventBusThing(EventAction action) {

        switch (action.getType()) {
            case EVENT_TAB_RESET:
                LogUtils.LogV("MainActivity1","xxxxx");
                refreshTab();
                break;
        }
    }

    public void registerScreenOffReceiver() {
        if (receiver == null) {
            receiver = new ScreenReceiver(context);
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            context.registerReceiver(receiver, filter);
        }
    }

    public void unregisterScreenOffReceiver() {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public void setTobBarColor() {
        context.getWindow().setBackgroundDrawable(null);
        int flag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            context.getWindow().addFlags(flag);
    }

    public void initView() {
        mIndicator = (MainTabIndicator) context.findViewById(R.id.id_indicator);
        mViewPager = (MainTabViewPager) context.findViewById(R.id.id_pager);
    }

    public void addFragmentToList() {
        mFragmentList = new ArrayList(4);
        mFragmentList.add(new BiddingFragment());
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new OrderListFragment());
        mFragmentList.add(new PersonalCenterFragment());
    }

    public void configViewPagerAndButton() {
        mAdapter = new MainTabFragmentAdapter(context.getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setScanScroll(false);
        mIndicator.setNavigateTab(new MainTabIndicatorBean());
        mIndicator.setOnTabSelectedListener(new TabSelectListener());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new PageChangeListener());
    }

    private void procTabClick(int paramInt) {
        if (mViewPager != null && paramInt < mViewPager.getAdapter().getCount()) {
            changePage(paramInt);
            changeFragmentState(paramInt);
        }
    }

    private void changePage(int paramInt) {
        mViewPager.setCurrentItem(paramInt, false);
        mIndicator.setCurrentTab(paramInt);
        AppConstants.HOME_PAGE_INDEX = paramInt;
    }

    private void changeFragmentState(int paramInt) {
        int counts = mFragmentList.size();
        BaseHomeFragment fragment;
        for (int index = 0; index < counts; index++) {
            fragment = (BaseHomeFragment) mFragmentList.get(index);
            if (paramInt == index) {
                fragment.OnFragmentSelectedChanged(true);
                BaseHomeFragment.current_index = index;
            } else if (AppConstants.HOME_PAGE_INDEX == index) {
                fragment.OnFragmentSelectedChanged(false);
            }
        }
    }

    public void afterBidSuccessBack() {
        if (BidSuccessActivity.isReset) {
            BidSuccessActivity.isReset = false;
            initPage();
        }
    }

    private void initPage() {
        int pageIndex = DEFALT_FRAGMENT_NUM;
        setViewPage(pageIndex);
    }

    public void setViewPage(int paramInt) {
        try {
            int tabIndex = paramInt;
            if ((mIndicator == null) || (mIndicator.getMainNavigateTab() == null)
                    || (tabIndex < mIndicator.getMainNavigateTab().getTabParams().size())) {
                mIndicator.onClickSelectedTab(tabIndex);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTab() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int num0 = UnreadUtils.getNewOrder(context);
                if (num0 > 0) {
                    mIndicator.showNewTag(AppConstants.HOME_TAB_BIDDING, num0);
                } else {
                    mIndicator.hideNewTag(AppConstants.HOME_TAB_BIDDING);
                }

                int num1 = UnreadUtils.getAllNum(context);
                if (num1 > 0) {
                    mIndicator.showNewTag(AppConstants.HOME_TAB_MESSAGE, num1);
                } else {
                    mIndicator.hideNewTag(AppConstants.HOME_TAB_MESSAGE);
                }

                int num2 = UnreadUtils.getQDResult(context);
                if (num2 > 0) {
                    mIndicator.showNewTag(AppConstants.HOME_TAB_ORDER, num2);
                } else {
                    mIndicator.hideNewTag(AppConstants.HOME_TAB_ORDER);
                }

            }
        });
    }


    public void unregistEvenBus() {
        EventbusAgent.getInstance().unregister(context);
    }

    public HomePageModel(HomePageActivity context) {
        this.context = context;
    }

    public class TabSelectListener implements MainTabIndicator.OnTabSelectedListener {

        @Override
        public void onTabReselected(int paramInt) {
            procTabClick(paramInt);
        }
    }

    public class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int tabIndex = position;
            mIndicator.setCurrentTab(tabIndex);
            AppConstants.HOME_PAGE_INDEX = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
