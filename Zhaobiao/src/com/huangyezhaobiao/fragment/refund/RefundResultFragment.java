package com.huangyezhaobiao.fragment.refund;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.RefundActivity;
import com.huangyezhaobiao.bean.RefundResultBean;
import com.huangyezhaobiao.view.UploadPicDialog;
import com.huangyezhaobiao.vm.RefundResultVM;

/**
 * Created by shenzhixin on 2015/12/14.
 * 退单结果页面
 */
public class RefundResultFragment extends RefundBaseFragment implements NetWorkVMCallBack {
    private TextView tv_refund_result_content;
    private TextView tv_order_id_content;
    private TextView tv_refund_reason_content;
    private TextView tv_refund_desc_content;
    private TextView tv_refund_evidence_content;
    private View     rl_add_evidence;
    private TextView tv_refund_addevidence_content;
    private View     layout_no_internet;
    private RefundResultVM refundResultVM;
    private RefundResultBean refundResultBean;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refundResultVM = new RefundResultVM(this,getActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refund_result;
    }

    @Override
    protected void inflateRootView() {
        tv_refund_result_content   = (TextView) rootView.findViewById(R.id.tv_refund_result_content);
        tv_order_id_content        = (TextView) rootView.findViewById(R.id.tv_order_id_content);
        tv_refund_reason_content   = (TextView) rootView.findViewById(R.id.tv_refund_reason_content);
        tv_refund_desc_content     = (TextView) rootView.findViewById(R.id.tv_refund_desc_content);
        tv_refund_evidence_content = (TextView) rootView.findViewById(R.id.tv_refund_evidence_content);
        layout_no_internet         = rootView.findViewById(R.id.layout_no_internet);
        rl_add_evidence            = rootView.findViewById(R.id.rl_add_evidence);
        tv_refund_addevidence_content = (TextView) rootView.findViewById(R.id.tv_refund_addevidence_content);
    }

    @Override
    protected void getDatas() {
        refundResultVM.setOrderId(orderId);
        refundResultVM.fetchReundResult();
    }

    @Override
    public void onLoadingStart() {
        startLoading();
    }

    @Override
    public void onLoadingSuccess(Object t) {
        stopLoading();
        layout_no_internet.setVisibility(View.GONE);
        if(t instanceof RefundResultBean){
            refundResultBean = (RefundResultBean) t;
        }
        fillDatas();

    }

    @Override
    public void fillDatas() {
        tv_order_id_content.setText(refundResultBean.getOrderId());
        tv_refund_desc_content.setText(refundResultBean.getDetailDesc());
        tv_refund_evidence_content.setText(refundResultBean.getEvidence());
        tv_refund_reason_content.setText(refundResultBean.getCancelReason());
        tv_refund_result_content.setText(refundResultBean.getCancelResult());
        if(!TextUtils.isEmpty(refundResultBean.getAdditionevidence())){
            rl_add_evidence.setVisibility(View.VISIBLE);
            tv_refund_addevidence_content.setText(refundResultBean.getAdditionevidence());
        }
    }

    @Override
    public void onLoadingError(String msg) {
        stopLoading();
        layout_no_internet.setVisibility(View.GONE);
        Toast.makeText(getActivity(),msg,0).show();
    }

    @Override
    public void onLoadingCancel() {
        stopLoading();
    }

    @Override
    public void onNoInterNetError() {
        stopLoading();
        layout_no_internet.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(),"没有网",0).show();
    }

    @Override
    public void onLoginInvalidate() {
        //TODO:回调到activity
        stopLoading();
        ((RefundActivity)getActivity()).onLoginInvalidate();
    }
}
