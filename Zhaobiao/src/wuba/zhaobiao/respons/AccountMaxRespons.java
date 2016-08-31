package wuba.zhaobiao.respons;

import java.io.Serializable;

/**
 * Created by 58 on 2016/8/5.
 */
public class AccountMaxRespons implements Serializable{
    private data data ;

    public AccountMaxRespons.data getData() {
        return data;
    }

    public void setData(AccountMaxRespons.data data) {
        this.data = data;
    }

    public class data implements Serializable{
        private String accountMaxSize;

        public String getAccountMaxSize() {
            return accountMaxSize;
        }

        public void setAccountMaxSize(String accountMaxSize) {
            this.accountMaxSize = accountMaxSize;
        }
    }
}
