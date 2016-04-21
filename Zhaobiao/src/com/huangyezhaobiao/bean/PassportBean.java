package com.huangyezhaobiao.bean;

import com.huangye.commonlib.utils.NetBean;

import java.io.Serializable;

/**
 * author keyes
 * time 2016/4/21 10:35
 * email：1175426782@qq.com
 * param：
 * descript：
 */
public class PassportBean extends NetBean implements Serializable{
    private String code;
    private String errorMsg;
    private String userId;
    private String ppu;

    public String getPpu() {
        return ppu;
    }

    public void setPpu(String ppu) {
        this.ppu = ppu;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PassportBean{" +
                "code='" + code + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
