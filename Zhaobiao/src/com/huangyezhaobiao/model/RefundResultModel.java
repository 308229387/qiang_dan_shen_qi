package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;

/**
 * Created by shenzhixin on 2015/12/14.
 */
public class RefundResultModel extends NetWorkModel{
    public RefundResultModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    protected ZhaoBiaoRequest<String> createHttpRequest() {
        return new ZhaoBiaoRequest<>(ZhaoBiaoRequest.METHOD_GET,"",this);
    }


    public void setUrl(String orderId){
        String url = URLConstans.URL_REFUND_NOT_OPEN + UrlSuffix.getRefundCloseTimeSuffix(context,orderId) ;
        setRequestURL(url);
    }
}
