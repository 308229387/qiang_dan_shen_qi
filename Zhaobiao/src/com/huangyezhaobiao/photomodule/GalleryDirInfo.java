package com.huangyezhaobiao.photomodule;

import android.text.TextUtils;

/**
 * Created by shenzhixin on 2015/9/26.
 * 各个文件夹的图片的地址
 */
public class GalleryDirInfo {
    /**
     * 路径的地址
     */
    private String dir_path;
    /**
     * 这个路径下的第一张图片的地址
     */
    private String first_pic_path;

    /**
     * 文件夹名
     */
    private String name;

    /**
     * 文件夹中的图片的数量
     */
    private int count;

    public GalleryDirInfo(String dir_path, String first_pic_path, String name, int count) {
        this.dir_path = dir_path;
        this.first_pic_path = first_pic_path;
        this.name = name;
        this.count = count;
    }

    public GalleryDirInfo(){

    }

    public String getDir_path() {
        return dir_path;
    }

    public void setDir_path(String dir_path) {
        this.dir_path = dir_path;
        if(TextUtils.isEmpty(dir_path)) return;
        int lastIndexOf = this.dir_path.lastIndexOf("/");
        this.name = this.dir_path.substring(lastIndexOf+1);
    }

    public String getFirst_pic_path() {
        return first_pic_path;
    }

    public void setFirst_pic_path(String first_pic_path) {
        this.first_pic_path = first_pic_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
