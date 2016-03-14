package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;

/**
 * Created by 58 on 2016/2/24.
 */
public class BackToForeModel extends NetWorkModel{
    public BackToForeModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    protected HttpRequest<String> createHttpRequest() {
        String url = URLConstans.URL_BACKGROUND_TO_FOREGROUND + UrlSuffix.getBackToForeSuffix(context);
        return new HttpRequest<>(HttpRequest.METHOD_GET,url,this);
    }
}
