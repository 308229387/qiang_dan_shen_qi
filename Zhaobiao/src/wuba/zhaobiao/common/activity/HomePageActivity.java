package wuba.zhaobiao.common.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.eventbus.EventAction;

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
    }

    private void initEvenBus() {
        model.registEvenBus();
    }

    private void registService() {
        model.startBindService();
        model.registerScreenOffReceiver();
    }

    public void onEventMainThread(EventAction action) {
        model.eventBusThing(action);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRedDotAndGrabBack();
    }

    private void checkRedDotAndGrabBack() {
        model.refreshTab();
        model.afterBidSuccessBack();
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
    public HomePageModel createModel() {
        return new HomePageModel(HomePageActivity.this);
    }
}
