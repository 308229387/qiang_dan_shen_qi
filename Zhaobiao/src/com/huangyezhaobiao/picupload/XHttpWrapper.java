package com.huangyezhaobiao.picupload;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class XHttpWrapper<T> {

    public void post(String url, RequestParams params, RequestCallBack<T> callBack) {
        send(url, HttpRequest.HttpMethod.POST, params, callBack);
    }

    public void get(String url, RequestParams params, RequestCallBack<T> callBack) {
        send(url, HttpRequest.HttpMethod.GET, params, callBack);
    }

    private void send(String url, HttpRequest.HttpMethod method, RequestParams params, RequestCallBack<T> callBack) {
        if (params == null) {
            params = new RequestParams();
        }
        HttpUtils http = new HttpUtils();
        http.configTimeout(30000);
        http.configCurrentHttpCacheExpiry(0);
        http.send(method, url, params, callBack);
        Log.e("shenzhixin", "url:" + url + ",params:" + params.toString());
    }
}
