package wuba.zhaobiao.respons;

import java.io.Serializable;
import java.util.ArrayList;

import wuba.zhaobiao.bean.BusinessData;

/**
 * Created by SongYongmeng on 2016/9/6.
 */
public class BusinessOpportunityRespons implements Serializable {
    public String timestamp;

    public ArrayList<BusinessData> data;

    public ArrayList<BusinessData> getRespData() {
        return data;
    }

    public void setRespData(ArrayList<BusinessData> respData) {
        this.data = respData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
