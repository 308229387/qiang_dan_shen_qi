package wuba.zhaobiao.respons;

import java.io.Serializable;
import java.util.ArrayList;

import wuba.zhaobiao.bean.BusinessData;

/**
 * Created by SongYongmeng on 2016/9/6.
 */
public class BusinessOpportunityRespons implements Serializable {
    public ArrayList<BusinessData> data;

    public ArrayList<BusinessData> getRespData() {
        return data;
    }

    public void setRespData(ArrayList<BusinessData> respData) {
        this.data = respData;
    }
}
