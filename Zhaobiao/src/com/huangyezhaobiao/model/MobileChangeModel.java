package com.huangyezhaobiao.model;

import android.content.Context;
import android.util.Log;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;

/**
 * Created by shenzhixin on 2015/11/11.
 * 获取初始手机号的接口
 */
public class MobileChangeModel extends NetWorkModel{
    public MobileChangeModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    protected ZhaoBiaoRequest<String> createHttpRequest() {
        //http://serverdomain/api/getUserMobile?token=&userId=
        String url = URLConstans.MOBILE_CHANGE_GET_CODE_URL + UrlSuffix.getMobileChangeOriMobileSuffix(context);
        Log.e("shenzhixinUrl","url:"+url);
        return new ZhaoBiaoRequest(ZhaoBiaoRequest.METHOD_GET,url,this);
    }

}
