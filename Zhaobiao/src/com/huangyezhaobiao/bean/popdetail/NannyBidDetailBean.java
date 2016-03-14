package com.huangyezhaobiao.bean.popdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 * Created by shenzhixin on 2015/12/7.
 * 保姆月嫂详情页的业务bean
 * "name":"基本信息",
    "serviceType":"保姆月嫂-保姆（住家）",
    "employTime":"1个月",      //雇佣时间
    "age":"30-35岁",			  //年龄限制
    "experience":"不限经验", 	  //经验年限
    "budget":"预算3-5万",
    "location":"北京-朝阳-大山子", //区域
    "startTime":"2015-11-11",	//开始时间
 */
public class NannyBidDetailBean extends QDDetailBaseBean{
    private String name;
    private String serviceType;
    private String employTime;
    private String age;
    private String experience;
    private String budget;
    private String location;
    private String startTime;

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

    public String getEmployTime() {
        return employTime;
    }

    public void setEmployTime(String employTime) {
        this.employTime = employTime;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public View initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_nanny_bid_detail,null);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_service_type_content)).setText(serviceType);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_hire_time_content)).setText(employTime);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_service_location_content)).setText(location);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_age_content)).setText(age);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_experience_content)).setText(experience);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_budget)).setText(budget);
        ((TextView)rootView.findViewById(R.id.nanny_bid_detail_start_time)).setText(startTime);

        return rootView;
    }
}
