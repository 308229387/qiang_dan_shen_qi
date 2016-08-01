package com.huangyezhaobiao.callback;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.huangye.commonlib.constans.ResponseConstans;
import com.huangye.commonlib.file.SharedPreferencesUtils;
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
        /**
         * 一般来说，服务器返回的响应码都包含 code，msg，data 三部分，在此根据自己的业务需要完成相应的逻辑判断
         * 以下只是一个示例，具体业务具体实现
         */
        JSONObject jsonObject = new JSONObject(responseData);
        final String msg = jsonObject.optString("msg", "");
        final int code = jsonObject.optInt("status", 0);
        final String data = jsonObject.optString("result", "");
        final String loginFlag = jsonObject.getString("loginFlag");
        final String token = jsonObject.getString("token");
        Boolean isLoginValidate = SharedPreferencesUtils.isLoginValidate(OkHttpUtils.getContext(), ResponseConstans.SP_NAME, token);

        switch (code) {
            case 0:
                if (loginFlag.equals("0") && !isLoginValidate) {
                    throw new IllegalStateException("need_down_line");
                } else {
                    /**
                     * code = 0 代表成功，默认实现了Gson解析成相应的实体Bean返回，可以自己替换成fastjson等
                     * 对于返回参数，先支持 String，然后优先支持class类型的字节码，最后支持type类型的参数
                     */
                    if (loginFlag.equals("1"))
                        SharedPreferencesUtils.saveUserToken(OkHttpUtils.getContext(), ResponseConstans.SP_NAME, token);
                    if (clazz == String.class) return (T) data;
                    if (clazz != null) return new Gson().fromJson(data, clazz);
                    if (type != null) return new Gson().fromJson(data, type);
                }
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
            case 2001:
                throw new IllegalStateException("ppu_expired");
            default:
                throw new IllegalStateException("未知错误");
        }

        return null;
    }


}
