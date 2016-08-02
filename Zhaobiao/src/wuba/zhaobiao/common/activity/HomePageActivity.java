package wuba.zhaobiao.common.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

import wuba.zhaobiao.common.model.HomePageModel;

/**
 * Created by SongYongmeng on 2016/7/29.
 * 描    述：首页，设置锁屏弹窗、设置顶部透明、注册EventBus、添加fragment到列表、配置Viewpager和底部按钮、每次onResume检查红点及抢单成功
 */
public class HomePageActivity extends BaseActivity<HomePageModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        init();
    }

    private void init() {
        initLayout();
        initEvenBus();
        registService();
        checkWltOnLineState();
    }

    private void initLayout() {
        addLayout();
        addFragment();
    }

    private void addLayout() {
        model.setTobBarColor();
        model.initViewPagerAndButton();
    }

    private void addFragment() {
        model.addFragmentToList();
        model.configViewPagerAndButton();
        model.initSelectPage();
    }

    private void initEvenBus() {
        model.registEvenBus();
    }

    private void registService() {
        model.startBindService();
        model.registerScreenOffReceiver();
    }

    private void checkWltOnLineState() {
        model.getWltOnlineStateAndPhoneNum();
    }

    public void onEventMainThread(EventAction action) {
        model.eventBusThing(action);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRedDotAndLandedTime();
    }

    private void checkRedDotAndLandedTime() {
        checkRedDotAndGrabBack();
        checkIsLongTimeNotLogout();
    }

    private void checkRedDotAndGrabBack() {
        model.refreshTab();
        model.afterBidSuccessBack();
    }

    private void checkIsLongTimeNotLogout() {
        model.checkIsLongTimeNotLogout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistService();
    }

    private void unRegistService() {
        model.unregisterScreenOffReceiver();
        model.unregistEvenBus();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            model.exitBy2Click();
        return false;
    }

    @Override
    public HomePageModel createModel() {
        return new HomePageModel(HomePageActivity.this);
    }
}