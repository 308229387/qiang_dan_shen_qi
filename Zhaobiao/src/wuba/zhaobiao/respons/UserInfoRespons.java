package wuba.zhaobiao.respons;

import java.io.Serializable;

/**
 * Created by 58 on 2016/8/4.
 */
public class UserInfoRespons implements Serializable{
    private data data;

    public UserInfoRespons.data getData() {
        return data;
    }

    public void setData(UserInfoRespons.data data) {
        this.data = data;
    }

    public class data implements Serializable{
        private String balance;
        private String companyName;
        private String phone;
        private String userName;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
