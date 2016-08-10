package wuba.zhaobiao.respons;

import java.io.Serializable;

/**
 * Created by 58 on 2016/8/9.
 */
public class GetCodeRespons implements Serializable{
    private data data;

    public GetCodeRespons.data getData() {
        return data;
    }

    public void setData(GetCodeRespons.data data) {
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
