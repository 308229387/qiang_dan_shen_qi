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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


    }
}
