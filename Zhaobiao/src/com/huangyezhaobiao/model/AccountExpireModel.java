package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;

/**
 * Created by shenzhixin on 2015/12/17.
 */
public class AccountExpireModel extends NetWorkModel{
    public AccountExpireModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    protected HttpRequest<String> createHttpRequest() {
        return new HttpRequest<>(HttpRequest.METHOD_GET, URLConstans.URL_APP_ACCOUNT_EXPIRE+ UrlSuffix.getAccountExpireSuffix(context),this);
    }
}
