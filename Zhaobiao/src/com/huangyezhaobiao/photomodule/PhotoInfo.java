package com.huangyezhaobiao.photomodule;

import java.io.Serializable;

/**
 * Created by shenzhixin on 2015/9/24.
 */
public class PhotoInfo implements Serializable {
    /**
     * 路径名
     */
    private String path;
    /**
     * 是否需要压缩
     */
    private boolean needCompress;

    private boolean isChecked;
    public PhotoInfo(String path, boolean needCompress,boolean isChecked) {
        this.path         = path;
        this.needCompress = needCompress;
        this.isChecked    = isChecked;
    }


    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
    }

    public boolean isChecked(){
        return isChecked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isNeedCompress() {
        return needCompress;
    }

    public void setNeedCompress(boolean needCompress) {
        this.needCompress = needCompress;
    }
}
