package com.huangyezhaobiao.bean;


/**
 * 消息界面，其他三页面的javaBean
 * @author shenzhixin
 *
 */
public class OtherBean {
	private int id;
	private String time;
	private String title;
	private String type;
	public OtherBean(int id, String time, String title,String type) {
		super();
		this.id = id;
		this.time = time;
		this.title = title;
		this.type  = type;
	}
	
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
