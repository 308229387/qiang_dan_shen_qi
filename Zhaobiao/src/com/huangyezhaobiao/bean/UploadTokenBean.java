package com.huangyezhaobiao.bean;

/**
 * com.huangyezhaobiao.bean
 * Created by shenzhixin on 2016/5/3 17:03.
 */
public class UploadTokenBean {
    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return "UploadTokenBean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
