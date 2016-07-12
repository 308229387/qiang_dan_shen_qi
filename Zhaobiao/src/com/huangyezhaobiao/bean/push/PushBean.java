package com.huangyezhaobiao.bean.push;

import java.io.Serializable;

/**
 * 所有推送消息基类
 * 
 * @author linyueyang
 *
 */
public abstract class PushBean implements Serializable {

	//所有推送都有的一些属性
	public int tag; // 推送类型
	public String pushTime; //推送时间
	public int pushTurn; //推送轮次
	public long pushId; //推送Id


	/**
	 * 不同的推送类别，根据不同情况拼接PushToStorageBean
	 * 
	 * @return PushToStorageBean
	 */
	public abstract PushToStorageBean toPushStorageBean();

	
	
	/**
	 * 不同的推送类别，根据不同情况拼接PushToPassBean
	 * 
	 * @return PushToPassBean
	 */
	public abstract PushToPassBean toPushPassBean();
	
	
	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getPushTime() {
		return pushTime;
	}

	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}

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

	

//	public String getMessage() {
//		return toPushStorageBean().getStr();
//	}
//
//	public int getStatus() {
//		return toPushStorageBean().getStatus();
//	}
//
//	public void setStatus(int status){
//		toPushStorageBean().setStatus(status);
//	}


	@Override
	public String toString() {
		return "PushBean{" +
				"tag=" + tag +
				", pushTime='" + pushTime + '\'' +
				", pushTurn=" + pushTurn +
				", pushId=" + pushId +
				'}';
	}
}
