package wuba.zhaobiao.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;
import com.huangyezhaobiao.utils.LogUtils;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.order.model.OrderModel;

/**
 * Created by 58 on 2016/8/15.
 */
public class OrderFragment extends BaseFragment<OrderModel> implements INotificationListener,INetStateChangedListener {

    public long resume_time;
    public long stop_time;

    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {
        if (isSelected && model != null){
            model.selectChange();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initalizationLayout(inflater, container);
        creatAdapter();
        setInfo();
        return model.getView();
    }

    private void initalizationLayout(LayoutInflater inflater, ViewGroup container) {
        model.createView(inflater, container);
        model.initView();
        model.registerMessageBar();
    }

    private void registPushAndEventBus() {
        model.registerListenrer();
    }

    private void creatAdapter() {
        model.createAdapter();
    }

    private void setInfo() {
        model.setParamsForListVew();
        model.setCacheRespons();
    }

    @Override
    public void onResume() {
        super.onResume();
        registPushAndEventBus();
        resume_time = System.currentTimeMillis();
        model.setHeaderHeight();
        model.checkNet();
        model.OrderTabClickedStatistics();
        model.getData();
        model.resetTabNumber();
    }

    @Override
    public void onStop() {
        super.onStop();
        stop_time = System.currentTimeMillis();
        model.statisticsDeadTime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.unregistPushAndEventBus();
    }

    @Override
    protected OrderModel createModel() {
        return new OrderModel(OrderFragment.this);
    }

    @Override
    public void onNotificationCome(PushBean pushBean) {
        model.showPush(pushBean);
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
