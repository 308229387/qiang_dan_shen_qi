package com.huangyezhaobiao.deal;

import android.content.Context;

import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.notification.NotificationExecutor;

/**
 * Created by 58 on 2016/1/7.
 * 处理推送过来的实体bean的接口
 */
public interface IDealWithBean {
    public void dealPushBean(Context context,PushBean pushBean,NotificationExecutor notificationExecutor,INotificationListener listener,String content);
}
