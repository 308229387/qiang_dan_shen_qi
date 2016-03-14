package com.huangyezhaobiao.inter;
/**
 * 网络回调的接口
 * @author 58
 *
 */
public interface INetworkCallBack {
	/**
	 * 开始加载时
	 */
	void onStartWork();

	
	/**
	 * 失败时
	 * @param errMessage
	 */
	void onFailure(String errMessage);

	
	/**
	 * 成功时
	 * @param jsonResult
	 */
	void onSuccess(String jsonResult);

	/**
	 * 取消时
	 */
	void onCancel();

}
