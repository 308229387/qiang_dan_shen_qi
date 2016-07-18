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
	private String guestName;

	@Override
	public PushToStorageBean toPushStorageBean() {
		PushToStorageBean bean = new PushToStorageBean();
		try {
			bean.setOrderid(Long.parseLong(orderId));
			bean.setTag(tag);
			bean.setStr("订单已经到手15分钟啦，别忘了联系客户！越早联系竞争优势越大，成单概率越高哦");
			bean.setTime(pushTime);
			bean.setGuestName(guestName);
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

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
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
