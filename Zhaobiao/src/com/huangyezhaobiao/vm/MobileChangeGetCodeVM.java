package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.model.MobileChangeGetCodeModel;

/**
 * Created by shenzhixin on 2015/11/11.
 * 手机修改绑定页面获取验证码的接口
 */
public class MobileChangeGetCodeVM extends SourceViewModel{
    public MobileChangeGetCodeVM(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new MobileChangeGetCodeModel(this,context);
    }

    /**
     * 对外暴露的获取验证码的接口
     */
    public void getValidateCode(){
        t.getDatas();
    }
}
