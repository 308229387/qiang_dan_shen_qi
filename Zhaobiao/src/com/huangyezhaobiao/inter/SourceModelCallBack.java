package com.huangyezhaobiao.inter;

public interface SourceModelCallBack {
	public void onLoadSuccess(String result);
	
	public void onLoadError(String msg);
	
	public void onLoadStart();
}
