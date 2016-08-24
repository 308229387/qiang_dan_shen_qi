package wuba.zhaobiao.grab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyezhaobiao.activity.PushInActivity;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.grab.model.GrabModel;

/**
 * Created by SongYongmeng on 2016/8/8.
 * 描    述：抢单展示，红点检测。
 */
public class GrabFragment extends BaseFragment<GrabModel> implements INotificationListener,INetStateChangedListener {
    public long resume_time;
    public long stop_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initalizationLayout(inflater, container);
        registPushAndEventBus();
        creatAdapter();
        setInfo();
        return model.getView();
    }

    private void initalizationLayout(LayoutInflater inflater, ViewGroup container) {
        model.creatView(inflater, container);
        model.initView();
        model.registMessageBar();
    }

    private void registPushAndEventBus() {
        model.registListener();
    }

    private void creatAdapter() {
        model.creatAdapter();
    }

    private void setInfo() {
        model.setInfoForTop();
        model.setParamsForListView();
        model.setCachRespons();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume_time = System.currentTimeMillis();
        model.setHeaderHeight();
        model.checkNet();
        model.getData();
        model.selectChange();
        model.grabClickedStatistics();
    }

    @Override
    public void onNotificationCome(PushBean pushBean) {
        model.showPush(pushBean);
    }

    public void onEventMainThread(PushInActivity.GrabSuccessMessage event) {
        model.jugePush(event);
    }


    public void OnFragmentSelectedChanged(boolean isSelected) {
        if (isSelected && model != null){
            model.selectChange();
        }

    }

    @Override
    protected GrabModel createModel() {
        return new GrabModel(GrabFragment.this);
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
}
