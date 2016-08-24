package wuba.zhaobiao.message.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangye.commonlib.vm.callback.StorageVMCallBack;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.message.model.MessageModel;

/**
 * Created by 58 on 2016/8/16.
 */
public class MessageFragment extends BaseFragment<MessageModel> implements INotificationListener,INetStateChangedListener,StorageVMCallBack {
    public long resume_time;
    public long stop_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initHeaderView(inflater, container);
        registerPushListener();
        createAdapter();
        return model.getView();
    }

    private void initHeaderView(LayoutInflater inflater, ViewGroup container){
        model.createView(inflater, container);
        model.initView();
        model.registerMessageBar();
    }

    private void  registerPushListener(){
        model.registerListener();
    }

    private void createAdapter(){
        model.createAdapter();
    }

    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {
        if (isSelected && model != null)
            model.load();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume_time = System.currentTimeMillis();
        model.MessageClickedStatistics();
        model.setHeaderHeight();
        model.checkNet();
        model.load();
        model.notifyDatasWithoutUnread();
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
    protected MessageModel createModel() {
        return new MessageModel(MessageFragment.this);
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
    @Override
    public void getDataSuccess(Object o) {
        model.getDataSuccess(o);
    }

    @Override
    public void getDataFailure() {
        model.getDataFailure();
    }

    @Override
    public void insertDataSuccess() {

    }

    @Override
    public void insertDataFailure() {

    }

    @Override
    public void deleteDataSuccess() {

    }

    @Override
    public void deleteDataFailure() {

    }


}
