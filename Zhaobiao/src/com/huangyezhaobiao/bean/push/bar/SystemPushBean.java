package com.huangyezhaobiao.bean.push.bar;

import android.util.Log;

import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;

public class SystemPushBean extends PushBean {

	private String type;// ":"1",//1代表纯文本内容，2代表超链接 ，以后可能会有更多类型，再商议
	private String value;// ":""//超链接的地址或者纯文本内容
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public PushToStorageBean toPushStorageBean() {

		PushToStorageBean bean = new PushToStorageBean();

		bean.setTag(tag);
		bean.setStr(value);
		bean.setTime(pushTime);
		Log.e("ashenUI","value:"+value);
		return bean;
	}

	@Override
	public PushToPassBean toPushPassBean() {
		PushToPassBean bean = new PushToPassBean();
		bean.setBidId(0);
		bean.setPushId(pushId);
		bean.setPushTurn(pushTurn);
		bean.setCateId(0);
		return bean;
	}

}
