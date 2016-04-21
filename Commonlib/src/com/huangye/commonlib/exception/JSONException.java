package com.huangye.commonlib.exception;

/**
 * author keyes
 * time 2016/4/20 20:17
 * email：1175426782@qq.com
 * param：
 * descript：
 */
public class JSONException extends ZBException {
    public JSONException(){
        super();
    }

    public JSONException(String msg,Exception e){
        super(msg,e);
    }

    public JSONException(String msg){
        super(msg);
    }

    public JSONException(Exception e){
        super(e);
    }
}
