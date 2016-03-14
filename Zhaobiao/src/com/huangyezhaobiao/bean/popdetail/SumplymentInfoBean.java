package com.huangyezhaobiao.bean.popdetail;

import com.huangyezhaobiao.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SumplymentInfoBean extends QDDetailBaseBean{

	private String name;
	private String special;
	private String note;
	
	public SumplymentInfoBean(){
		
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public View initView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.detail_item_sumplyment, null);
		((TextView)view.findViewById(R.id.detail_item_sumplyment_text1)).setText(special);
		return view;
	}

}
