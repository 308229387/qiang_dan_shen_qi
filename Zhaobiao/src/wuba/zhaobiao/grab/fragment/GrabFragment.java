package wuba.zhaobiao.grab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.grab.model.GrabModel;

/**
 * Created by SongYongmeng on 2016/8/8.
 * 描    述：抢单展示，红点检测。
 */
public class GrabFragment extends BaseFragment<GrabModel> implements INotificationListener, INetStateChangedListener {
    public long resume_time;
    public long stop_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initalizationLayout(inflater, container);
        creatAdapter();
        setInfo();
        registEventBus();
        return model.getView();
    }

    private void initalizationLayout(LayoutInflater inflater, ViewGroup container) {
        model.creatView(inflater, container);
        model.initView();
        model.registMessageBar();
    }

    public void registPush() {
        model.registListener();
    }

    private void registEventBus() {
        model.registerEventBus();
    }

    private void creatAdapter() {
        model.creatAdapter();
    }

    private void setInfo() {
        model.setParamsForListView();
        model.setCachRespons();
    }

    @Override
    public void onResume() {
        super.onResume();
        registPush();
        resume_time = System.currentTimeMillis();
        model.setHeaderHeight();
        model.checkNet();
        model.grabClickedStatistics();
        model.getGrabData();
        model.resetTabNumber();
    }

    @Override
    public void onNotificationCome(PushBean pushBean) {
        model.showPush(pushBean);
    }

    public void onEventMainThread(EventAction action) {
        model.jugePush(action);
    }

    public void OnFragmentSelectedChanged(boolean isSelected) {
        if (isSelected && model != null) {
            model.selectChange();
        }
    }

    @Override
    protected GrabModel createModel() {
        return new GrabModel(GrabFragment.this);
    }


    public void unregistNotificationListener() {
        model.unregistNotificationListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.unregistPushAndEventBus();
    }

    @Override
    public void onStop() {
        super.onStop();
        stop_time = System.currentTimeMillis();
        model.statisticsDeadTime();
    }


    @Override
    public void NetConnected() {
        model.closeMessageBar();
    }

    @Override
    public void NetDisConnected() {
        model.diaplayMessageBar();
    }


    public void topSwitchChicked(boolean isChecked) {
        if (model != null)
            model.topSwitchChicked(isChecked);
    }
}
