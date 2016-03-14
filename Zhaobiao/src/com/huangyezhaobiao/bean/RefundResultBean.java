package com.huangyezhaobiao.bean;

/**
 * Created by shenzhixin on 2015/12/14.
 * "orderId":"",		//订单ID
 "cancelOrderId":"",	//退单ID
 “cancelResult”:"退单失败，证据不足", //退单处理结果
 “cancelReason”:"联系不上用户 需求不符 其他", //退单原因，拼接成字符串
 “detailDesc”:"",  //退单详情描述
 “evidence”:"已上传6张图片" //上传证据
 */
public class RefundResultBean {
    private String orderId;
    private String cancelOrderId;
    private String cancelResult;
    private String cancelReason;
    private String detailDesc;
    private String evidence;
    private String additionevidence;

    public String getAdditionevidence() {
        return additionevidence;
    }

    public void setAdditionevidence(String additionevidence) {
        this.additionevidence = additionevidence;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCancelOrderId() {
        return cancelOrderId;
    }

    public void setCancelOrderId(String cancelOrderId) {
        this.cancelOrderId = cancelOrderId;
    }

    public String getCancelResult() {
        return cancelResult;
    }

    public void setCancelResult(String cancelResult) {
        this.cancelResult = cancelResult;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }
}
