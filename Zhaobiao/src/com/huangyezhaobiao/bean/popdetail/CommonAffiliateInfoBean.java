package com.huangyezhaobiao.bean.popdetail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 * Created by 58 on 2015/8/17.
 * 招商加盟的详情页面
 */
public class CommonAffiliateInfoBean extends QDDetailBaseBean {
    private String consultCategory;
    private String budget;
    private String investKeywords;
    private String investIndusty;
    private String investCity;
    private String jobIndusty;
    private String jobTitle;
    private String jobExperience;
    private String shopExperience;
    private String name;


    @Override
    public View initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_common_affiliate, null);
        ((TextView) view.findViewById(R.id.consultCategory)).setText(consultCategory);
        ((TextView) view.findViewById(R.id.budget)).setText(budget);
        ((TextView) view.findViewById(R.id.investKeywords)).setText(investKeywords);
        ((TextView) view.findViewById(R.id.investIndusty)).setText(investIndusty);
        ((TextView) view.findViewById(R.id.jobIndusty)).setText(jobIndusty);
        ((TextView) view.findViewById(R.id.jobTitle)).setText(jobTitle);
        ((TextView) view.findViewById(R.id.jobExperience)).setText(jobExperience);
        ((TextView) view.findViewById(R.id.shopExperience)).setText(shopExperience);
        if (!TextUtils.isEmpty(investCity)) {
            if (investCity.startsWith("无")) {
                investCity = "无意向城市";
            }else if(investCity.length()<=3){
                //fix bug invest city length <3.没法解析
            }
            else {
                investCity = investCity.substring(2, investCity.length() - 1);
            }
        }
        ((TextView) view.findViewById(R.id.investCity)).setText(investCity);

        return view;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getInvestCity() {
        return investCity;
    }

    public void setInvestCity(String investCity) {
        this.investCity = investCity;
    }

    public String getJobIndusty() {
        return jobIndusty;
    }

    public void setJobIndusty(String jobIndusty) {
        this.jobIndusty = jobIndusty;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(String jobExperience) {
        this.jobExperience = jobExperience;
    }

    public String getShopExperience() {
        return shopExperience;
    }

    public void setShopExperience(String shopExperience) {
        this.shopExperience = shopExperience;
    }
}
