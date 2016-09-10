package wuba.zhaobiao.respons;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 58 on 2016/9/7.
 */
public class OrderListRespons implements Serializable {

    private ArrayList<bean> data;
    private Page other;

    public ArrayList<bean> getData() {
        return data;
    }

    public void setData(ArrayList<bean> data) {
        this.data = data;
    }

    public Page getOther() {
        return other;
    }

    public void setOther(Page other) {
        this.other = other;
    }

    public class bean implements Serializable{
        private String id;
        private String key1;
        private String key2;
        private String key3;
        private String key4;
        private String key5;
        private String key6;
        private String key7;
        private String key8;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKey1() {
            return key1;
        }

        public void setKey1(String key1) {
            this.key1 = key1;
        }

        public String getKey2() {
            return key2;
        }

        public void setKey2(String key2) {
            this.key2 = key2;
        }

        public String getKey3() {
            return key3;
        }

        public void setKey3(String key3) {
            this.key3 = key3;
        }

        public String getKey4() {
            return key4;
        }

        public void setKey4(String key4) {
            this.key4 = key4;
        }

        public String getKey5() {
            return key5;
        }

        public void setKey5(String key5) {
            this.key5 = key5;
        }

        public String getKey6() {
            return key6;
        }

        public void setKey6(String key6) {
            this.key6 = key6;
        }

        public String getKey7() {
            return key7;
        }

        public void setKey7(String key7) {
            this.key7 = key7;
        }

        public String getKey8() {
            return key8;
        }

        public void setKey8(String key8) {
            this.key8 = key8;
        }
    }

    public class Page implements Serializable{

        private String pageCount;

        public String getPageCount() {
            return pageCount;
        }

        public void setPageCount(String pageCount) {
            this.pageCount = pageCount;
        }
    }

}
