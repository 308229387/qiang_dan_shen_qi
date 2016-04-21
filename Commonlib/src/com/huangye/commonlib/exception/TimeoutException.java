package com.huangye.commonlib.exception;

/**
 * author keyes
 * time 2016/4/20 20:17
 * email：1175426782@qq.com
 * param：
 * descript：
 */
public class TimeoutException extends ZBException {
    public TimeoutException(){
        super();
    }

    public TimeoutException(String msg,Exception e){
        super(msg,e);
    }

    public TimeoutException(String msg){
        super(msg);
    }

    public TimeoutException(Exception e){
        super(e);
    }
}
