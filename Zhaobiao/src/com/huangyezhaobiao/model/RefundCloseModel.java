package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;

/**
 * Created by shenzhixin on 2015/12/9.
 */
public class RefundCloseModel extends NetWorkModel{
    public RefundCloseModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    protected HttpRequest<String> createHttpRequest() {
        return new HttpRequest<>(HttpRequest.METHOD_GET,"",this);
    }



}
