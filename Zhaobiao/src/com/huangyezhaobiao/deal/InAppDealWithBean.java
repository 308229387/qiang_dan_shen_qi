package com.huangyezhaobiao.deal;

import android.content.Context;

import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.notification.NotificationExecutor;
import com.huangyezhaobiao.push.BiddingMessageReceiver;
import com.huangyezhaobiao.utils.UpdateManager;

/**
 * Created by 58 on 2016/1/7.
 * 在app内部收到了推送的处理方式
 */
public class InAppDealWithBean implements IDealWithBean {
    @Override
    public void dealPushBean(final Context context, final PushBean bean, final NotificationExecutor notificationExecutor, final INotificationListener listener,final String content) {
        if (listener != null) {
                BiddingMessageReceiver.PushHandler handler = BiddingApplication.getHandler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(bean==null){
                            return ;
                        }
                        if (!UpdateManager.needUpdate)//当要强制更新时是不能够传到Activity进行Ui的显示的，但是可以存入数据库，在dealPushMessage处已经存储了

                            listener.onNotificationCome(bean);
                    }
                });
            }
    }
}
