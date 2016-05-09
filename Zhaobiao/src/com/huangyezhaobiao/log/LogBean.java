package com.huangyezhaobiao.log;

/**
 * com.huangyezhaobiao.log
 * Created by shenzhixin on 2016/5/9 14:21.
 * 日志的实体bean
 */
public class LogBean {
    private int id;
    private String name;
    public LogBean(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
