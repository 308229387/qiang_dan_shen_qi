package com.huangyezhaobiao.bean.tt;

/**
 * Created by shenzhixin on 2015/12/9.
 * 退单过期时的接口实体bean
 *
 */
public class RefundCloseTimeBean extends RefundBaseBean{
    private String orderId;
    private String openTime;
    private String closeTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}
