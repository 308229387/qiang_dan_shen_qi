package com.huangye.commonlib.vm.callback;
/**
 * model 层专用callback 给ViewModel层提供接口
 */
public interface StorageVMCallBack {

	/**
	 * 读取数据成功
	 * @param exception
	 * @param err
	 */
	public  void getDataSuccess(Object o);
	

	/**
	 * 读取数据失败
	 * @param jsonResult
	 */
	public void getDataFailure();
	public void insertDataSuccess();
	public void insertDataFailure();
	public void deleteDataSuccess();
	public void deleteDataFailure();


}
