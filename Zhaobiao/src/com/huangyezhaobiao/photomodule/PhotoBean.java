package com.huangyezhaobiao.photomodule;

import java.util.ArrayList;

/**
 * Created by shenzhixin on 2015/10/18.
 */
public class PhotoBean {
    //从提交评价那去选图
    public static final int TYPE_COMMENT = 1;
    public static final String KEY_TYPE ="type";
    //从修改/发布服务那去选图
    public static final int TYPE_MODIFY_SKILLS =2 ;
    //从晚上个人信息页面那去选图
    public static final int TYPE_IMPROVE_INFOR = 3;
    //从个人信息页进入
    public static final int TYPE_PERSONAL_INFO = 4;
    //交易详情界面进入
    public static final int TYPE_DEAL_DETAIL = 5;
    //发布需求界面
    public static final int TYPE_DEMAND_POST = 6;
    //发布预约界面
    public static final int TYPE_DEAL_POST = 7;

    private int type = -1;
    private ArrayList<MediaPicBean> beans = new ArrayList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<MediaPicBean> getBeans() {
        return beans;
    }

    public void setBeans(ArrayList<MediaPicBean> beans) {
        this.beans = beans;
    }
}
