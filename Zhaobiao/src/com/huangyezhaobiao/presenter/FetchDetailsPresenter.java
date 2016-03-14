package com.huangyezhaobiao.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huangyezhaobiao.activity.RefundActivity;

/**
 * Created by shenzhixin on 2015/12/8.
 */
public class FetchDetailsPresenter {
    private Context context;
    public FetchDetailsPresenter(Context context){
        this.context = context;
    }

    //去退单时间未开放界面
    public void goToRefundActivity(String refundType,String orderId){
        Log.e("shenzhixinUUU","refundType:"+refundType);
        Intent intent = RefundActivity.onNewIntent(context,refundType,orderId);
        context.startActivity(intent);
    }


}
