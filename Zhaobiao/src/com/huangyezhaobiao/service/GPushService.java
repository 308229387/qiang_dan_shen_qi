package com.huangyezhaobiao.service;

        import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
        import android.util.Log;

        import com.huangye.commonlib.delegate.HttpRequestCallBack;
import com.huangye.commonlib.network.HTTPTools;
        import com.huangye.commonlib.utils.PhoneUtils;
        import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.deal.IDealWithBean;
import com.huangyezhaobiao.gtui.AppStateFactory;
import com.huangyezhaobiao.notification.NotificationExecutor;
import com.huangyezhaobiao.push.BiddingMessageReceiver;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
        import com.huangyezhaobiao.utils.LogUtils;
        import com.huangyezhaobiao.utils.PushUtils;
        import com.huangyezhaobiao.utils.UserUtils;
        import com.lidroid.xutils.http.RequestParams;
        import com.lidroid.xutils.http.ResponseInfo;
        import com.wuba.loginsdk.external.LoginClient;

        import java.util.List;

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
        LogUtils.LogV("pushBean",pushBean.toString());

        BiddingMessageReceiver.PushHandler handler = BiddingApplication.getHandler();
        //根据不同的状态做不同的东西
        IDealWithBean dealWithBean = AppStateFactory.getDealFromState(this);
        //处理
        dealWithBean.dealPushBean(this,pushBean,notificationExecutor,app.getCurrentNotificationListener(),jsonResult);
        //  VoiceManager.getVoiceManagerInstance(this).speakLocalMessage("这是一条重要的消息");
        reportToServerForGeTuiArrived(pushBean);
    }

    private RequestParams getRequestParams(){
        RequestParams params = new RequestParams();
        List<RequestParams.HeaderItem> list = params.getHeaders();
        if(list!= null && list.size() != 0){
            list.clear();
        }
//        params.addHeader("ppu", UserUtils.getUserPPU(BiddingApplication.getAppInstanceContext()));
//        params.addHeader("userId",UserUtils.getPassportUserId(BiddingApplication.getAppInstanceContext()));

        params.addHeader("ppu", LoginClient.doGetPPUOperate(BiddingApplication.getAppInstanceContext()));
        params.addHeader("userId",UserUtils.getUserId(BiddingApplication.getAppInstanceContext()));
//        params.addHeader("userId","34675169722113");  //bigbang1
//        params.addHeader("userId","34680567140865");  //bigbang2
//        params.addHeader("userId","34680592616449");  //bigbang3
//        params.addHeader("userId","34964986925569");  //bigbang4
//        params.addHeader("userId","35606241334273");  //bigbang5
//        params.addHeader("userId","35606250707713");  //bigbang6
//        params.addHeader("userId","35606332708865");  //bigbang7
        params.addHeader("version", "6");
        params.addHeader("platform", "1");
        params.addHeader("UUID", PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()));

        params.addHeader("isSon", UserUtils.getIsSon(BiddingApplication.getAppInstanceContext()));
        params.addHeader("suserId", LoginClient.doGetUserIDOperate(BiddingApplication.getAppInstanceContext()));
        return params;
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
        RequestParams params = new RequestParams();

        //2016.5.3 add end
        try {
            HTTPTools.newHttpUtilsInstance().doGet(url, getRequestParams(), new HttpRequestCallBack() {
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
