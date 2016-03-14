package com.huangyezhaobiao.bean;

import android.app.Application;

public class AppBean {
	private Application app;
	private static AppBean bean = new AppBean();
	public static AppBean getAppBean(){
		return bean;
	}  
	
	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}
	
}
