package com.huangyezhaobiao.bean.tt;

import java.util.List;

/**
 * Created by shenzhixin on 2015/12/9.
 * 第一次提交的页面的展示的bean
 */
public class RefundFirstCommitBean extends RefundBaseBean{
    private String orderId;
    private List<RefundFirstReasonEntity> cancelReasons;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public List<RefundFirstReasonEntity> getCancelReasons() {
        return cancelReasons;
    }

    public void setCancelReasons(List<RefundFirstReasonEntity> cancelReasons) {
        this.cancelReasons = cancelReasons;
    }

    @Override
    public String toString() {
        return "RefundFirstCommitBean{" +
                "orderId='" + orderId + '\'' +
                ", cancelReasons=" + cancelReasons +
                '}';
    }
}
