package com.huangyezhaobiao.bean.popdetail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;

/**
 * 可抢订单详情页的内资外资工商注册的bean字段
 */
public class CommonInternationalRegisterInfoBean extends QDDetailBaseBean {
	private String title;// ":"基本信息",
	private String registerType;// :"外资工商注册",
	private String proxyTally;// :"3个月",
	private String isProxyLocation;// :"是",
	private String registerLocation;// :"朝阳区北苑"//如果isProxyLocation的值为“是”，该字段为空
	private String registerTime;// :"2015年6月",
	private String industry;// :"商贸类"

	//2015.10.21 add
	private String business;//业务类型
	//2015.10.21 add end
	public CommonInternationalRegisterInfoBean() {

	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

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

	public String getProxyTally() {
		return proxyTally;
	}

	public void setProxyTally(String proxyTally) {
		this.proxyTally = proxyTally;
	}

	public String getIsProxyLocation() {
		return isProxyLocation;
	}

	public void setIsProxyLocation(String isProxyLocation) {
		this.isProxyLocation = isProxyLocation;
	}

	public String getRegisterLocation() {
		return registerLocation;
	}

	public void setRegisterLocation(String registerLocation) {
		this.registerLocation = registerLocation;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	private int getLayoutId(){
		return R.layout.detail_item_common_international;
	}

	@Override
	public View initView(Context context) {
		View view = LayoutInflater.from(context).inflate(getLayoutId(), null);
		((TextView)view.findViewById(R.id.detail_item_common_international_text_register_type)).setText(registerType);
		((TextView)view.findViewById(R.id.detail_item_common_international_text_delegate_time)).setText(proxyTally);
		((TextView)view.findViewById(R.id.detail_item_common_international_text_delegate_address)).setText(isProxyLocation);
		((TextView)view.findViewById(R.id.detail_item_common_international_text_register_location)).setText(registerLocation);
		((TextView)view.findViewById(R.id.detail_item_common_international_text_register_industry)).setText(industry);
		((TextView)view.findViewById(R.id.detail_item_common_international_text_register_time)).setText(registerTime);
		if(!TextUtils.isEmpty(business)) {
			((TextView) view.findViewById(R.id.detail_item_common_international_text_business)).setText(business);
		}
		if(TextUtils.isEmpty(registerLocation)){
			((TextView)view.findViewById(R.id.detail_item_common_international_text_register_location)).setText("无注册区域");
		}
		return view;
	}

}
