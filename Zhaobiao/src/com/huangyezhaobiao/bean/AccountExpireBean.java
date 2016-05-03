package com.huangyezhaobiao.bean;

/**
 * Created by shenzhixin on 2015/12/17.
 * 网灵通过期的实体
 */
public class AccountExpireBean {
    /**
     * 已到期
     */
    public static String ALREADY_EXPIRE = "1";
    /**
     * 未到期
     */
    public static String UNEXPIRE = "0";
    private String expireState;
    private String msg;

    public String getExpireState() {
        return expireState;
    }

    public void setExpireState(String expireState) {
        this.expireState = expireState;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public AccountExpireBean(String expireState, String msg) {
        this.expireState = expireState;
        this.msg = msg;
    }

    public AccountExpireBean() {
    }
}
