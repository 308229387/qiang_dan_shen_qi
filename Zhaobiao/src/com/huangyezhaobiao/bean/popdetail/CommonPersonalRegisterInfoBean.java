package com.huangyezhaobiao.bean.popdetail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;

public class CommonPersonalRegisterInfoBean extends QDDetailBaseBean {

	private String title;// :"基本信息",
	private String registerType;// :"个体工商注册",
	private String registerTime;// :"2015年8月1日",
	private String registerLocation;// ":"朝阳区北苑",
	//2015.10.21 add
	private String business;//业务类型
	//2015.10.21 add end
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getRegisterLocation() {
		return registerLocation;
	}

	public void setRegisterLocation(String registerLocation) {
		this.registerLocation = registerLocation;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public CommonPersonalRegisterInfoBean() {

	}

	private int getLayoutId(){
		return R.layout.detail_item_common_personal;
	}

	@Override
	public View initView(Context context) {
		View view = LayoutInflater.from(context).inflate(getLayoutId(), null);
		((TextView)view.findViewById(R.id.detail_item_common_personal_text_reg_type)).setText(registerType);
		((TextView)view.findViewById(R.id.detail_item_common_personal_text_reg_location)).setText(registerLocation);
		((TextView)view.findViewById(R.id.detail_item_common_personal_text_reg_time)).setText(registerTime);
		if(!TextUtils.isEmpty(business)) {
			((TextView) view.findViewById(R.id.detail_item_common_personal_text_business)).setText(business);
		}
		if(TextUtils.isEmpty(registerLocation)){
			((TextView)view.findViewById(R.id.detail_item_common_personal_text_reg_location)).setText("没有注册区域");
		}
		return view;
	}

}
