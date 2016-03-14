package com.huangyezhaobiao.bean.push.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.utils.LogUtils;

/**
 * Created by 58 on 2015/8/17.
 * <p/>
 * <p/>
 * "title":"招商加盟",
 * "fee":"300",
 * <p/>
 * "consultCategory":"招商加盟-餐饮加盟-火锅",
 * "budget":"20万",
 * "investKeywords":"有品牌，回本快", 	//投资意向关键词
 * "investIndusty":"餐饮加盟、服装鞋包",
 */
public class AffiliatePopBean extends PopBaseBean {
    private String orderId;
    private int status;// 抢单结果通知，1代表失败，0代表成功
    private int cateId;//":"4065",
    private int displayType;//":"4"
    private long bidId;//":"12312321",
    private String time;
    private String title;
    private String fee;
    private String consultCategory;
    private String budget;
    private String investKeywords;
    private String investIndusty;
    private String voice;
    private String originalFee;

    @Override
    public String getOriginalFee() {
        return originalFee;
    }

    public void setOriginalFee(String originalFee) {
        this.originalFee = originalFee;
    }

    @Override
    public View initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pop_affiliate, null);
        ((TextView) view.findViewById(R.id.budget)).setText(budget);
        ((TextView) view.findViewById(R.id.consultCategory)).setText(consultCategory);
        ((TextView) view.findViewById(R.id.investKeywords)).setText(investKeywords);
        ((TextView) view.findViewById(R.id.investIndusty)).setText(investIndusty);
        return view;
    }

    @Override
    public int getCateId() {
        return cateId;
    }

    @Override
    public String getVoice() {
        return voice;
    }

    @Override
    public String getFee() {
        return fee;
    }

    //存储到数据库需要
    @Override
    public PushToStorageBean toPushStorageBean() {
        PushToStorageBean bean = new PushToStorageBean();
        try {
            bean.setOrderid(Long.parseLong(orderId));
        } catch (Exception e) {

        }
        bean.setTag(100);
        bean.setTime(time);
        LogUtils.LogE("asheasasn", "budget:" + budget + ",title:" + title + ",category:" + consultCategory + ",fee:" + fee + "status:" + status);
        // 拼接消息字符串
        StringBuilder str = new StringBuilder();
        str.append(title + " ").append(consultCategory + " ").append(budget + "");
        if (status == 1) {
            bean.setFee(fee);
        }
        bean.setStr(str.toString());
        bean.setStatus(status);
        return bean;
    }



    @Override
    public PushToPassBean toPushPassBean() {
        PushToPassBean bean = new PushToPassBean();
        bean.setBidId(bidId);
        bean.setPushId(pushId);
        bean.setPushTurn(pushTurn);
        bean.setCateId(cateId);
        return bean;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public long getBidId() {
        return bidId;
    }

    public void setBidId(long bidId) {
        this.bidId = bidId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getConsultCategory() {
        return consultCategory;
    }

    public void setConsultCategory(String consultCategory) {
        this.consultCategory = consultCategory;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getInvestKeywords() {
        return investKeywords;
    }

    public void setInvestKeywords(String investKeywords) {
        this.investKeywords = investKeywords;
    }

    public String getInvestIndusty() {
        return investIndusty;
    }

    public void setInvestIndusty(String investIndusty) {
        this.investIndusty = investIndusty;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
