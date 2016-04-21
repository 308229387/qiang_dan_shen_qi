package com.huangye.commonlib.exception;

/**
 * author keyes
 * time 2016/4/20 20:13
 * email：1175426782@qq.com
 * param：
 * descript：
 */
public class ZBException extends RuntimeException {

    public ZBException(){
        super();
    }

    public ZBException(String msg,Exception e){
        super(msg,e);
    }

    public ZBException(String msg){
        super(msg);
    }

    public ZBException(Exception e){
        super(e);
    }
}
