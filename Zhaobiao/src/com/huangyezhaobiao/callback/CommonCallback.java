package com.huangyezhaobiao.callback;

import com.huangye.commonlib.utils.PhoneUtils;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.utils.UserUtils;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.wuba.loginsdk.external.LoginClient;

/**
 * 该类主要用于在所有请求之前添加公共的请求头或请求参数，例如登录授权的 token,使用的设备信息等
 */
public abstract class CommonCallback<T> extends AbsCallback<T> {
    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //如果账户已经登录，就添加 token 等等
        request.headers("ppu", LoginClient.doGetPPUOperate(BiddingApplication.getAppInstanceContext()))//
                .headers("userId", UserUtils.getUserId(BiddingApplication.getAppInstanceContext()))//
//                .headers("userId","34675169722113")//bigbang1
//                .headers("userId","35606250707713")//bigbang6
                .headers("version", "6")//
                .headers("platform", "1")//
                .headers("UUID", PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()))
                .headers("isSon", UserUtils.getIsSon(BiddingApplication.getAppInstanceContext()))
                .headers("suserId",LoginClient.doGetUserIDOperate(BiddingApplication.getAppInstanceContext()));
    }
}
