package com.huangyezhaobiao.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.MessageCenterActivity;
import com.huangyezhaobiao.activity.SystemNotiListActivity;
import com.xiaomi.mipush.sdk.MiPushMessage;

/**
 * Created by shenzhixin on 2015/12/16.
 */
public class MiPushNotify implements INotify<MiPushMessage>{
    MiPushMessage miPushMessage;
    private String type;
    private String pushTime;
    private String title;
    private String content;
    private Context context;
    public MiPushNotify(Context context){
        this.context = context;
    }
    @Override
    public void dealPushMessage() {
        Log.e("shenzhixinPushsh","miPushMessage:"+miPushMessage);
        initJson(miPushMessage);
    }

    @Override
    public void setNotifyMessage(MiPushMessage miPushMessage) {
        this.miPushMessage = miPushMessage;
    }


    private  void initJson(MiPushMessage message) {
        try {
            Intent intent = new Intent();
            JSONObject obj = JSON.parseObject(message.getContent());
            JSONObject extMap = JSON.parseObject(obj.getString("extMap"));
            JSONObject detail = JSON.parseObject(extMap.getString("detail"));
            JSONObject info = JSON.parseObject(detail.getString("info"));
            fillType(extMap);
            fillTime(extMap);
            Log.e("shenzhixinPushshss","title"+title+",content:"+content+",type:"+type);
            if("103".equals(type)){
                fillContent(info.getString("value"));
                title = "系统通知";
                intent.setClass(context, SystemNotiListActivity.class);
            }else{
                fillContent("您有一条新消息");
                title = "消息";
                intent.setClass(context, MessageCenterActivity.class);

            }
            startNotify(intent);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("shenzhixinPushshss","error");
        }
    }

    //填充type数据
    private void fillType(JSONObject jsonObject){
        type = jsonObject.getString("type");
    }

    //填充time数据
    private void fillTime(JSONObject jsonObject){
        pushTime = jsonObject.getString("pushTime");
    }

    //填充content数据
    private void fillContent(String content){
        this.content = content;
    }



    public void startNotify(Intent intent){
        //得到系统通知的管理者
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        Notification notify1 = new Notification(R.drawable.launcher,"抢单神器",System.currentTimeMillis());
        notify1.icon = R.drawable.launcher;
        notify1.tickerText = "抢单神器有信息,请注意查收！";
        notify1.when = System.currentTimeMillis();
        notify1.setLatestEventInfo(context, title,
                content, pendingIntent);
        notify1.number = 1;
        notify1.defaults=Notification.DEFAULT_SOUND;
        notify1.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        // 通过通知管理器来发起通知。如果id不同，则每click，在statu那里增加一个提示
        manager.notify(1, notify1);
        //   Log.e("shenzhixinPushshss", "start notify");
    }
}
