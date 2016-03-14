package com.huangyezhaobiao.utils;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

/**
 * listView的配置工具，初始化，下拉刷新，上拉刷新等
 * @author shenzhixin
 *
 */
public class PullToRefreshListViewUtils {
	public static void initListView(PullToRefreshListView mPullToRefreshListView){
		if(mPullToRefreshListView!=null){
			/** 初始化抢单列表 */
			mPullToRefreshListView.setMode(Mode.PULL_FROM_END);
			// 设置上拉下拉事件
			mPullToRefreshListView.getLoadingLayoutProxy(true, false)
					.setLastUpdatedLabel("");
			mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(
					"下拉刷新");
			mPullToRefreshListView.getLoadingLayoutProxy(true, false)
					.setRefreshingLabel("正在刷新");
			mPullToRefreshListView.getLoadingLayoutProxy(true, false)
					.setReleaseLabel("放开以刷新");
			// 上拉加载更多时的提示文本设置
			mPullToRefreshListView.getLoadingLayoutProxy(false, true)
					.setLastUpdatedLabel("");
			mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
					"上拉加载");
			mPullToRefreshListView.getLoadingLayoutProxy(false, true)
					.setRefreshingLabel("正在加载...");
			mPullToRefreshListView.getLoadingLayoutProxy(false, true)
					.setReleaseLabel("放开以加载");
		}
	}
	
	/**
	 * listView不能再加载更多了
	 */
	public static void PullToListViewCannotLoadMore(PullToRefreshListView mPullToRefreshListView){
		if(mPullToRefreshListView!=null){
		//	mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
			//mPullToRefreshListView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("没有更多了");
			mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
					"没有更多数据了");
			mPullToRefreshListView.getLoadingLayoutProxy(false, true)
					.setRefreshingLabel("没有更多数据了...");
			mPullToRefreshListView.getLoadingLayoutProxy(false, true)
					.setReleaseLabel("没有更多数据了");
		}
	}
	
	/**
	 * listView都可以加载
	 * @param mPullToRefreshListView
	 */
	public static void PullToListViewCanLoadMore(PullToRefreshListView mPullToRefreshListView){
		initListView(mPullToRefreshListView);
	}
	
	
}
