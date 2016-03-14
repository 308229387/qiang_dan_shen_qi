package com.huangyezhaobiao.bean.popdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 *  Created by shenzhixin on 2015/12/7.
 *  保洁清洗的标地详情业务bean
 *  "name":"基本信息",
    "serviceType":"保洁清洗-家庭保洁",
    "cleanSpace":"50平米以下",      //清洁面积
    "needTools":"用户自备洁具",   //是否自备洁具
    "location":"北京-朝阳-大山子", //区域
    "serveTime":"2015-11-11",	//服务时间
    "budget":"预算3-5万",
 */
public class CleaningBidDetailBean extends QDDetailBaseBean{
    private String name;
    private String serviceType;
    private String cleanSpace;
    private String needTools;
    private String location;
    private String serveTime;
    private String budget;
    private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCleanSpace() {
        return cleanSpace;
    }

    public void setCleanSpace(String cleanSpace) {
        this.cleanSpace = cleanSpace;
    }

    public String getNeedTools() {
        return needTools;
    }

    public void setNeedTools(String needTools) {
        this.needTools = needTools;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServeTime() {
        return serveTime;
    }

    public void setServeTime(String serveTime) {
        this.serveTime = serveTime;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    @Override
    public View initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.cleaning_bid_detail_layout,null);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_service_type_content)).setText(serviceType);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_size_content)).setText(cleanSpace);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_service_location_content)).setText(location);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_service_time)).setText(serveTime);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_cleaning_tools)).setText(needTools);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_budget)).setText(budget);
        ((TextView)view.findViewById(R.id.cleaning_bid_detail_age)).setText(age);
        return view;
    }
}
