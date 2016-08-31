package com.huangyezhaobiao.deal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UserUtils;

/**
 * Created by 58 on 2016/1/7.
 * 锁屏内的处理
 */
public class LockDealWithBean implements IDealWithBean {
    @Override
    public void dealPushBean(final Context context, final PushBean bean,final NotificationExecutor notificationExecutor, INotificationListener listener,final String content) {
        if (context != null && KeyguardUtils.isLockScreen(context) && !KeyguardUtils.notLock && StateUtils.state == 1 && !KeyguardUtils.onLock) {//锁屏界面,亮屏界面，服务模式下
            Log.e("shenzhixinUI", "lock");
            BiddingMessageReceiver.PushHandler handler = BiddingApplication.getHandler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(bean==null){
                        PushUtils.pushList.clear();
                        return ;
                    }
                    if(bean.getTag()==100 ) {
                        if(SPUtils.getServiceState(context).equals("1")) {
                            String isSon = UserUtils.getIsSon(context);
                            if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
                                String rbac = UserUtils.getRbac(context);
                                if (!TextUtils.isEmpty(rbac)
                                        && TextUtils.equals("1", rbac) || TextUtils.equals("5", rbac)) {
                                    LogUtils.LogV("LockActivity", "LockDealWithBean" + "锁屏没有权限弹窗");;
                                }else{
                                    KeyguardUtils.goToKeyguardActivity(context, LockActivity.class);
                                }

                            } else {
                                KeyguardUtils.goToKeyguardActivity(context, LockActivity.class);
                            }
                            LockUtils.needLock = true;
                        }else{
                            PushUtils.pushList.clear();
                        }
                    }else{
                        //TODO:通知栏
                       notificationExecutor.onMessageArrived(content);
                    }
                }
            });
            return;
        }
    }
}
