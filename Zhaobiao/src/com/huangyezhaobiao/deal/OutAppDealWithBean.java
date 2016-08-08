package com.huangyezhaobiao.deal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.huangyezhaobiao.activity.LockActivity;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.notification.NotificationExecutor;
import com.huangyezhaobiao.push.BiddingMessageReceiver;
import com.huangyezhaobiao.utils.KeyguardUtils;
import com.huangyezhaobiao.utils.LockUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.UserUtils;

/**
 * Created by 58 on 2016/1/7.
 * 在应用外部的处理
 */
public class OutAppDealWithBean implements IDealWithBean {
    @Override
    public void dealPushBean(final Context context,final PushBean pushBean, final NotificationExecutor notificationExecutor, INotificationListener listener,final String content) {
        judgeLockOrNot(context);
        BiddingMessageReceiver.PushHandler handler = BiddingApplication.getHandler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(pushBean==null){return ;}
                if(pushBean.getTag()==100 ) {
                    if(SPUtils.getServiceState(context).equals("1")){
                        String isSon = UserUtils.getIsSon(context);
                        if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
                            String rbac = UserUtils.getRbac(context);
                            if (!TextUtils.isEmpty(rbac)
                                    && TextUtils.equals("1", rbac) || TextUtils.equals("3", rbac)) {
                                LogUtils.LogV("LockActivity", "OutAppDealWithBean" + "后台没有权限弹窗");;
                            }else{
                                KeyguardUtils.goToKeyguardActivity(context, LockActivity.class);
                            }

                        } else {
                            KeyguardUtils.goToKeyguardActivity(context, LockActivity.class);
                        }
                    }else{
                        PushUtils.pushList.clear();
                    }
                }else{
                    //TODO:通知栏
                  notificationExecutor.onMessageArrived(content);
                }
            }
        });
    }


    /**
     * 判断应用在后台时有没有锁屏，会影响锁屏界面的显示状态
     * @param context
     */
    private void judgeLockOrNot(Context context){
        if(KeyguardUtils.isLockScreen(context)){
            Log.e("shenzhixinUI", "lock");
            LockUtils.needLock = true;
        }else{
            Log.e("shenzhixinUI","unlock");
            LockUtils.needLock = false;
        }
    }
}
