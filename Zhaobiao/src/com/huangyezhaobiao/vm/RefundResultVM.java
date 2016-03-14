package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.RefundResultBean;
import com.huangyezhaobiao.model.RefundResultModel;

/**
 * Created by shenzhixin on 2015/12/14.
 */
public class RefundResultVM extends SourceViewModel{
    public RefundResultVM(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }


    @Override
    public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
        int status = bean.getStatus();
        if(status==0){
            callBack.onLoadingSuccess(JsonUtils.jsonToObject(bean.getData(), RefundResultBean.class));
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
    protected NetWorkModel initListNetworkModel(Context context) {
        return new RefundResultModel(this,context);
    }

    public void setOrderId(String orderId){
        ((RefundResultModel)t).setUrl(orderId);
    }

    public void fetchReundResult(){
        t.getDatas();
    }
}
