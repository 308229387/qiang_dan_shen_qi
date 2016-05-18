package com.huangyezhaobiao.bean.HYEventBean;

/**
 * Created by 58 on 2016/5/17.
 */
public class CommonBean {

    /**
     * 渠道ID
     */
    private String ca;
    /**
     * 客户端版本
     */
    private String cb;
    /**
     * 客户端操作系统名称
     */
    private String cc;
    /**
     * 分辨率
     */
    private String ce;
    /**
     * 手机品牌
     */
    private String ch;

    /**
     * 网络类型
     */
    private String ci;
    /**
     * mac地址
     */
    private String ck;

    /**
     * 设备型号
     */
    private String cl;
    /**
     * 设备id(客户端生成,唯一标识一个设备)
     */
    private String cm;
    /**
     * 手机号
     */
    private String cn;

    /**
     * 默认的构造方法必须不能省，不然不能解析
    */
    public CommonBean() {
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getCb() {
        return cb;
    }

    public void setCb(String cb) {
        this.cb = cb;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCe() {
        return ce;
    }

    public void setCe(String ce) {
        this.ce = ce;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getCk() {
        return ck;
    }

    public void setCk(String ck) {
        this.ck = ck;
    }

    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    public String getCm() {
        return cm;
    }

    public void setCm(String cm) {
        this.cm = cm;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }


}
