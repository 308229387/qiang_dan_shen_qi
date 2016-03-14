package com.huangyezhaobiao.bean.popdetail;

import com.huangyezhaobiao.bean.push.PushToPassBean;

public class LogBean {

	private int cateID;//":"4063",//cateID :cmcs对应的归属类
	private long bidID;//":321331231231,//bidID:标的id
	private int bidState;//":1//标的可抢状态，1:可抢，0:不可抢，如果这个状态已经有规则就按照现有的规则来
	private long pushId;//":"11112222",
	private int pushTurn;//":"1",
	
	public int getCateID() {
		return cateID;
	}
	public void setCateID(int cateID) {
		this.cateID = cateID;
	}
	public long getBidID() {
		return bidID;
	}
	public void setBidID(long bidID) {
		this.bidID = bidID;
	}
	public int getBidState() {
		return bidState;
	}
	public void setBidState(int bidState) {
		this.bidState = bidState;
	}
	public long getPushId() {
		return pushId;
	}
	public void setPushId(long pushId) {
		this.pushId = pushId;
	}
	public int getPushTurn() {
		return pushTurn;
	}
	public void setPushTurn(int pushTurn) {
		this.pushTurn = pushTurn;
	}
	
	public PushToPassBean toPopPassBean(){
		PushToPassBean bean = new PushToPassBean();
		bean.setBidId(bidID);
		bean.setPushId(pushId);
		bean.setPushTurn(pushTurn);
		bean.setCateId(cateID);
		return bean;
	}
	
	
	
}
