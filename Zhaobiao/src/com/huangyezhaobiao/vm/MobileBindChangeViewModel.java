package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.model.MobileChangeModel;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;

/**
 * Created by shenzhixin on 2015/11/11.
 * 提交数据时的接口
 */
public class MobileBindChangeViewModel extends SourceViewModel{
    Context context;
    public MobileBindChangeViewModel(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
        this.context = context;
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new MobileChangeModel(this,context);
    }

    /**
     * 提交
     * @param newPhoneNumber
     * @param validateCode
     */
    public void submit(String mobile,String newPhoneNumber,String validateCode){
        //2016.5.4 add
        String url = URLConstans.MOBILE_CHANGE_SUBMIT_URK + UrlSuffix.getMobileChangeSubmitSuffix(mobile,newPhoneNumber,validateCode);
        //2016.5.4 add end
        t.setRequestURL(url);
        t.getDatas();
    }

}
