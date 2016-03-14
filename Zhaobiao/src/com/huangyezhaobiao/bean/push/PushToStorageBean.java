package com.huangyezhaobiao.bean.push;

import com.huangyezhaobiao.utils.UserUtils;

/**
 * PushBean的变种1 用于推送消息的存储和展示
 * 
 * @author linyueyang
 *
 *         8.4 加入uid
 */
public class PushToStorageBean {

	private int id;
	private int tag;
	private long orderid;
	private String time;
	private int status;

	private String str;
	private String fee;

	private long userId;


	public PushToStorageBean() {
		try {
			setUserId(Long.parseLong(UserUtils.userId));
		} catch (Exception e) {

		}

	}

	public PushToStorageBean(String messge, String sd) {
		this.str = messge;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public long getOrderid() {
		return orderid;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}



}
