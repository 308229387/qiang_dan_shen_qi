package com.huangyezhaobiao.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.OrderDetailActivity;
import com.huangyezhaobiao.activity.SplashActivity;
import com.huangyezhaobiao.bean.push.PushToPassBean;

import java.util.Timer;
import java.util.TimerTask;

public class AlertService extends Service {

    private final long MINPostTime = 15 * 60 * 1000;
    private Timer mTimer;

    private PushToPassBean receivePassBean;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.sendEmptyMessageDelayed(1, 60 * 1 * 1000);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }
    /**
     * 设置一个notification
     * @param context
     * @return
     */
    private Notification makeServiceNotification(Context context){
        Intent intent = null;
        Notification notification = new Notification(R.drawable.launcher,"抢单神器",System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
        intent = new Intent(this,OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("passBean", receivePassBean);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context, "抢单神器", "刚到手的客户都快被抢跑了，还不快来联系客户!", pendingIntent);
        return notification;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        handler.sendEmptyMessage(1);

        if (null != mTimer) {
            mTimer.cancel();
        }
        mTimer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receivePassBean=(PushToPassBean)intent.getSerializableExtra("passBean");
        return START_STICKY;
    }

    @Override
    //当调用者使用startService()方法启动Service时，该方法被调用
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        mTimer.schedule(new UploadTask(), MINPostTime);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        //重新启动自己
        startSelf();
        if (null != mTimer) {
            mTimer.cancel();
        }
    }

    private void startSelf(){
        Intent intent = new Intent(this,AlertService.class);
        startService(intent);
    }

    class UploadTask extends TimerTask {
        @Override
        public void run() {
            //将service设置为前台进程，保证这个服务不会被杀死
            startForeground(AlertService.class.hashCode(), makeServiceNotification(AlertService.this));
        }
    }


}
