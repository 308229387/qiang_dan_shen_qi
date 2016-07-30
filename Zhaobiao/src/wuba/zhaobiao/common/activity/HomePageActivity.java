package wuba.zhaobiao.common.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.result;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.eventbus.EventAction;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
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
        get();

    }

    private void init() {
        initLayout();
        initEvenBus();
        registService();
    }

    private void initLayout() {
        model.setTobBarColor();
        model.initView();
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

    private void get() {
        OkHttpUtils.get("http://zhaobiao.58.com/api/getBids")//
                .params("pushId", "-1")//
                .params("bidId", "-1")//
                .params("bidState", "-1")//
                .execute(new ObjectText(HomePageActivity.this, result.class));
    }

    //序列化的实体响应类
    private class ObjectText<T> extends DialogCallback<T> {

        public ObjectText(Activity activity, Class<T> clazz) {
            super(activity, clazz);
        }

        @Override
        public void onResponse(boolean isFromCache, T t, Request request, @Nullable Response response) {

        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
        }

    }

    @Override
    public HomePageModel createModel() {
        return new HomePageModel(HomePageActivity.this);
    }
}
