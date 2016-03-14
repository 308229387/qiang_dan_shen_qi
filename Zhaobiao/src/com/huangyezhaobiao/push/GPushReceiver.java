package com.huangyezhaobiao.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.service.GPushService;
import com.igexin.sdk.PushConsts;

/**
 * Created by 58 on 2016/1/7.
 */
public class GPushReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.e("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传（payload）数据
                byte[] payload = bundle.getByteArray("payload");
                Log.e("GetuiSdkDemo", "data:" + payload);
                if (payload != null)
                {
                    String data = new String(payload);
                    Log.e("GetuiSdkDemo", "data:" + data);
                    //TODO:把data转化为json，然后一步一步做
                    Intent intent1 = getServiceIntent(context,data);
                    context.startService(intent1);
                }
                break;

            default:
                break;
        }
    }


    private Intent getServiceIntent(Context context,String json){
        Intent intent = GPushService.onNewIntent(context.getApplicationContext(),json);
        return intent;
    }
}
