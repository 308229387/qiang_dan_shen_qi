package com.huangyezhaobiao.bean;

/**
 * Created by shenzhixin on 2015/12/12.
 * 消费记录的实体
 * "costTime":"2015-05-15 15:00:01", //具体到秒，消费时间年月日 时分秒
 "title":"抢单消费",
 "cost":"-79元",
 */
public class ConsumeItemBean {
    private String costTime;
    private String title;
    private String cost;

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "ConsumeItemBean{" +
                "costTime='" + costTime + '\'' +
                ", title='" + title + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
