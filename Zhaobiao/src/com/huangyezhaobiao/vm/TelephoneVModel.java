package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.model.TelephoneModel;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;

/**
 * Created by 58 on 2016/2/24.
 */
public class TelephoneVModel extends SourceViewModel{
    public TelephoneVModel(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new TelephoneModel(this,context);
    }


    public void telephone(String orderId,String source){
        //2016.5.3 add
        String url = URLConstans.URL_APP_TELEPHONE + UrlSuffix.getTelephoneSuffix(orderId);
        //2016.5.3 add end
        t.setRequestURL(url);
        t.getDatas();
    }
}
