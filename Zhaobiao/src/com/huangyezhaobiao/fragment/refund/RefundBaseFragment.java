package com.huangyezhaobiao.fragment.refund;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.inter.ICamera;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.view.LoadingProgress;

import de.greenrobot.event.EventBus;

/**
 * Created by shenzhixin on 2015/12/9.
 * 退单的基类fragment
 */
public abstract class RefundBaseFragment extends Fragment implements ICamera,NetWorkVMCallBack{
    protected View rootView;//根布局view
    private LoadingProgress dialog;
    protected String orderId;//订单编号
    /**
     * 返回fragment需要解析的布局文件id
     * @return
     */
    protected abstract int getLayoutId();


    @Override
    public void fillDatas() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//object
    }

    public void onEventMainThread(EventAction action) {}
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//object
    }

    public void setOrderId(String orderId){
        this.orderId = orderId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(),null);
        inflateRootView();
        return rootView;
    }


    /**
     * 对每个fragment的根布局进行解析
     */
    protected abstract void inflateRootView();


    /**
     * 加载效果
     */
    public void startLoading() {
        if (dialog == null) {
            dialog = new LoadingProgress(getActivity(),
                    R.style.loading);
        }
        if(dialog!=null && !getActivity().isFinishing()){
            dialog.show();
            LogUtils.LogE("ashenDialog", "show");
        }
    }

    /**
     * 对话框消失
     */
    public void stopLoading() {
        if (getActivity()!=null && !getActivity().isFinishing() && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDatas();
    }


    protected abstract void getDatas();

    @Override
    public void onVersionBack(String version) {
        Log.e("shenyy","version:"+version);
    }
}

