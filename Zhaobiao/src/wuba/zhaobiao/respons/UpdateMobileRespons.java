
package wuba.zhaobiao.respons;

import java.io.Serializable;

/**
 * Created by 58 on 2016/8/10.
 */
public class UpdateMobileRespons implements Serializable{
    private data data;

    public UpdateMobileRespons.data getData() {
        return data;
    }

    public void setData(UpdateMobileRespons.data data) {
        this.data = data;
    }

    public class data implements Serializable{
        private String msg;
        private String status;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
