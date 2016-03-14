package com.huangyezhaobiao.bean.popdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;

public class TimeFeeBean extends QDDetailBaseBean{
	private String publishTime;
	private String name;
	private String orderFee;
	
	
	public TimeFeeBean(){
		
	}
	
	
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderFee() {
		return orderFee;
	}
	public void setOrderFee(String orderFee) {
		this.orderFee = orderFee;
	}
	@Override
	public View initView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.detail_item_timefee, null);
		((TextView)view.findViewById(R.id.detail_item_timefee_text1)).setText(publishTime);
		((TextView)view.findViewById(R.id.detail_item_timefee_text2)).setText(orderFee);
		return view;
	}

}
