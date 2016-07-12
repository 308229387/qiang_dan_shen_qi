package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;

/**
 * Created by 58 on 2016/6/22.
 */
public class CallPhoneModel extends NetWorkModel {
    public CallPhoneModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }


    @Override
    protected HttpRequest<String> createHttpRequest() {
//        String url = URLConstans.CALL_API_URL+ UrlSuffix.getCallSuffix(context,orderId);
//        String url = "http://10.252.20.157/call/doCall?"+ UrlSuffix.getCallSuffix(context,orderId);
        return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_GET, "",this);
    }
}
