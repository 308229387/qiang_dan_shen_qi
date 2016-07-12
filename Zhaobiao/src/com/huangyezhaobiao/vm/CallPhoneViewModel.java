package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.LoginBean;
import com.huangyezhaobiao.model.CallPhoneModel;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.view.LoadingProgress;
import com.huangyezhaobiao.view.WaitingTransfer;

/**
 * Created by 58 on 2016/6/22.
 */
public class CallPhoneViewModel extends SourceViewModel{

    private WaitingTransfer dialog;

    public CallPhoneViewModel(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new CallPhoneModel(this,context);
    }

    public void call(String orderId, String mobile ){
        String url = URLConstans.CALL_API_URL+ UrlSuffix.getCallSuffix(context,orderId,mobile);
//        String url = "http://10.252.20.157/call/doCall?"+ UrlSuffix.getCallSuffix(context, orderId,mobile);
        t.setRequestURL(url);
        t.getDatas();
    }

    @Override
    public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
        int status = bean.getStatus();
        if(status==0){
            JSONObject object = JSON.parseObject(bean.getData());
            if (null != object) {
                int callStatus = object.getInteger("status");
                callBack.onLoadingSuccess(callStatus);
            }
            callBack.onLoadingSuccess(bean);
        }else{
            String msg = bean.getMsg();
            if(!TextUtils.isEmpty(msg)){
                callBack.onLoadingError(bean.getMsg());
            }else{
                callBack.onLoadingError("连接失败!");
            }
        }
    }

    @Override
    public void onLoadingStart() {

    }

    @Override
    public void onLoadingCancell() {

    }

    @Override
    public void onLoadingFailure(String err) {

    }


}
