package wuba.zhaobiao.respons;

import java.io.Serializable;

/**
 * Created by SongYongmeng on 2016/7/31.
 */
public class GetWltStateRespons implements Serializable {
    private data data;

    public GetWltStateRespons.data getData() {
        return data;
    }

    public void setData(GetWltStateRespons.data data) {
        this.data = data;
    }

    public class data implements Serializable {
        private userPhoneResult userPhoneResult;
        private wltAlertResult wltAlertResult;

        public GetWltStateRespons.data.userPhoneResult getUserPhoneResult() {
            return userPhoneResult;
        }

        public void setUserPhoneResult(GetWltStateRespons.data.userPhoneResult userPhoneResult) {
            this.userPhoneResult = userPhoneResult;
        }

        public GetWltStateRespons.data.wltAlertResult getWltAlertResult() {
            return wltAlertResult;
        }

        public void setWltAlertResult(GetWltStateRespons.data.wltAlertResult wltAlertResult) {
            this.wltAlertResult = wltAlertResult;
        }

        public class userPhoneResult implements Serializable {
            private String status;
            private String userPhone;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUserPhone() {
                return userPhone;
            }

            public void setUserPhone(String userPhone) {
                this.userPhone = userPhone;
            }
        }

        public class wltAlertResult implements Serializable {
            private String expireState;
            private String msg;

            public String getExpireState() {
                return expireState;
            }

            public void setExpireState(String expireState) {
                this.expireState = expireState;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }
        }
    }
}
