package com.huangyezhaobiao.notification;

/**
 * Created by shenzhixin on 2015/12/16.
 */
public class NotificationExecutor {
    private INotify iNotify;


    public void setiNotify(INotify iNotify) {
        this.iNotify = iNotify;
    }

    public <T> void onMessageArrived(T t){
        iNotify.setNotifyMessage(t);
        executeNotification();
    }
    private void executeNotification(){
        iNotify.dealPushMessage();
    }
}
