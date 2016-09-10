package com.huangyezhaobiao.bean.mydetail.OrderDetail;

/**
 * Created by 58 on 2016/9/3.
 */
public class CallClassifyEntity {
    private String callClassifyId;
    private int callClassifyImage;
    private String callClassifyName;

    public String getCallClassifyId() {
        return callClassifyId;
    }

    public void setCallClassifyId(String callClassifyId) {
        this.callClassifyId = callClassifyId;
    }

    public int getCallClassifyImage() {
        return callClassifyImage;
    }

    public void setCallClassifyImage(int callClassifyImage) {
        this.callClassifyImage = callClassifyImage;
    }

    public String getCallClassifyName() {
        return callClassifyName;
    }

    public void setCallClassifyName(String callClassifyName) {
        this.callClassifyName = callClassifyName;
    }

    @Override
    public String toString() {
        return "CallClassifyEntity{" +
                "callClassifyId='" + callClassifyId + '\'' +
                ", callClassifyImage=" + callClassifyImage +
                ", callClassifyName='" + callClassifyName + '\'' +
                '}';
    }
}
