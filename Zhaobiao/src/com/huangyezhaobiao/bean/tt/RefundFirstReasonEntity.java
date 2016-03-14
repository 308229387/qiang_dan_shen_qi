package com.huangyezhaobiao.bean.tt;

/**
 * Created by shenzhixin on 2015/12/9.
 * 退单原因的实体bean
 */
public class RefundFirstReasonEntity {
    private String reasonId;
    private String reasonName;

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    @Override
    public String toString() {
        return "RefundFirstReasonEntity{" +
                "reasonId='" + reasonId + '\'' +
                ", reasonName='" + reasonName + '\'' +
                '}';
    }
}
