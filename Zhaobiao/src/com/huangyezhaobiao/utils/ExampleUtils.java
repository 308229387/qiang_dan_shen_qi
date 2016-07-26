package com.huangyezhaobiao.utils;

import android.support.annotation.Nullable;

import com.huangyezhaobiao.bean.result;
import com.huangyezhaobiao.callback.JsonCallback;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SongYongmeng on 2016/7/26.
 */
public class ExampleUtils {

    //请求实体
    private void get() {
        OkHttpUtils.get("http://zhaobiao.58.com/api/getBids")//
                .params("pushId", "-1")//
                .params("bidId", "-1")//
                .params("bidState", "-1")//
                .execute(new ObjectText());
    }


    //序列化的实体响应类
    private class ObjectText extends JsonCallback<result> {//泛型中是什么类型就是什么类型，可以是String

        @Override
        public void onResponse(boolean isFromCache, result getBinds, Request request, @Nullable Response response) {
            ToastUtils.showToast(getBinds.getData().get(0).getBidState());
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }

    }


    //响应类
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
