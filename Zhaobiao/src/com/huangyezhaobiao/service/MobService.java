package com.huangyezhaobiao.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        if (null != mTimer) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new UploadTask(), MINPostTime, MINPostTime);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, TAG + " onStart " + intent.getStringExtra("data"));
        if("sp".equals(intent.getStringExtra("from"))){
            uploadMob(this,UserUtils.getMobCommon(this),UserUtils.getMobData(this),0,"sp");
        }
        if("sixitems".equals(intent.getStringExtra("from"))){
            uploadMob(this,HYMob.params_map.get("common"),HYMob.params_map.get("data"),0,"");
        }
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
        params.addHeader("version", "6");
        params.addHeader("platform", "1");
        params.addHeader("UUID", PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()));
        return params;
    }

    class UploadTask extends TimerTask {
        @Override
        public void run() {
            Log.v(TAG,"UploadTask is running....");
            if(HYMob.dataList.size() < 5){
                return;
            }
            uploadMob(MobService.this,HYMob.params_map.get("common"),HYMob.params_map.get("data"),0,"");
        }
    }



    // 这里只能定义成static
    private synchronized void uploadMob(final Context context, final String common, final String data, int t, final String from){
        if(NetUtils.isNetworkConnected(context)){
            HTTPTools.newHttpUtilsInstance().doGet(URLConstans.UPLOAD_URL +"?common=" + common + "&data=" + data +"&t=" + t, null, new HttpRequestCallBack() {
                @Override
                public void onLoadingFailure(String err) {
                    UserUtils.setMobItem(context,HYMob.dataList.size());
                    UserUtils.setMobCommon(context,common);
                    UserUtils.setMobData(context,data);
                }

                @Override
                public void onLoadingSuccess(ResponseInfo<String> result) {
                    Log.v("Upload",result.result);
                    try {
                        JSONObject jsonResult = JSON.parseObject(result.result);
                        if (jsonResult.containsKey("status") && "0".equals(jsonResult.getString("status"))) {
                            if("sp".equals(from)){
                                Log.v("Upload","sp上传成功");
                                UserUtils.clearMob(context);
                            } else {
                                Log.v("Upload","上传成功");
                                HYMob.dataList.clear();
                            }
                        }
                    } catch (Exception e) {
                        UserUtils.setMobItem(context,HYMob.dataList.size());
                        UserUtils.setMobCommon(context,common);
                        UserUtils.setMobData(context,data);
                        e.printStackTrace();
                    }
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
        } else {
            UserUtils.setMobItem(context,HYMob.dataList.size());
            UserUtils.setMobCommon(context,common);
            UserUtils.setMobData(context,data);
        }
    }

}
