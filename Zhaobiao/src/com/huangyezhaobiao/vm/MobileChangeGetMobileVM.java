package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.MobileChangeBean;
import com.huangyezhaobiao.model.MobileChangeModel;

/**
 * Created by shenzhixin on 2015/11/11.
 * 获取初始绑定的手机号
 */
public class MobileChangeGetMobileVM extends SourceViewModel{
    public MobileChangeGetMobileVM(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new MobileChangeModel(this,context);
    }

    @Override
    public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
        int status = bean.getStatus();
        if(status==0){
            callBack.onLoadingSuccess(JsonUtils.jsonToObject(bean.getData(), MobileChangeBean.class));
        }else{
            String msg = bean.getMsg();
            if(!TextUtils.isEmpty(msg)){
                callBack.onLoadingError(bean.getMsg());
            }else{
                callBack.onLoadingError("连接失败!");
            }
        }
    }

    public void getOriMobile(){
        t.getDatas();
    }
}
