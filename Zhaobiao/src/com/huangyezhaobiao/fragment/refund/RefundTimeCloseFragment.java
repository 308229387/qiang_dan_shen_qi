package com.huangyezhaobiao.fragment.refund;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.RefundActivity;
import com.huangyezhaobiao.bean.tt.RefundCloseTimeBean;
import com.huangyezhaobiao.mediator.RefundMediator;
import com.huangyezhaobiao.vm.RefundCloseVM;

/**
 * Created by shenzhixin on 2015/12/9.
 * 退单时间未开放的fragment
 * 每一次请求都对应一个model---vm----实体bean---callback
 */
public class RefundTimeCloseFragment extends RefundBaseFragment implements NetWorkVMCallBack {
    private TextView tv_refund_startTime;
    private TextView tv_refund_endTime;
    private View layout_no_internet;
    private RefundCloseVM refundCloseVM;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refundCloseVM = new RefundCloseVM(this,getActivity());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refund;
    }

    @Override
    protected void inflateRootView() {
        tv_refund_startTime = (TextView) rootView.findViewById(R.id.tv_refund_startTime);
        tv_refund_endTime   = (TextView) rootView.findViewById(R.id.tv_refund_endTime);
        layout_no_internet  = rootView.findViewById(R.id.layout_no_internet);
    }


    @Override
    protected void getDatas() {
        refundCloseVM.setRefundType(RefundMediator.TYPE_REFUND_TIME_CLOSE);
        refundCloseVM.setOrderId(orderId);
        refundCloseVM.getDatas();
    }

    @Override
    public void onLoadingStart() {
        startLoading();
    }

    @Override
    public void onLoadingSuccess(Object t) {
        stopLoading();
        layout_no_internet.setVisibility(View.GONE);
        if(t instanceof RefundCloseTimeBean){
            RefundCloseTimeBean refundCloseTimeBean = (RefundCloseTimeBean) t;
            tv_refund_startTime.setText(refundCloseTimeBean.getOpenTime());
            tv_refund_endTime.setText(refundCloseTimeBean.getCloseTime());
        }
    }

    @Override
    public void onLoadingError(String msg) {
        stopLoading();
        layout_no_internet.setVisibility(View.GONE);
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingCancel() {
        stopLoading();
    }

    @Override
    public void onNoInterNetError() {
        stopLoading();
        layout_no_internet.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(),"没有网络",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginInvalidate() {
        stopLoading();
        //调用到acivity的loginInvalidate
        ((RefundActivity)getActivity()).onLoginInvalidate();
    }
}
