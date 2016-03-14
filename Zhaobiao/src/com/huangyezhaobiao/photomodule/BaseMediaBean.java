package com.huangyezhaobiao.photomodule;


import java.io.Serializable;

/**
 * Created by shenzhixin on 2015/9/24.
 * 选择媒体的3个bean
 */
public class BaseMediaBean implements Serializable {
    /**
     * 媒体类型
     */
    protected int type;
    protected boolean local = true;//设置这个文件是不是本地的
    protected String url;
    protected MediaAdapter.ViewHolder holder;

    public int getType() {
        return type;
    }

    public void setType(int media_type) {
        this.type = media_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BaseMediaBean(int media_type, String logo_url) {
        this.type = media_type;
        this.url = logo_url;
    }

    public BaseMediaBean() {

    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
}
