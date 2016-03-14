package com.huangyezhaobiao.bean.push.bar;

import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;

public class CountdownPushBean extends PushBean {
	private String orderId;
	private String deadLine;// ": "2015-05-15",
	private int cateId;// ": "4063",
	private int displayType;// ": "1",
	private long bidId;// ": "12312321",

	@Override
	public PushToStorageBean toPushStorageBean() {
		PushToStorageBean bean = new PushToStorageBean();
		try {
			bean.setOrderid(Long.parseLong(orderId));
			bean.setTag(tag);
			bean.setStr("您有一条新订单，即将于三小时内到期");
			bean.setTime(pushTime);
		}catch(RuntimeException e){

		}
		return bean;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public int getCateId() {
		return cateId;
	}

	public void setCateId(int cateId) {
		this.cateId = cateId;
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
	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public PushToPassBean toPushPassBean() {
		PushToPassBean bean = new PushToPassBean();
		bean.setBidId(bidId);
		bean.setPushId(pushId);
		bean.setPushTurn(pushTurn);
		bean.setCateId(cateId);
		return bean;
	}

}
