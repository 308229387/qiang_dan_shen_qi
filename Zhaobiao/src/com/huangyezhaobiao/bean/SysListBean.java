package com.huangyezhaobiao.bean;

import java.io.Serializable;

/**
 * Created by shenzhixin on 2015/12/15.
 * 系统列表的提示bean
 */
public class SysListBean implements Serializable{
    private String sysType;
    private String time;
    private String title;
    private String content;
    private String sysId;
    private String url;

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SysListBean{" +
                "sysType='" + sysType + '\'' +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sysId='" + sysId + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
