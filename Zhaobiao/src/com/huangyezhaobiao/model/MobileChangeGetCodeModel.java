package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;

/**
 * Created by shenzhixin on 2015/11/11.
 * 手机绑定修改页面修改手机号的model层--获取验证码接口
 */
public class MobileChangeGetCodeModel extends NetWorkModel{
    public MobileChangeGetCodeModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    protected ZhaoBiaoRequest<String> createHttpRequest() {
        return new ZhaoBiaoRequest(ZhaoBiaoRequest.METHOD_GET,"",this);
    }
}
