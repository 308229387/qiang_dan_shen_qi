package wuba.zhaobiao.common.model;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.BidSuccessActivity;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.UserPhoneBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.tab.MainTabFragmentAdapter;
import com.huangyezhaobiao.tab.MainTabIndicator;
import com.huangyezhaobiao.tab.MainTabIndicatorBean;
import com.huangyezhaobiao.tab.MainTabViewPager;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.PhoneUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.VersionUtils;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.activity.HomePageActivity;
import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.config.ScreenReceiver;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.grab.fragment.GrabFragment;
import wuba.zhaobiao.grab.fragment.GrabTestFragment;
import wuba.zhaobiao.message.fragment.MessageFragment;
import wuba.zhaobiao.mine.fragment.MineFragment;
import wuba.zhaobiao.order.fragment.OrderFragment;
import wuba.zhaobiao.respons.GetWltStateRespons;
import wuba.zhaobiao.utils.AutoSettingDialogUtils;
import wuba.zhaobiao.utils.LogoutDialogUtils;
import wuba.zhaobiao.utils.UpdateDialogUtils;

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
    private static Boolean isExit = false;
    private int DEFALT_FRAGMENT_NUM = 0;

    int currentVersion = -1; //当前版本号
    int versionNum = -1; //获取当前系统版本号
    private boolean forceUpdate = false; //是否强制更新

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
        mFragmentList.add(new GrabFragment());
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new OrderFragment());
        mFragmentList.add(new MineFragment());
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

    public void getWltOnlineStateAndPhoneNumAndIsNeedUpdateAfterFirstSetting() {
        OkHttpUtils.post(Urls.WLT_CHECK)
                .params("deviceId", PhoneUtils.getIMEI(context))
                .execute(new GetWltState(context));
    }

    private void saveUserSetState(GetWltStateRespons wltStateRespons) {
        String setState = getUserSetState(wltStateRespons);
        if (!TextUtils.isEmpty(setState))
            SPUtils.saveKV(context, GlobalConfigBean.KEY_SETSTATE, setState);
    }

    private String getUserSetState(GetWltStateRespons wltStateRespons) {
        try {
            return wltStateRespons.getData().getAppUserSet().getSetState();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private void savePhoneAndWltState(GetWltStateRespons wltStateRespons) {
        jugedPhoneNumState(wltStateRespons);
        saveWltState(wltStateRespons);
    }

    private void jugedPhoneNumState(GetWltStateRespons wltStateRespons) {
        String status = getStatus(wltStateRespons);
        if (TextUtils.equals(UserPhoneBean.SUCCESS, status))
            savePhoneNumToSP(wltStateRespons);
    }

    private String getStatus(GetWltStateRespons wltStateRespons) {
        try {
            return wltStateRespons.getData().getUserPhoneResult().getStatus();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private void savePhoneNumToSP(GetWltStateRespons wltStateRespons) {
        String userPhone = getPhoneNumToSP(wltStateRespons);
        SPUtils.saveKV(context, GlobalConfigBean.KEY_USERPHONE, userPhone);
    }

    private String getPhoneNumToSP(GetWltStateRespons wltStateRespons) {
        try {
            return wltStateRespons.getData().getUserPhoneResult().getUserPhone();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private void saveWltState(GetWltStateRespons wltStateRespons) {
        saveWltExpireState(wltStateRespons);
        saveWltMsg(wltStateRespons);
    }

    private void saveWltExpireState(GetWltStateRespons wltStateRespons) {
        String expireState = getExpireState(wltStateRespons);
        SPUtils.saveKV(context, GlobalConfigBean.KEY_WLT_EXPIRE, expireState);
    }

    private String getExpireState(GetWltStateRespons wltStateRespons) {
        try {
            return wltStateRespons.getData().getWltAlertResult().getExpireState();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private void saveWltMsg(GetWltStateRespons wltStateRespons) {
        String msg = getSaveWltMsg(wltStateRespons);
        SPUtils.saveKV(context, GlobalConfigBean.KEY_WLT_EXPIRE_MSG, msg);
    }

    private String getSaveWltMsg(GetWltStateRespons wltStateRespons) {
        try {
            return wltStateRespons.getData().getWltAlertResult().getMsg();
        } catch (Exception e) {
            return e.getMessage();
        }
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
        if (num0 > 0)
            mIndicator.showNewTag(AppConstants.HOME_TAB_BIDDING, num0);
        else
            mIndicator.hideNewTag(AppConstants.HOME_TAB_BIDDING);
    }

    private void checkWhenIndicatorEqualsOne() {
        int num1 = UnreadUtils.getAllNum(context);
        if (num1 > 0)
            mIndicator.showNewTag(AppConstants.HOME_TAB_MESSAGE, num1);
        else
            mIndicator.hideNewTag(AppConstants.HOME_TAB_MESSAGE);
    }

    private void checkWhenIndicatorEqualsTwo() {
        int num2 = UnreadUtils.getQDResult(context);
        if (num2 > 0)
            mIndicator.showNewTag(AppConstants.HOME_TAB_ORDER, num2);
        else
            mIndicator.hideNewTag(AppConstants.HOME_TAB_ORDER);
    }

    public void afterBidSuccessBack() {
        if (BidSuccessActivity.isReset) {
            BidSuccessActivity.isReset = false;
            initSelectPage();
        }
    }

    public void initSelectPage() {
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

    public void checkIsLongTimeNotLogout() {
        if (beyondLogoutTime() && userIdNotEmpty())
            new LogoutDialogUtils(context, "您已长时间无登录操作，请重新登录").showSingleButtonDialog();
    }

    private boolean beyondLogoutTime() {
        long currentTimeLine = System.currentTimeMillis();
        long latTimeLine = UserUtils.getSessionTime(context);
        return TimeUtils.beyond13Days(currentTimeLine, latTimeLine);
    }

    private boolean userIdNotEmpty() {
        return !TextUtils.isEmpty(UserUtils.getUserId(context));
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
        for (int index = 0; index < counts; index++)
            checkWhichOne(paramInt, index);
    }

    private void checkWhichOne(int paramInt, int index) {
        BaseFragment
                fragment = (BaseFragment) mFragmentList.get(index);
        if (paramInt == index) {
            changeFragmentStateForTrue(fragment, index);
        } else if (AppConstants.HOME_PAGE_INDEX == index) {
            changeFragmentStateForFalse(fragment);
        }
    }

    private void changeFragmentStateForTrue(BaseFragment fragment, int index) {
        fragment.OnFragmentSelectedChanged(true);
        BaseFragment.current_index = index;
    }

    private void changeFragmentStateForFalse(BaseFragment fragment) {
        fragment.OnFragmentSelectedChanged(false);
    }

    public void unregistEvenBus() {
        EventbusAgent.getInstance().unregister(context);
    }

    public void exitBy2Click() {
        if (isExit == false)
            logoutRemind();
        else
            logout();
    }

    private void logoutRemind() {
        Timer tExit;
        isExit = true;
        ToastUtils.showToast("再按一次退出程序");
        tExit = new Timer();
        exitTimeRunTask(tExit);
    }

    private void logout() {
        BiddingApplication.getInstance().exit();
        System.exit(0);
    }

    private void exitTimeRunTask(Timer tExit) {
        tExit.schedule(new TimerTask() {
            @Override
            public void run() {
                isExit = false;
            }
        }, 2000);
    }

    private void saveInfoAndJugeUpdate(GetWltStateRespons WltStateRespons, @Nullable Response response) {
        saveUserSetState(WltStateRespons);
        savePhoneAndWltState(WltStateRespons);
        getVerSionAndJugeIsNeedUpdate(response);
    }

    private void getVerSionAndJugeIsNeedUpdate(@Nullable Response response) {
        Headers responseHeadersString = response.headers();
        String version = responseHeadersString.get("version");
        if (!UserUtils.isNeedUpdate(context) && version != null)
            getVersion(version);
    }

    private void getVersion(String version) {
        getVersionForCompare(version);
        compare();
    }

    private void getVersionForCompare(String version) {
        try {
            jugeVersionAndGet(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jugeVersionAndGet(String version) throws PackageManager.NameNotFoundException {
        if (version.contains("F")) {
            constansF(version);
        } else {
            versionNum = Integer.parseInt(version);
        }
        currentVersion = Integer.parseInt(VersionUtils.getVersionCode(context));
    }

    private void constansF(String version) {
        forceUpdate = true;
        String[] fs = version.split("F");
        String versionCode = fs[0];
        versionNum = Integer.parseInt(versionCode);
    }

    private void compare() {
        compareVersion(versionNum, currentVersion, forceUpdate);
    }

    private void compareVersion(int netVersion, int localVersion, boolean isForceUpdate) {
        UpdateManager.getUpdateManager().isUpdateNow(context, netVersion, localVersion, URLConstans.DOWNLOAD_ZHAOBIAO_ADDRESS, isForceUpdate);
        alreadyNewVersion();
    }

    private void alreadyNewVersion() {
        if (UpdateManager.needUpdate)
            isFirstUpdate();
    }

    private void isFirstUpdate() {
        if (SPUtils.isFirstUpdate(context))
            new UpdateDialogUtils(context, context.getString(R.string.update_message)).showSingleButtonDialog();
        else
            oldUserUpdate();
    }

    private void oldUserUpdate() {
        String isSet = SPUtils.getVByK(context, GlobalConfigBean.KEY_SETSTATE);
        if (!TextUtils.equals("1", isSet) && SPUtils.isAutoSetting(context))
            new AutoSettingDialogUtils(context, context.getString(R.string.auto_setting_message)).showTwoButtonDialog();
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

    private class GetWltState extends DialogCallback<GetWltStateRespons> {
        public GetWltState(Activity activity) {
            super(activity);
        }

        @Override
        public void onResponse(boolean isFromCache, GetWltStateRespons WltStateRespons, Request request, @Nullable Response response) {
            saveInfoAndJugeUpdate(WltStateRespons, response);

        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            if (!isToast) {
                ToastUtils.showToast(e.getMessage());
            }
        }
    }

}
