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
import com.huangyezhaobiao.utils.UserUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private final long MINPostTime = 24 * 60 * 60 * 1000;
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
        mTimer.schedule(new UploadTask(), 0, MINPostTime);
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
        return params;
    }

    class UploadTask extends TimerTask {
        @Override
        public void run() {
            Log.v(TAG,"UploadTask is running....");
            String params = "common=&data=&t=0";
            String common = null;
            String data = null;
            try {
                common = URLEncoder.encode("H4sIAAAAAAAAAB3MSwrCQBAE0Lv0UjT0fDLT9gGEnEC38xFsNAkIriR3T3d29aqg/tAKMNQi/QdnaFXhh3FAQ1OUpX9X6canMns8OU/H/FI/pKyzmER1n26T5bfmEJmQa2DKPCYuzoaP3aOL5A7OSkrZx4g+uxATXa1etL7AtgPk8RlDnAAAAA==","UTF-8");
                data = URLEncoder.encode("H4sIAAAAAAAAAIuuVkrOV7JSMjYwUNJRSi4EMg1NzIxNTY1MTM0tDMyAgrn5Kak5wSWJJakgSaBAcSJIg6mZgZmRiaGxsYmRubFSbSwAnEis90oAAAA=","UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String uploadUrl = "common=" + common + "&data=" +data + "&t=0";
            String url = URLConstans.UPLOAD_URL + uploadUrl;
            HTTPTools.newHttpUtilsInstance().doGet(url, getRequestParams(), new HttpRequestCallBack() {
                @Override
                public void onLoadingFailure(String err) {

                }

                @Override
                public void onLoadingSuccess(ResponseInfo<String> result) {
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
