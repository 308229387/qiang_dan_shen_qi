package wuba.zhaobiao.respons;

import java.io.Serializable;

/**
 * Created by SongYongmeng on 2016/9/13.
 */
public class BusinessSettlementRespons implements Serializable {
    private String totalFee;
    private String totalCount;
    private String succCount;
    private String state;
    private String falseCount;

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getSuccCount() {
        return succCount;
    }

    public void setSuccCount(String succCount) {
        this.succCount = succCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFalseCount() {
        return falseCount;
    }

    public void setFalseCount(String falseCount) {
        this.falseCount = falseCount;
    }
}
