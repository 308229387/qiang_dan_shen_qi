package com.huangyezhaobiao.request;

import com.huangye.commonlib.delegate.HttpRequestCallBack;
import com.huangye.commonlib.network.HTTPTools;
import com.huangye.commonlib.network.HttpRequest;
import com.huangye.commonlib.utils.PhoneUtils;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.utils.UserUtils;

/**
 * author keyes
 * time 2016/5/3 20:15
 * email：1175426782@qq.com
 * param：
 * descript：
 */
public class ZhaoBiaoRequest<T> extends HttpRequest {

    public ZhaoBiaoRequest(int method,String url,HttpRequestCallBack callBack){
       super(method,url,callBack);
    }

    @Override
    protected void initEnv() {
        params.addHeader("ppu", UserUtils.getUserPPU(BiddingApplication.getAppInstanceContext()));
        params.addHeader("userId",UserUtils.getPassportUserId(BiddingApplication.getAppInstanceContext()));
//        try {
//            params.addHeader("version", VersionUtils.getVersionCode(BiddingApplication.getAppInstanceContext()));
            params.addHeader("version", "5");
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        params.addHeader("platform","1");
        params.addHeader("UUID", PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()));
        httpTools = HTTPTools.newHttpUtilsInstance();
        setRequestTimeOut(timeout);
    }
}
