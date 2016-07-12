package com.huangyezhaobiao.bean.push.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;

public class PersonalRegisterPopBean extends PopBaseBean {
	private String orderId;
	private int status;// 抢单结果通知，1代表成功，0代表失败

	private int cateId;//":"4065",
	private int displayType;//":"2"
	private long bidId;//":"12312321",	
	private String title;//:"工商注册-香港/海外",
	private String registerType;//":"香港/海外工商注册",
	private String bidState;//":"0",
	private String location;//":"朝阳区北苑",
	private String time;//":"2015-05-15 18:49",
	private String registerTime;//":"2015年6月"
	private String fee;//":"2015年6月"
	private String voice;

	//2015.10.22 add
	private String business;//业务种类
	//2015.10.22 add end
	private String originalFee;

	private String guestName;

	@Override
	public String getOriginalFee() {
		return originalFee;
	}

	public void setOriginalFee(String originalFee) {
		this.originalFee = originalFee;
	}

	@Override
	public View initView(Context context) {
		if(location!=null && location.length()>20){
			location = location.substring(0,19)+"...";
		}
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_pop_personalregister, null);
		// ((TextView)view.findViewById(R.id.personalregister_pop_title)).setText(title);
		((TextView) view.findViewById(R.id.personalregister_pop_registertime)).setText(registerTime);
		((TextView) view.findViewById(R.id.personalregister_pop_registertype)).setText(registerType);
		((TextView) view.findViewById(R.id.personalregister_pop_location)).setText(location);
		((TextView) view.findViewById(R.id.personalregister_pop_business)).setText(business);
		return view;
	}

	@Override
	public PushToStorageBean toPushStorageBean() {

		PushToStorageBean bean = new PushToStorageBean();
		try {
			bean.setOrderid(Long.parseLong(orderId));
		} catch (Exception e) {

		}
		bean.setTag(100);
		bean.setTime(time);

		// 拼接消息字符串
		StringBuilder str = new StringBuilder();
//		str.append(title + " ").append(time + "");
		if(location.contains("-")){
			String[] locations = location.split("-");
			if(locations.length == 3){
				str.append(locations[0]+"").append("-").append(locations[1]+"").append("-").append(title + "");
			}else{
				str.append(location+"").append("-").append(title + "");
			}
		}
		if (status == 1) {
			bean.setFee(fee);
		}

		bean.setStr(str.toString());
		bean.setStatus(status);
        bean.setGuestName(guestName);
		return bean;
	}

	@Override
	public PushToPassBean toPushPassBean(){
		PushToPassBean bean = new PushToPassBean();
		bean.setBidId(bidId);
		bean.setPushId(pushId);
		bean.setPushTurn(pushTurn);
		bean.setCateId(cateId);
		return bean;
	}
	
	@Override
	public int getCateId() {
		return cateId;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public String getBidState() {
		return bidState;
	}

	public void setBidState(String bidState) {
		this.bidState = bidState;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public void setCateId(int cateId) {
		this.cateId = cateId;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}

	@Override
	public String getVoice() {
		return voice;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
}
