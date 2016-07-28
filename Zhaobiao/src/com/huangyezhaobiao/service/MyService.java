package com.huangyezhaobiao.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.utils.LogUtils;

import wuba.zhaobiao.common.activity.SplashActivity;

/**
 * Created by shenzhixin on 2015/11/9.
 */
public class MyService extends Service{
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            LogUtils.LogE("broadcast", "service handle..");
            super.handleMessage(msg);
            handler.sendEmptyMessageDelayed(1,60*1*1000);
        }
    };
    /**
     * 设置一个notification
     * @param context
     * @return
     */
    private Notification makeServiceNotification(Context context){
        Intent intent = null;
        Notification notification = new Notification(R.drawable.launcher,"抢单神器",System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
        intent = new Intent(this,SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context, "抢单神器", "点击进入查看可抢标地", pendingIntent);
        return notification;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //将service设置为前台进程，保证这个服务不会被杀死
        startForeground(MyService.class.hashCode(),makeServiceNotification(this));
        handler = new Handler();
        handler.sendEmptyMessage(1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   Toast.makeText(this,"startCommand",0).show();

        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        startMiPushService1();
        startMiPushService2();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        //重新启动自己
        startSelf();
    }

    private void startSelf(){
        Intent intent = new Intent(this,MyService.class);
        startService(intent);
    }


    private void startMiPushService1(){
        Intent intent = new Intent();
        intent.setClassName(this,"com.xiaomi.push.service.XMPushService");
        startActivity(intent);
    }

    private void startMiPushService2(){
        Intent intent = new Intent();
        intent.setClassName(this,"com.xiaomi.mipush.sdk.MessageHandleService");
        startActivity(intent);
    }
}
