package wuba.zhaobiao.respons;

import java.io.Serializable;

/**
 * Created by 58 on 2016/9/22.
 */
public class WakeUpRespons implements Serializable {

    private data data;

    public WakeUpRespons.data getData() {
        return data;
    }

    public void setData(WakeUpRespons.data data) {
        this.data = data;
    }

    public class data implements Serializable{
        private String status;
        private String msg;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
