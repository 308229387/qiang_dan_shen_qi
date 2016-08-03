package wuba.zhaobiao.respons;

import java.io.Serializable;

/**
 * Created by SongYongmeng on 2016/7/29.
 */
public class LoginRespons implements Serializable {
    private data data;

    public LoginRespons.data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public class data implements Serializable{
        private String companyName;
        private String hasValidated;
        private String isSon;
        private String rbac;
        private String suserId;
        private String userId;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getHasValidated() {
            return hasValidated;
        }

        public void setHasValidated(String hasValidated) {
            this.hasValidated = hasValidated;
        }

        public String getIsSon() {
            return isSon;
        }

        public void setIsSon(String isSon) {
            this.isSon = isSon;
        }

        public String getRbac() {
            return rbac;
        }

        public void setRbac(String rbac) {
            this.rbac = rbac;
        }

        public String getSuserId() {
            return suserId;
        }

        public void setSuserId(String suserId) {
            this.suserId = suserId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


    }
}
