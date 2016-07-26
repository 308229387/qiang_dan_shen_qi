package com.huangyezhaobiao.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SongYongmeng on 2016/7/26.
 */
public class result implements Serializable {

        private ArrayList<Bind> data;

        public ArrayList<Bind> getData() {
            return data;
        }

        public void setData(ArrayList<Bind> data) {
            this.data = data;
        }

    public class Bind implements Serializable {
        private String bidId;
        private String bidState;
        private String cateId;
        private String cleanSpace;
        private String displayType;
        private String fee;
        private String location;
        private String originFee;
        private String pushId;
        private String pushTurn;
        private String serveTime;
        private String time;
        private String title;

        public String getBidId() {
            return bidId;
        }

        public void setBidId(String bidId) {
            this.bidId = bidId;
        }

        public String getBidState() {
            return bidState;
        }

        public void setBidState(String bidState) {
            this.bidState = bidState;
        }

        public String getCateId() {
            return cateId;
        }

        public void setCateId(String cateId) {
            this.cateId = cateId;
        }

        public String getCleanSpace() {
            return cleanSpace;
        }

        public void setCleanSpace(String cleanSpace) {
            this.cleanSpace = cleanSpace;
        }

        public String getDisplayType() {
            return displayType;
        }

        public void setDisplayType(String displayType) {
            this.displayType = displayType;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getOriginFee() {
            return originFee;
        }

        public void setOriginFee(String originFee) {
            this.originFee = originFee;
        }

        public String getPushId() {
            return pushId;
        }

        public void setPushId(String pushId) {
            this.pushId = pushId;
        }

        public String getPushTurn() {
            return pushTurn;
        }

        public void setPushTurn(String pushTurn) {
            this.pushTurn = pushTurn;
        }

        public String getServeTime() {
            return serveTime;
        }

        public void setServeTime(String serveTime) {
            this.serveTime = serveTime;
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
    }

}
