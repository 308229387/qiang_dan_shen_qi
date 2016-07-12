package com.huangyezhaobiao.bean.HYEventBean;

/**
 * Created by 58 on 2016/5/17.
 */
public class DataBean {

    /**
     * 埋点类型(统计事件的)
     */
    protected String co;
    /**
     * 用户Userid
     */
    protected String sa;
    /**
     * 客户端时间,13位时间戳
     */
    protected String cq;
    /**
     * 模式状态---服务、休息
     */
    private String modelState;
    /**
     * 标地id
     */
    private String s1;
    /**
     * 抢单入口方式-----列表页、详情页、弹窗
     */
    private String grabOrderStyle;
    /**
     * 抢单状态-----已抢、可抢
     */
    private String grabOrderState;
    /**
     * 锁屏状态----安卓:锁屏、未锁屏
     */
    private String lockScreenState;
    /**
     *服务状态----待服务/服务中/已结束
     */
    private String serviceState;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 我的抢单模块拨打电话方式----列表、详情
     */
    private String callStyle;
    /**
     * 接单类别
     */
    private String orderCate;
    /**
     * 服务区域
     */
    private String serviceArea;
    /**
     * 装修类型
     */
    private String decorateType;
    /**
     * 装修费用
     */
    private String decorateCost;
    /**
     * 装修面积
     */
    private String decorateArea;
    /**
     * 装修方式
     */
    private String decorateStyle;
    /**
     * 注册类型
     */
    private String registrationType;
    /**
     * 代理地址
     */
    private String agentAddress;
    /**
     * 代理记账
     */
    private String agencyBookkeeping;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 登录状态
     */
    private String loginState;
    /**
     * 失败原因
     */
    private String failureReason;
    /**
     * 埋点类型(统计页面停留时间的)
     */
    private String cr;
    /**
     * 页面停留时间
     */
    private String cs;

    /**
     * 上一个页面
     */
    private String pageFrom;

    /**
     * 默认的构造方法必须不能省，不然不能解析
     */
    public DataBean() {
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getSa() {
        return sa;
    }

    public void setSa(String sa) {
        this.sa = sa;
    }

    public String getCq() {
        return cq;
    }

    public void setCq(String cq) {
        this.cq = cq;
    }

    public String getModelState() {
        return modelState;
    }

    public void setModelState(String modelState) {
        this.modelState = modelState;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getGrabOrderStyle() {
        return grabOrderStyle;
    }

    public void setGrabOrderStyle(String grabOrderStyle) {
        this.grabOrderStyle = grabOrderStyle;
    }

    public String getGrabOrderState() {
        return grabOrderState;
    }

    public void setGrabOrderState(String grabOrderState) {
        this.grabOrderState = grabOrderState;
    }

    public String getLockScreenState() {
        return lockScreenState;
    }

    public void setLockScreenState(String lockScreenState) {
        this.lockScreenState = lockScreenState;
    }

    public String getServiceState() {
        return serviceState;
    }

    public void setServiceState(String serviceState) {
        this.serviceState = serviceState;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCallStyle() {
        return callStyle;
    }

    public void setCallStyle(String callStyle) {
        this.callStyle = callStyle;
    }

    public String getOrderCate() {
        return orderCate;
    }

    public void setOrderCate(String orderCate) {
        this.orderCate = orderCate;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getDecorateType() {
        return decorateType;
    }

    public void setDecorateType(String decorateType) {
        this.decorateType = decorateType;
    }

    public String getDecorateCost() {
        return decorateCost;
    }

    public void setDecorateCost(String decorateCost) {
        this.decorateCost = decorateCost;
    }

    public String getDecorateArea() {
        return decorateArea;
    }

    public void setDecorateArea(String decorateArea) {
        this.decorateArea = decorateArea;
    }

    public String getDecorateStyle() {
        return decorateStyle;
    }

    public void setDecorateStyle(String decorateStyle) {
        this.decorateStyle = decorateStyle;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getAgencyBookkeeping() {
        return agencyBookkeeping;
    }

    public void setAgencyBookkeeping(String agencyBookkeeping) {
        this.agencyBookkeeping = agencyBookkeeping;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginState() {
        return loginState;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public String getPageFrom() {
        return pageFrom;
    }

    public void setPageFrom(String pageFrom) {
        this.pageFrom = pageFrom;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "co='" + co + '\'' +
                ", sa='" + sa + '\'' +
                ", cq='" + cq + '\'' +
                ", modelState='" + modelState + '\'' +
                ", s1='" + s1 + '\'' +
                ", grabOrderStyle='" + grabOrderStyle + '\'' +
                ", grabOrderState='" + grabOrderState + '\'' +
                ", lockScreenState='" + lockScreenState + '\'' +
                ", serviceState='" + serviceState + '\'' +
                ", orderId='" + orderId + '\'' +
                ", callStyle='" + callStyle + '\'' +
                ", orderCate='" + orderCate + '\'' +
                ", serviceArea='" + serviceArea + '\'' +
                ", decorateType='" + decorateType + '\'' +
                ", decorateCost='" + decorateCost + '\'' +
                ", decorateArea='" + decorateArea + '\'' +
                ", decorateStyle='" + decorateStyle + '\'' +
                ", registrationType='" + registrationType + '\'' +
                ", agentAddress='" + agentAddress + '\'' +
                ", agencyBookkeeping='" + agencyBookkeeping + '\'' +
                ", userName='" + userName + '\'' +
                ", loginState='" + loginState + '\'' +
                ", failureReason='" + failureReason + '\'' +
                ", cr='" + cr + '\'' +
                ", cs='" + cs + '\'' +
                '}';
    }
}
