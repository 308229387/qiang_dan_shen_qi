package com.huangyezhaobiao.bean;

/**
 * Created by 58 on 2016/2/24.
 * 打电话时要回调的bean
 */
public class TelephoneBean {
    public static final String SOURCE_LIST   = "0";
    public static final String SOURCE_DETAIL = "1";
    private String orderId;
    private String source;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public TelephoneBean(String orderId, String source) {
        this.orderId = orderId;
        this.source = source;
    }
}
