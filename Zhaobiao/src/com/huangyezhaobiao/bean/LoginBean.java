package com.huangyezhaobiao.bean;

public class LoginBean {

	private long userId;//":"7348394343",
	private String companyName;//":"北京崛起装饰公司"
	private int hasValidated;//":1,//是否验证过手机，1：验证过，0：没验证
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getHasValidated() {
		return hasValidated;
	}
	public void setHasValidated(int hasValidated) {
		this.hasValidated = hasValidated;
	}
	

}
