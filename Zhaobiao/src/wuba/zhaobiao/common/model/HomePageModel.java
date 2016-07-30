package wuba.zhaobiao.common.model;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.BidSuccessActivity;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
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

    public void setTobBarColor() {
        context.getWindow().setBackgroundDrawable(null);
        int flag = creatTopBarParams();
        setTopBarParams(flag);
    }

    private int creatTopBarParams() {
        return WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    }

    private void setTopBarParams(int flag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            context.getWindow().addFlags(flag);
    }

    public void initViewPagerAndButton() {
        mIndicator = (MainTabIndicator) context.findViewById(R.id.id_indicator);
        mViewPager = (MainTabViewPager) context.findViewById(R.id.id_pager);
    }

    public void addFragmentToList() {
        creatFragmentList();
        addFragment();
    }

    private void creatFragmentList() {
        mFragmentList = new ArrayList(4);
    }

    private void addFragment() {
        mFragmentList.add(new BiddingFragment());
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new OrderListFragment());
        mFragmentList.add(new PersonalCenterFragment());
    }

    public void configViewPagerAndButton() {
        creatAdapter();
        setAdapterForViewPager();
        configViewPager();
        configIndicator();
    }

    private void creatAdapter() {
        mAdapter = new MainTabFragmentAdapter(context.getSupportFragmentManager(), mFragmentList);
    }

    private void setAdapterForViewPager() {
        mViewPager.setAdapter(mAdapter);
    }

    private void configViewPager() {
        mViewPager.setScanScroll(false);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new PageChangeListener());
    }

    private void configIndicator() {
        mIndicator.setNavigateTab(new MainTabIndicatorBean());
        mIndicator.setOnTabSelectedListener(new TabSelectListener());
    }

    public void registEvenBus() {
        EventbusAgent.getInstance().register(context);
    }

    public void startBindService() {
        Intent intent = new Intent(context, MyService.class);
        context.startService(intent);
    }

    public void registerScreenOffReceiver() {
        if (receiver == null)
            doRegist();
    }

    public void eventBusThing(EventAction action) {
        if (action.getType() == EventType.EVENT_TAB_RESET)
            refreshTab();
    }

    public void refreshTab() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkWhenIndicatorEqualsZero();
                checkWhenIndicatorEqualsOne();
                checkWhenIndicatorEqualsTwo();
            }
        });
    }

    private void checkWhenIndicatorEqualsZero() {
        int num0 = UnreadUtils.getNewOrder(context);
        if (num0 > 0) {
            mIndicator.showNewTag(AppConstants.HOME_TAB_BIDDING, num0);
        } else {
            mIndicator.hideNewTag(AppConstants.HOME_TAB_BIDDING);
        }
    }

    private void checkWhenIndicatorEqualsOne() {
        int num1 = UnreadUtils.getAllNum(context);
        if (num1 > 0) {
            mIndicator.showNewTag(AppConstants.HOME_TAB_MESSAGE, num1);
        } else {
            mIndicator.hideNewTag(AppConstants.HOME_TAB_MESSAGE);
        }
    }

    private void checkWhenIndicatorEqualsTwo() {
        int num2 = UnreadUtils.getQDResult(context);
        if (num2 > 0) {
            mIndicator.showNewTag(AppConstants.HOME_TAB_ORDER, num2);
        } else {
            mIndicator.hideNewTag(AppConstants.HOME_TAB_ORDER);
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
        int tabIndex = paramInt;
        if (viewPagerStateIsRight(tabIndex)) {
            mIndicator.onClickSelectedTab(tabIndex);
            return;
        }
    }

    private boolean viewPagerStateIsRight(int tabIndex) {
        return (mIndicator == null) || (mIndicator.getMainNavigateTab() == null)
                || (tabIndex < mIndicator.getMainNavigateTab().getTabParams().size());
    }

    public void unregisterScreenOffReceiver() {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private void doRegist() {
        receiver = new ScreenReceiver(context);
        IntentFilter filter = creatFilter();
        context.registerReceiver(receiver, filter);
    }

    @NonNull
    private IntentFilter creatFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        return filter;
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
        turnChangeState(paramInt, counts);
    }

    private void turnChangeState(int paramInt, int counts) {
        for (int index = 0; index < counts; index++) {
            checkWhichOne(paramInt, index);
        }
    }

    private void checkWhichOne(int paramInt, int index) {
        BaseHomeFragment
                fragment = (BaseHomeFragment) mFragmentList.get(index);
        if (paramInt == index) {
            changeFragmentStateForTrue(fragment, index);
        } else if (AppConstants.HOME_PAGE_INDEX == index) {
            changeFragmentStateForFalse(fragment);
        }
    }

    private void changeFragmentStateForTrue(BaseHomeFragment fragment, int index) {
        fragment.OnFragmentSelectedChanged(true);
        BaseHomeFragment.current_index = index;
    }

    private void changeFragmentStateForFalse(BaseHomeFragment fragment) {
        fragment.OnFragmentSelectedChanged(false);
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
