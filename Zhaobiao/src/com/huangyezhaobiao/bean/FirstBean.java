package com.huangyezhaobiao.bean;
/**
 * 第一排的东西
 * @author shenzhixin
 *
 */
public class FirstBean {
	public static int YUE = 0;
	public static int KOUFEI = YUE + 1;
	public static int DAOJISHI = KOUFEI + 1;
	public static int SYS_NOTI = DAOJISHI + 1;
	
	
	
	private String time;
	private String content;
	private int category;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public FirstBean(String time, String content, int category) {
		super();
		this.time = time;
		this.content = content;
		this.category = category;
	}
	
	
	
	
}
