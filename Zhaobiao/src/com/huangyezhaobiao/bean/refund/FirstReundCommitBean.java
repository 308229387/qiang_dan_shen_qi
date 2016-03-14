package com.huangyezhaobiao.bean.refund;

import java.io.File;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/17.
 * userId=
 orderId=
 cancelReasons="2" //退单原因
 detailDesc=""
 image1=
 image2=
 */
public class FirstReundCommitBean {
    private String userId;
    private String orderId;
    private String cancelReasons;
    private String detailDesc;
    private List<File> images;


    public FirstReundCommitBean(String userId, String orderId, String cancelReasons, String detailDesc, List<File> images) {
        this.userId = userId;
        this.orderId = orderId;
        this.cancelReasons = cancelReasons;
        this.detailDesc = detailDesc;
        this.images = images;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCancelReasons() {
        return cancelReasons;
    }

    public void setCancelReasons(String cancelReasons) {
        this.cancelReasons = cancelReasons;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public List<File> getImages() {
        return images;
    }

    public void setImages(List<File> images) {
        this.images = images;
    }
}
