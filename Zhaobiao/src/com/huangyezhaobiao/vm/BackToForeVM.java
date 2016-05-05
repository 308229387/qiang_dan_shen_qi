package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.model.BackToForeModel;

/**
 * Created by 58 on 2016/2/24.
 */
public class BackToForeVM extends SourceViewModel{
    public BackToForeVM(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new BackToForeModel(this,context);
    }

    public void report(){
        /*t.getDatas();*/
    }
}
