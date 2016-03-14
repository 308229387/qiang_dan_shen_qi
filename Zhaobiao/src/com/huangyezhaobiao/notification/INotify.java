package com.huangyezhaobiao.notification;

/**
 * Created by shenzhixin on 2015/12/16.
 *
 */
public interface INotify<T> {
    void dealPushMessage();

    void setNotifyMessage(T t);
}
