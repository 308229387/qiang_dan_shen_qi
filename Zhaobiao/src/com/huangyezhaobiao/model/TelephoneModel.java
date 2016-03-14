package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;

/**
 * Created by 58 on 2016/2/24.
 */
public class TelephoneModel extends NetWorkModel{
    public TelephoneModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    protected HttpRequest<String> createHttpRequest() {
        return new HttpRequest<>(HttpRequest.METHOD_GET,"",this);
    }


}
