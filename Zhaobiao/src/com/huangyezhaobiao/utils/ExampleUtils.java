package com.huangyezhaobiao.utils;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.huangyezhaobiao.bean.result;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.callback.JsonCallback;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.config.Urls;

/**
 * Created by SongYongmeng on 2016/7/26.
 * 描    述：此类为示例代码。
 */
public class ExampleUtils {
    public Activity context;

    //示例get请求。
    private void get() {
        OkHttpUtils.get("http://zhaobiao.58.com/api/getBids")//
                .params("pushId", "-1")//
                .params("bidId", "-1")//
                .params("bidState", "-1")//
                .execute(new LogoutRequest(context));
    }

    //示例post请求
    private void post() {
        OkHttpUtils.post(Urls.WLT_CHECK)
                .params("params", "params")
                .execute(new LogoutRequest(context));
    }

    /**
     * 此响应适用首页，Fragment等有需要提示被T下线的请求，响应体中少参数也可以取出值，如在返回的JSON串中包含两个，
     * 但在定义的result序列化类里，可以只有一个参数
     */
    private class LogoutRequest extends DialogCallback<result> {

        public LogoutRequest(Activity context) {
            super(context);
        }

        @Override
        public void onResponse(boolean isFromCache, result result, Request request, @Nullable Response response) {

        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
        }

    }

    /**
     * 基础序列化的实体响应类，泛型中是什么类型就是什么类型，可以是String
     */
    private class ObjectText extends JsonCallback<result> {

        @Override
        public void onResponse(boolean isFromCache, result getBinds, Request request, @Nullable Response response) {
            ToastUtils.showToast(getBinds.getData().get(0).getBidState());
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }

    }


    private class Test extends JsonCallback<String> {

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            ToastUtils.showToast(s);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }

    }


}
