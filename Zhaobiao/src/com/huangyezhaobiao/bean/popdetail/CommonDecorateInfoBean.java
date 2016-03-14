package com.huangyezhaobiao.bean.popdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 * 家庭装修的商机详情页bean
 * <p>
 * "name":"基本信息",
 * "decoraType":"住宅装修-二手房",
 * "space":"56平米",
 * "budget":"预算3-5万",
 * "type":"半包",
 * "measureTime":"2015年6月1日",
 * "decorateTime":"2015年6月",
 * "location":"朝阳区北苑"
 */
public class CommonDecorateInfoBean extends QDDetailBaseBean {

	private String name;// :"基本信息",
	private String decoraType;//装修类型
	private String space;// ":"56平米",
	private String budget;// ":"预算3-5万",
	private String type;// ":"半包",
	private String measureTime;// ":"2015年6月1日",
	private String decorateTime;// ":"2015年6月",
	private String location;// ":"朝阳区北苑"

	//详细地址 2015.10.21 add
//	private String detailAddress;
	//2015.10.21 add end
	public CommonDecorateInfoBean() {

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

	@Override
	public View initView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.detail_item_common_decorate, null);
		((TextView) view.findViewById(R.id.detail_item_common_decorate_text_type)).setText(decoraType);
		((TextView) view.findViewById(R.id.detail_item_common_decorate_text_space)).setText(space);
		((TextView) view.findViewById(R.id.detail_item_common_decorate_text_budget)).setText(budget);
		((TextView) view.findViewById(R.id.detail_item_common_decorate_text_dec_type)).setText(type);
		((TextView) view.findViewById(R.id.detail_item_common_decorate_measure_time)).setText(measureTime);
		((TextView) view.findViewById(R.id.detail_item_common_decorate_text_dec_time)).setText(decorateTime);
		((TextView) view.findViewById(R.id.detail_item_common_decorate_text_loaction)).setText(location);
		//((TextView) view.findViewById(R.id.detail_item_common_decorate_text_detail_address)).setText(detailAddress);
		return view;
	}

}
