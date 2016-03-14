package com.huangyezhaobiao.bean;
/**
 * 消息列表的bean
 * @author shenzhixin
 *
 */
public class MessageBean {
	private int id;//为了用xUtils的数据库而设的
	String message;
	String time;
	String type;
	String orderId;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@Override
	public String toString() {
		return "MessageBean [message=" + message + ", time=" + time + ", type="
				+ type + ", orderId=" + orderId + ", getMessage()="
				+ getMessage() + ", getTime()=" + getTime() + ", getType()="
				+ getType() + ", getOrderId()=" + getOrderId()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	public MessageBean(String message, String time) {
		super();
		this.message = message;
		this.time = time;
	}
	
	public MessageBean() {
	}
	
}
