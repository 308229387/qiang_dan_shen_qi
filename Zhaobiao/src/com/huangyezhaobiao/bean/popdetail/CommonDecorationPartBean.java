package com.huangyezhaobiao.bean.popdetail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 * Created by 58 on 2015/8/25.
 * 商机详情的局部装修Bean
 * "name":"基本信息",
 * "decoraType":"住宅装修-二手房",
 * “reconsProject”:”改造类型”,
 * "space":"56平米",
 * "budget":"预算3-5万",
 * "type":"半包",
 * "measureTime":"2015年6月1日",
 * "decorateTime":"2015年6月",
 * "location":"朝阳区北苑"
 */
public class CommonDecorationPartBean extends QDDetailBaseBean {
    private String name;
    private String decoraType;
    private String reconsProject;
    private String space;
    private String budget;
    private String type;
    private String measureTime;
    private String decorateTime;
    private String location;

    private String needNear;
  //  private String detailAddress;
    @Override
    public View initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_item_common_decorate_part, null);
        ((TextView) view.findViewById(R.id.detail_item_common_decorate_text_dec_type)).setText(decoraType);
        ((TextView) view.findViewById(R.id.detail_item_common_decorate_text_space)).setText(space);
        ((TextView) view.findViewById(R.id.detail_item_common_decorate_text_budget)).setText(budget);
        LinearLayout ll_part_distance = ((LinearLayout) view.findViewById(R.id.ll_part_distance));
        if(TextUtils.isEmpty(needNear)){
            ll_part_distance.setVisibility(View.GONE);
        }else{
            ll_part_distance.setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.detail_item_common_decorate_text_distance)).setText(needNear);
        }
        ((TextView) view.findViewById(R.id.detail_item_common_decorate_text_type)).setText(type);
        ((TextView) view.findViewById(R.id.detail_item_common_decorate_text_measure_time)).setText(measureTime);
        ((TextView) view.findViewById(R.id.detail_item_common_decorate_text_dec_time)).setText(decorateTime);
        ((TextView) view.findViewById(R.id.detail_item_common_decorate_text_location)).setText(location);
        ((TextView) view.findViewById(R.id.detail_item_common_decorate_recons)).setText(reconsProject);
       // ((TextView)view.findViewById(R.id.detail_item_common_decorate_text_details)).setText(detailAddress);
        return view;
    }


    /*public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecoraType() {
        return decoraType;
    }

    public void setDecoraType(String decoraType) {
        this.decoraType = decoraType;
    }

    public String getReconsProject() {
        return reconsProject;
    }

    public void setReconsProject(String reconsProject) {
        this.reconsProject = reconsProject;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }

    public String getDecorateTime() {
        return decorateTime;
    }

    public void setDecorateTime(String decorateTime) {
        this.decorateTime = decorateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNeedNear() {
        return needNear;
    }

    public void setNeedNear(String needNear) {
        this.needNear = needNear;
    }
}
