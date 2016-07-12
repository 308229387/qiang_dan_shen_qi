package com.huangyezhaobiao.request;

import android.util.Log;

import com.huangye.commonlib.delegate.HttpRequestCallBack;
import com.huangye.commonlib.network.HTTPTools;
import com.huangye.commonlib.network.HttpRequest;
import com.huangye.commonlib.utils.PhoneUtils;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.lidroid.xutils.http.RequestParams;
import com.wuba.loginsdk.external.LoginClient;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * author keyes
 * time 2016/5/3 20:15
 * email：1175426782@qq.com
 * param：
 * descript：
 */
public class ZhaoBiaoRequest<T> extends HttpRequest {

    public ZhaoBiaoRequest(int method,String url,HttpRequestCallBack callBack){
       super(method, url, callBack);
    }

    @Override
    protected void initEnv() {
        List<RequestParams.HeaderItem> list = params.getHeaders();
        if(list!= null && list.size() != 0){
            list.clear();
        }

        List<NameValuePair> bodyParams = params.getQueryStringParams();
        if(bodyParams!= null && bodyParams.size() != 0){
            bodyParams.clear();
        }

//        params.addHeader("ppu", UserUtils.getUserPPU(BiddingApplication.getAppInstanceContext()));
//        params.addHeader("userId",UserUtils.getPassportUserId(BiddingApplication.getAppInstanceContext()));

        params.addHeader("ppu", LoginClient.doGetPPUOperate(BiddingApplication.getAppInstanceContext()));
        params.addHeader("userId",LoginClient.doGetUserIDOperate(BiddingApplication.getAppInstanceContext()));
//        params.addHeader("userId","34675169722113");  //bigbang1
//        params.addHeader("userId","34680567140865");  //bigbang2
//        params.addHeader("userId","34680592616449");  //bigbang3
//        params.addHeader("userId","34964986925569");  //bigbang4
//        params.addHeader("userId","35606241334273");  //bigbang5
//        params.addHeader("userId","35606250707713");  //bigbang6
//        params.addHeader("userId","35606332708865");  //bigbang7
        params.addHeader("version", "6");
        params.addHeader("platform","1");// android=1;
        params.addHeader("UUID", PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()));
        httpTools = HTTPTools.newHttpUtilsInstance();
        setRequestTimeOut(timeout);
    }
}
