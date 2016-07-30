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

/**
 * Created by SongYongmeng on 2016/7/26.
 */
public class ExampleUtils {
    public Activity context;

    //带退出提醒的请求。
    private void get() {
        OkHttpUtils.get("http://zhaobiao.58.com/api/getBids")//
                .params("pushId", "-1")//
                .params("bidId", "-1")//
                .params("bidState", "-1")//
                .execute(new LogoutRequest(context, result.class));
    }

    /**
     * 此响应适用首页，Fragment等有需要提示被T下线的请求
     */
    private class LogoutRequest<T> extends DialogCallback<T> {

        public LogoutRequest(Activity activity, Class<T> clazz) {
            super(activity, clazz);
        }

        @Override
        public void onResponse(boolean isFromCache, T t, Request request, @Nullable Response response) {

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
