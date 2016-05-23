package com.huangyezhaobiao.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.huangye.commonlib.delegate.HttpRequestCallBack;
import com.huangye.commonlib.network.HTTPTools;
import com.huangye.commonlib.utils.PhoneUtils;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * author keyes
 * time 2016/5/17 13:32
 * email：1175426782@qq.com
 * param：
 * descript：
 */
public class MobService extends Service {
    private static final String TAG = MobService.class.getName();
    private final long MINPostTime = 5 * 60 * 1000;
    private Timer mTimer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG,">=========== " + TAG + " is created ===========>");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, TAG + " onStart " + intent.getStringExtra("data"));
        if (null != mTimer) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new UploadTask(), MINPostTime, MINPostTime);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mTimer) {
            mTimer.cancel();
        }
    }

    private RequestParams getRequestParams(){
        RequestParams params = new RequestParams();
        List<RequestParams.HeaderItem> list = params.getHeaders();
        if(list!= null && list.size() != 0){
            list.clear();
        }
        params.addHeader("ppu", UserUtils.getUserPPU(BiddingApplication.getAppInstanceContext()));
        params.addHeader("userId",UserUtils.getPassportUserId(BiddingApplication.getAppInstanceContext()));
        Log.e("sdf", "ppu:" + UserUtils.getUserPPU(BiddingApplication.getAppInstanceContext()));
        Log.e("sdf", "userId:" + UserUtils.getPassportUserId(BiddingApplication.getAppInstanceContext()));
//        try {
//            params.addHeader("version", VersionUtils.getVersionCode(BiddingApplication.getAppInstanceContext()));
        params.addHeader("version", "6");
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        params.addHeader("platform", "1");
        params.addHeader("UUID", PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()));
//        if(HYMob.params_map != null && !HYMob.params_map.isEmpty()){
//            for(Iterator it = HYMob.params_map.entrySet().iterator(); it.hasNext();){
//                Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
//                params.addBodyParameter(e.getKey().toString(), e.getValue());
//            }
//        }
        return params;
    }

    class UploadTask extends TimerTask {
        @Override
        public void run() {
            Log.v(TAG,"UploadTask is running....");
            if(HYMob.dataList.size() < 10){
                return;
            }
            if(NetUtils.isNetworkConnected(MobService.this)){
                HTTPTools.newHttpUtilsInstance().doGet(URLConstans.UPLOAD_URL +"?common=" + HYMob.params_map.get("common") + "&data=" + HYMob.params_map.get("data") +"&t=0", getRequestParams(), new HttpRequestCallBack() {
                    @Override
                    public void onLoadingFailure(String err) {

                    }

                    @Override
                    public void onLoadingSuccess(ResponseInfo<String> result) {
                        HYMob.dataList.clear();
                        Log.v(TAG,result.result.toString());
                    }

                    @Override
                    public void onLoadingStart() {

                    }

                    @Override
                    public void onLoadingCancelled() {

                    }

                    @Override
                    public void onLoading(long total, long current) {

                    }
                });
            }

        }
    }

}
