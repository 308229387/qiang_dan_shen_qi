package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;

/**
 * Created by shenzhixin on 2015/11/11.
 * 获取初始手机号的model层
 */
public class MobileChangeGetModel extends NetWorkModel{
    public MobileChangeGetModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    protected ZhaoBiaoRequest<String> createHttpRequest() {
        return new ZhaoBiaoRequest(ZhaoBiaoRequest.METHOD_GET,"",this);
    }
}
