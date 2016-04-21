package com.huangye.commonlib.delegate;

import com.lidroid.xutils.http.ResponseInfo;

/**
 * 这个是model层和net层的接口
 * @author 58
 *
 */
public interface HttpRequestCallBack {
	/**
	 * 加载失败
	 * 
	 * @param err
	 */
	 void onLoadingFailure(String err);

	
	/**
	 * 加载成功
	 * @param result
	 */
	void onLoadingSuccess(ResponseInfo<String> result);

	/**
	 * 加载开始
	 */
	void onLoadingStart();

	
	/**
	 * 取消请求
	 */
	void onLoadingCancelled();

	
	
	void onLoading(long total, long current);

}
