package com.huangye.commonlib.delegate;

public interface StorageCallBack {
	/**
	 * 读取数据成功
	 *
	 *
	 *
	 */
	public  void getDataSuccess(Object o);
	
	/**
	 * 读取数据失败
	 *
	 */
	public void getDataFailure();
	
	public void insertDataSuccess();
	public void insertDataFailure();
	public void deleteDataSuccess();
	public void deleteDataFailure();
	

}
