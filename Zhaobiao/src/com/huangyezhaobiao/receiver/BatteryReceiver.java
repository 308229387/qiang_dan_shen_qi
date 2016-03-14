package com.huangyezhaobiao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.utils.LogUtils;

/**
 * Created by shenzhixin on 2015/11/11.
 * 监听手机电量变化的广播接收者，如果电量变化，收到后就去唤醒myService
 */
public class BatteryReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(context!=null){
            LogUtils.LogE("broadcast","battery changed..");
            Intent serviceIntent = new Intent(context, MyService.class);
            context.startService(serviceIntent);
        }
    }
}
