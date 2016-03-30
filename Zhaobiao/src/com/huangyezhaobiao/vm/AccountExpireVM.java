package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.AccountExpireBean;
import com.huangyezhaobiao.model.AccountExpireModel;

/**
 * Created by shenzhixin on 2015/12/17.
 * 网灵通是否过期的提示
 */
public class AccountExpireVM extends SourceViewModel{
    public AccountExpireVM(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new AccountExpireModel(this,context);
    }

    @Override
    public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
      //  Log.e("ahahahahah","msg:"+bean.getData());
        AccountExpireBean accountExpireBean = JsonUtils.jsonToObject(bean.getData(),AccountExpireBean.class);
      //  Log.e("ahahahahah","msg:"+accountExpireBean.getMsg()+",haha:"+accountExpireBean.getExpireState());
        callBack.onLoadingSuccess(accountExpireBean);
    }

    @Override
    public void onLoadingStart() {

    }

    public void validateAccount(){
        t.getDatas();
    }
}
