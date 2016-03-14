package com.huangyezhaobiao.bean.popdetail;

public class BottomViewBean {

	private String originFee;//":"￥300.00",//原价
	private String previlage;//":"￥30.00",//当有活动时传活动价，否则传空字符串
	private String bidState;//":1//订单状态，传值跟现有规则一致
	
	public String getOriginFee() {
		return originFee;
	}
	public void setOriginFee(String originFee) {
		this.originFee = originFee;
	}
	public String getPrevilage() {
		return previlage;
	}
	public void setPrevilage(String previlage) {
		this.previlage = previlage;
	}
	public String getBidState() {
		return bidState;
	}
	public void setBidState(String bidState) {
		this.bidState = bidState;
	}
	
	
	
}
