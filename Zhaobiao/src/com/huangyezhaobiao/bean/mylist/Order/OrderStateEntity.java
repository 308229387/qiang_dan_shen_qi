package com.huangyezhaobiao.bean.mylist.Order;

/**
 * Created by 58 on 2016/6/29.
 */
public class OrderStateEntity {
    private String orderStateId;
    private String orderState;

    public String getOrderStateId() {
        return orderStateId;
    }

    public void setOrderStateId(String orderStateId) {
        this.orderStateId = orderStateId;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    @Override
    public String toString() {
        return "OrderStateEntity{" +
                "orderStateId='" + orderStateId + '\'' +
                ", orderState='" + orderState + '\'' +
                '}';
    }
}
