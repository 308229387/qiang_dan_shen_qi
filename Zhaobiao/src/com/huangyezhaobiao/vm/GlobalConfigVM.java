package com.huangyezhaobiao.vm;

import android.content.Context;
import android.util.Log;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.model.GlobalConfigModel;
import com.huangyezhaobiao.utils.PhoneUtils;

import java.util.HashMap;


/**
 * com.huangyezhaobiao.vm
 * Created by shenzhixin on 2016/5/3 16:01.
 */
public class GlobalConfigVM extends SourceViewModel{
    public GlobalConfigVM(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new GlobalConfigModel(this,context);
    }

    /**
     * 刷新用户的信息
     */
    public void refreshUsers(){
        HashMap<String,String> params = new HashMap<>();
        params.put("deviceId", PhoneUtils.getIMEI(context));
        t.configParams(params);
        t.getDatas();
    }


    @Override
    public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
        Log.e("shenzhixin","data:"+bean.getData());
        callBack.onLoadingSuccess(JsonUtils.jsonToObject(bean.getData(), GlobalConfigBean.class));
    }
}
