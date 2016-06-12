package com.huangyezhaobiao.request;

import android.util.Log;

import com.huangye.commonlib.delegate.HttpRequestCallBack;
import com.huangye.commonlib.network.HTTPTools;
import com.huangye.commonlib.network.HttpRequest;
import com.huangye.commonlib.utils.PhoneUtils;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.utils.UserUtils;
import com.lidroid.xutils.http.RequestParams;

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
       super(method,url,callBack);
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

        params.addHeader("ppu", UserUtils.getUserPPU(BiddingApplication.getAppInstanceContext()));
        params.addHeader("userId",UserUtils.getPassportUserId(BiddingApplication.getAppInstanceContext()));
//        try {
//            params.addHeader("version", VersionUtils.getVersionCode(BiddingApplication.getAppInstanceContext()));
            params.addHeader("version", "6");
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        params.addHeader("platform","1");// android=1;
        params.addHeader("UUID", PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()));
        httpTools = HTTPTools.newHttpUtilsInstance();
        setRequestTimeOut(timeout);
    }
}
