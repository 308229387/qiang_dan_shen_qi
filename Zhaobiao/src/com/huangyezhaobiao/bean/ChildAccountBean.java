package com.huangyezhaobiao.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 58 on 2016/7/22.
 */
public class ChildAccountBean implements Serializable{

    private data data;

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public  class data implements Serializable{

        private ArrayList<bean> list;

        public ArrayList<bean> getList() {
            return list;
        }

        public void setList(ArrayList<bean> list) {
            this.list = list;
        }

        public class bean implements Serializable{
            private String id;
            private String username;
            private String phone;
            private String rbac;

//            private String loginToken;
//            private String createTime;
//            private String updateTime;
//            private String ident;
//            private String upVersion;
//            private String suserId;
//            private String puserid;
//            private String valid;
//            private String validTime;
//            private String unvalidTime;
//            private String appToken;
//            private String loginTime;
//            private String onlineState;
//            private String activityTime;
//            private String resv1;
//            private String resv2;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getRbac() {
                return rbac;
            }

            public void setRbac(String rbac) {
                this.rbac = rbac;
            }
//
//            public String getLoginToken() {
//                return loginToken;
//            }
//
//            public void setLoginToken(String loginToken) {
//                this.loginToken = loginToken;
//            }
//
//            public String getCreateTime() {
//                return createTime;
//            }
//
//            public void setCreateTime(String createTime) {
//                this.createTime = createTime;
//            }
//
//            public String getUpdateTime() {
//                return updateTime;
//            }
//
//            public void setUpdateTime(String updateTime) {
//                this.updateTime = updateTime;
//            }
//
//            public String getIdent() {
//                return ident;
//            }
//
//            public void setIdent(String ident) {
//                this.ident = ident;
//            }
//
//            public String getUpVersion() {
//                return upVersion;
//            }
//
//            public void setUpVersion(String upVersion) {
//                this.upVersion = upVersion;
//            }
//
//            public String getSuserId() {
//                return suserId;
//            }
//
//            public void setSuserId(String suserId) {
//                this.suserId = suserId;
//            }
//
//            public String getPuserid() {
//                return puserid;
//            }
//
//            public void setPuserid(String puserid) {
//                this.puserid = puserid;
//            }
//
//            public String getValid() {
//                return valid;
//            }
//
//            public void setValid(String valid) {
//                this.valid = valid;
//            }
//
//            public String getValidTime() {
//                return validTime;
//            }
//
//            public void setValidTime(String validTime) {
//                this.validTime = validTime;
//            }
//
//            public String getUnvalidTime() {
//                return unvalidTime;
//            }
//
//            public void setUnvalidTime(String unvalidTime) {
//                this.unvalidTime = unvalidTime;
//            }
//
//            public String getAppToken() {
//                return appToken;
//            }
//
//            public void setAppToken(String appToken) {
//                this.appToken = appToken;
//            }
//
//            public String getLoginTime() {
//                return loginTime;
//            }
//
//            public void setLoginTime(String loginTime) {
//                this.loginTime = loginTime;
//            }
//
//            public String getOnlineState() {
//                return onlineState;
//            }
//
//            public void setOnlineState(String onlineState) {
//                this.onlineState = onlineState;
//            }
//
//            public String getActivityTime() {
//                return activityTime;
//            }
//
//            public void setActivityTime(String activityTime) {
//                this.activityTime = activityTime;
//            }
//
//            public String getResv1() {
//                return resv1;
//            }
//
//            public void setResv1(String resv1) {
//                this.resv1 = resv1;
//            }
//
//            public String getResv2() {
//                return resv2;
//            }
//
//            public void setResv2(String resv2) {
//                this.resv2 = resv2;
//            }
        }
    }


}
