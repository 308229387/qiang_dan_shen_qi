package com.huangyezhaobiao.callback;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 */
public abstract class JsonCallback<T> extends EncryptCallback<T> {

    private Class<T> clazz;
    private Type type;

    public JsonCallback() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    //该方法是子线程处理，不能做ui相关的工作
    @Override
    public T parseNetworkResponse(Response response) throws Exception {
        String responseData = response.body().string();
        if (TextUtils.isEmpty(responseData)) return null;
        Log.d("get==",responseData);
        JSONObject jsonObject = new JSONObject(responseData);
        final String msg = jsonObject.optString("msg", "");
        final int code = jsonObject.optInt("status", 0);
        String data = jsonObject.optString("result", "");
        switch (code) {
            case 0:
                if (clazz == String.class) return (T) data;
                if (clazz != null) return new Gson().fromJson(data, clazz);
                if (type != null) return new Gson().fromJson(data, type);
                break;
            case 100:
                throw new IllegalStateException(msg);
            case 200:
                throw new IllegalStateException(msg);
            case 300:
                throw new IllegalStateException(msg);
            case 400:
                throw new IllegalStateException(msg);
            case 500:
                throw new IllegalStateException(msg);
            case 600:
                throw new IllegalStateException(msg);
            default:
                throw new IllegalStateException("未知错误");
        }
        //如果要更新UI，需要使用handler，可以如下方式实现，也可以自己写handler
        OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OkHttpUtils.getContext(), "错误代码：" + code + "，错误信息：" + msg, Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}
