package com.huangyezhaobiao.deal;

import android.content.Context;
import android.util.Log;

import com.huangyezhaobiao.activity.LockActivity;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.notification.NotificationExecutor;
import com.huangyezhaobiao.push.BiddingMessageReceiver;
import com.huangyezhaobiao.utils.KeyguardUtils;
import com.huangyezhaobiao.utils.LockUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.StateUtils;

/**
 * Created by 58 on 2016/1/25.
 * //通话过程中的bean
 */
public class PhoneCallingWithBean implements IDealWithBean{

    @Override
    public void dealPushBean(final Context context,final PushBean bean,final NotificationExecutor notificationExecutor,final INotificationListener listener,final String content) {
            Log.e("shenzhixin", "type:"+bean.getTag()+",content:"+content);
            BiddingMessageReceiver.PushHandler handler = BiddingApplication.getHandler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(bean==null){
                        PushUtils.pushList.clear();
                        return ;
                    }
                    if(bean.getTag()==100 ) {
                        PushUtils.pushList.clear();
                        notificationExecutor.onMessageArrived(content);
                    }else{
                        //TODO:通知栏
                        notificationExecutor.onMessageArrived(content);
                    }
                }
            });


    }
}
