package wuba.zhaobiao.respons;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 58 on 2016/9/8.
 */
public class OrderDetailRespons implements Serializable{

    private  ArrayList<bean> basicDetail;

    private ArrayList<bean> orderDetail;

    private PriceBean priceDetail;

    private ArrayList<bean> callList;

    private String orderState;

    public ArrayList<bean> getBasicDetail() {
        return basicDetail;
    }

    public void setBasicDetail(ArrayList<bean> basicDetail) {
        this.basicDetail = basicDetail;
    }

    public ArrayList<bean> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(ArrayList<bean> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public PriceBean getPriceDetail() {
        return priceDetail;
    }

    public void setPriceDetail(PriceBean priceDetail) {
        this.priceDetail = priceDetail;
    }

    public ArrayList<bean> getCallList() {
        return callList;
    }

    public void setCallList(ArrayList<bean> callList) {
        this.callList = callList;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public class bean implements Serializable{
        private String title;
        private String content;

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
    }

    public class PriceBean  implements Serializable{
        private String title;
        private String promotionPrice;
        private String originPrice;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPromotionPrice() {
            return promotionPrice;
        }

        public void setPromotionPrice(String promotionPrice) {
            this.promotionPrice = promotionPrice;
        }

        public String getOriginPrice() {
            return originPrice;
        }

        public void setOriginPrice(String originPrice) {
            this.originPrice = originPrice;
        }
    }

}
