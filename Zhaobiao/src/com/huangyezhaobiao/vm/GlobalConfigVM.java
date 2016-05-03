package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.model.GlobalConfigModel;


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
        t.getDatas();
    }
}
