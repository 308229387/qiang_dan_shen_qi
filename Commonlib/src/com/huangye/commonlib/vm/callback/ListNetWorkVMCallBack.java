package com.huangye.commonlib.vm.callback;



/**
 * listView的vm层callBack---vm层和activity的结合
 * @author 58
 *
 */
public interface ListNetWorkVMCallBack extends NetWorkVMCallBack{

	
	
	public void onRefreshSuccess(Object t);
	
	public void onLoadingMoreSuccess(Object res);
	
	/**
	 * 还可以加载更多
	 */
	public void canLoadMore();
	
	
	/**
	 * 不能加载更多了
	 */
	public void loadMoreEnd();
	
	
}
