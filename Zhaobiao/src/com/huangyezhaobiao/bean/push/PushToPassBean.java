package com.huangyezhaobiao.bean.push;

import java.io.Serializable;

/**
 * PushBean的变种2
 * 用于activity信息的传递,日志的记录
 * 
 * @author linyueyang
 *
 */
public class PushToPassBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int pushTurn; // 推送轮次
	private long pushId; // 推送Id
	private long bidId; // 标的id
	private int cateId; // 类别id
	//private int bidState; // 标的状态,是否可抢 针对抢单列表的状态

	public int getPushTurn() {
		return pushTurn;
	}

	public void setPushTurn(int pushTurn) {
		this.pushTurn = pushTurn;
	}

	public long getPushId() {
		return pushId;
	}

	public void setPushId(long pushId) {
		this.pushId = pushId;
	}

	public long getBidId() {
		return bidId;
	}

	public void setBidId(long bidId) {
		this.bidId = bidId;
	}

	public int getCateId() {
		return cateId;
	}

	public void setCateId(int cateId) {
		this.cateId = cateId;
	}

//	public int getBidState() {
//		return bidState;
//	}
//
//	public void setBidState(int bidState) {
//		this.bidState = bidState;
//	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
