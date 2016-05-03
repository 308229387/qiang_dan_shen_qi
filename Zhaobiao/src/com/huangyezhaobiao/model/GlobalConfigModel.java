package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;
import com.huangyezhaobiao.url.URLConstans;

/**
 * com.huangyezhaobiao.model
 * Created by shenzhixin on 2016/5/3 16:01.
 */
public class GlobalConfigModel extends NetWorkModel{
    public GlobalConfigModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    protected HttpRequest<String> createHttpRequest() {
        String url = URLConstans.URL_GLOBAL_CONFIG;
        return new HttpRequest<>(HttpRequest.METHOD_GET,url,this);
    }
}
