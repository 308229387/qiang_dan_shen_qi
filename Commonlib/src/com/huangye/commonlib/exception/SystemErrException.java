package com.huangye.commonlib.exception;

/**
 * author keyes
 * time 2016/4/20 20:19
 * email：1175426782@qq.com
 * param：
 * descript：
 */
public class SystemErrException extends ZBException {
    public SystemErrException(){
        super();
    }

    public SystemErrException(String msg,Exception e){
        super(msg,e);
    }

    public SystemErrException(String msg){
        super(msg);
    }

    public SystemErrException(Exception e){
        super(e);
    }
}
