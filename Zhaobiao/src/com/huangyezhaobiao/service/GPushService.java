package com.huangyezhaobiao.service;

        import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.huangye.commonlib.delegate.HttpRequestCallBack;
import com.huangye.commonlib.network.HTTPTools;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.deal.IDealWithBean;
import com.huangyezhaobiao.gtui.AppStateFactory;
import com.huangyezhaobiao.notification.NotificationExecutor;
import com.huangyezhaobiao.push.BiddingMessageReceiver;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.PushUtils;
import com.lidroid.xutils.http.ResponseInfo;

/**
 * Created by 58 on 2016/1/7.
 * 当个推的receiver收到消息后，开启这个广播，把json串带过来，在这边处理，
 * 因为receiver就10秒钟时间就挂掉了
 */
public class GPushService extends IntentService {
    public static final String KEY_GPUSH_SERVICE = "json_key";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GPushService(String name) {
        super("GePush");
    }

    public GPushService(){
        super("GePush");
    }

    public static Intent onNewIntent(Context context,String json){
        Intent intent = new Intent(context,GPushService.class);
        intent.putExtra(KEY_GPUSH_SERVICE, json);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String jsonResult = intent.getStringExtra(KEY_GPUSH_SERVICE);
        //把jsonResult转化成javaBean
        //得到app的实例
        BiddingApplication app = (BiddingApplication) getApplicationContext();
        //得到通知栏的实例
        final NotificationExecutor notificationExecutor = app.getGeTuiNotification();
        //得到转化过来的实体bean--新订单/结果/倒计时等
        final PushBean pushBean = PushUtils.dealGePushMessage(this, jsonResult);

        BiddingMessageReceiver.PushHandler handler = BiddingApplication.getHandler();
        //根据不同的状态做不同的东西
        IDealWithBean dealWithBean = AppStateFactory.getDealFromState(this);
        //处理
        dealWithBean.dealPushBean(this,pushBean,notificationExecutor,app.getCurrentNotificationListener(),jsonResult);
        //  VoiceManager.getVoiceManagerInstance(this).speakLocalMessage("这是一条重要的消息");
        reportToServerForGeTuiArrived(pushBean);
    }

    /**
     * 回调到这个方法时表示收到了推送，传递给服务端一条消息
     * userId= & bidId= & bidType = & platform= &UUID= &version= &token=
     * @param pushBean
     */
    private void reportToServerForGeTuiArrived(PushBean pushBean) {
        if(pushBean==null)
            return;
        String bidId   = pushBean.toPushPassBean().getBidId()+"";
        String bidType = pushBean.getTag()+"";
        //2016.5.3 add
        String url     = URLConstans.URL_RECEIVE_GE_PUSH + UrlSuffix.getGePushSuffix(bidId,bidType);
        //2016.5.3 add end
        try {
            HTTPTools.newHttpUtilsInstance().doGet(url, new HttpRequestCallBack() {
                @Override
                public void onLoadingFailure(String err) {

                }

                @Override
                public void onLoadingSuccess(ResponseInfo<String> result) {

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
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
