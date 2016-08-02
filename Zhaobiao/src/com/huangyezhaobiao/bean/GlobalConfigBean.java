package com.huangyezhaobiao.bean;

/**
 * com.huangyezhaobiao.bean
 * Created by shenzhixin on 2016/5/3 16:33.
 */
public class GlobalConfigBean {

    /**
     * 用户是否设置
     */
    public static final String KEY_SETSTATE = "setState";
    /**
     * 是否增量拉取的key
     */
    public static final String KEY_isIncrementalPull="isIncrementalPull";
    /**
     * 用户电话的phone
     */
    public static final String KEY_USERPHONE = "userPhone";

    /**
     * 是否网灵通过期的状态
     */
    public static final String KEY_WLT_EXPIRE = "expire_status";

    /**
     * 网灵通如果过期的msg
     */
    public static final String KEY_WLT_EXPIRE_MSG = "expire_msg";


    private AccountExpireBean wltAlertResult;
    private UserPhoneBean     userPhoneResult;
    private UploadTokenBean   uploadTokenMap;
    private String            isIncrementalPull;

    public String getIsIncrementalPull() {
        return isIncrementalPull;
    }

    public void setIsIncrementalPull(String isIncrementalPull) {
        this.isIncrementalPull = isIncrementalPull;
    }

    public AccountExpireBean getWltAlertResult() {
        return wltAlertResult;
    }

    public void setWltAlertResult(AccountExpireBean wltAlertResult) {
        this.wltAlertResult = wltAlertResult;
    }

    public UserPhoneBean getUserPhoneResult() {
        return userPhoneResult;
    }

    public void setUserPhoneResult(UserPhoneBean userPhoneResult) {
        this.userPhoneResult = userPhoneResult;
    }

    public UploadTokenBean getUploadTokenMap() {
        return uploadTokenMap;
    }

    public void setUploadTokenMap(UploadTokenBean uploadTokenMap) {
        this.uploadTokenMap = uploadTokenMap;
    }

    @Override
    public String toString() {
        return "GlobalConfigBean{" +
                "wltAlertResult=" + wltAlertResult +
                ", userPhoneResult=" + userPhoneResult +
                ", uploadTokenMap=" + uploadTokenMap +
                ", isIncrementalPull='" + isIncrementalPull + '\'' +
                '}';
    }
}
