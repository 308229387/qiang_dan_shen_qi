package com.huangye.commonlib.model.callback;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.NetBean;

/**
 * 这个是model层和viewModel层或者controller层(不推荐)的接口
 * @author shenzhixin
 *
 * @param <T>
 */
public interface NetworkModelCallBack {
	/**
	 * 取消加载
	 */
	public void onLoadingCancell();

	/**
	 * 开始加载
	 */
	public void onLoadingStart();

	/**
	 * 加载失败
	 * @param
	 * @param err
	 */
	public void onLoadingFailure(String err);

	
	/**
	 * 加载成功
	 * @param
	 */
	public  void onLoadingSuccess(NetBean bean, NetWorkModel model);

	/**
	 * 没有网络连接
	 */
	public void noInternetConnect();


	/**
	 * 登录异常
	 */
	public void onModelLoginInvalidate();
}
