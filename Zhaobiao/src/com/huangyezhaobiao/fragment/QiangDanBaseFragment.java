package com.huangyezhaobiao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.MainActivity;
import com.huangyezhaobiao.activity.OrderListActivity;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.ZBBaseAdapter.AdapterListener;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.view.LoadingProgress;
import com.huangyezhaobiao.vm.QiangDanListViewModel;
import com.huangyezhaobiao.vm.YuEViewModel;


/**
 * 抢单列表的基类fragment 抽一些共通的方法
 * 
 * @author shenzhixin 第一次去取数据，然后后面就是手动的取刷新
 */
public abstract class QiangDanBaseFragment extends Fragment implements
		ListNetWorkVMCallBack, OnItemClickListener, AdapterListener {
	protected QiangDanListViewModel lvm;
	protected boolean isLoadFirst;
	private View emptyView;
	protected View layout_no_internets;
	private LoadingProgress dialog;
	protected Handler handler;
	public static String orderState;
	public static final String WAITING_SERVICE = "1";
	public static final String ON_SERVICE = "2";
	public static final String DONE_SERVICE = "3";
	public static final String DONE_SERVICE_FINISH = "31";
	public static final String DONE_SERVICE_CANCEL = "32";
	protected YuEViewModel yuEViewModel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lvm = new QiangDanListViewModel(this, getActivity());
		yuEViewModel = new YuEViewModel(this,getActivity());

	}

	public void fetchYuE(){
		yuEViewModel.getBalance();
	}

	/**
	 * 加载网络数据
	 */
	protected abstract void loadDatas();

	/**
	 * 给ListView配备动画效果
	 * 
	 * @param lv
	 * @param controller
	 */
	protected void configListView(ListView lv,
			LayoutAnimationController controller) {
		lv.setLayoutAnimation(controller);
	}

	/**
	 * 配置swipeRefreshLayout
	 * @param srl
	 */
	public void configSwipeRefreshLayoutView(final SwipeRefreshLayout srl,final ListView lv){
		if(srl!=null){
			srl.setColorSchemeResources
					(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
			srl.setProgressBackgroundColor(R.color.red);
			srl.setProgressViewEndTarget(true, 150);
			srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					// 加载数据
					if (lv.getFirstVisiblePosition() == 0) {
						loadDatas();
						MDUtils.OrderListPageMD(orderState, "0", "0",
								MDConstans.ACTION_PULL_TO_REFRESH);
					}
				}
			});
			lv.setOnScrollListener(new AbsListView.OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					View firstView = view.getChildAt(firstVisibleItem);
					// 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
					if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
						srl.setEnabled(true);
					} else {
						srl.setEnabled(false);
					}
				}
			});
		}
	}


	/**
	 * 配置下拉刷新，上拉刷新的lv
	 * 
	 * @param mPullToRefreshListView
	 */
	public void configRefreshableListView(
			PullToRefreshListView mPullToRefreshListView) {
		/** 初始化抢单列表 */
		mPullToRefreshListView.setMode(Mode.PULL_UP_TO_REFRESH);
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
		configListViewRefresh(mPullToRefreshListView);
	}



	/**
	 * 配置listView的上拉下拉事件
	 */
	private void configListViewRefresh(PullToRefreshListView lv) {
		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					/*// 下拉刷新
					String label = DateUtils.formatDateTime(getActivity()
							.getApplicationContext(), System
							.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
					// Update the LastUpdatedLabel
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
							label);
					// 加载数据
					loadDatas();
					MDUtils.OrderListPageMD(orderState, "0", "0",
							MDConstans.ACTION_PULL_TO_REFRESH);*/
				} else {
					// 上滑加载---page++
					// 加载数据
					loadMore();
					MDUtils.OrderListPageMD(orderState, "0", "0",
							MDConstans.ACTION_LOAD_MORE_REFRESH);
				}
			}
		});
	}

	/**
	 * 下拉加载
	 */
	protected abstract void loadMore();

	/**
	 * 配置layout的动画
	 * 
	 * @return
	 */
	protected LayoutAnimationController getAnimationController(float fromX,
			float toX) {
		AnimationSet set = new AnimationSet(true);
		//AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		//alphaAnimation.setDuration(600);
		//TranslateAnimation animation = new TranslateAnimation(fromX, toX, 0, 0);
		//animation.setDuration(600);
		//set.addAnimation(alphaAnimation);
		//set.addAnimation(animation);
		//set.setInterpolator(new OvershootInterpolator(1.3f));
		LayoutAnimationController lac = new LayoutAnimationController(set, 0.3f);
		return lac;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && !isLoadFirst) {
			// 设置orderState
			loadDatas();
			isLoadFirst = true;
			LogUtils.LogE("ashenaassaasassaass", "visible");
		} else {
			LogUtils.LogE("ashen", "not visible");
		}
	}

	/**
	 * 判断有没有数据，没数据就显示啥都没
	 * 
	 * @param lv
	 * @param rl
	 * @param size
	 */
	protected void judgeHasOrNotOrder(ListView lv, View rl, int size,int type) {
		if (size > 0) {
			lv.setVisibility(View.VISIBLE);
			rl.setVisibility(View.GONE);

		} else {
			lv.setVisibility(View.GONE);
			rl.setVisibility(View.VISIBLE);
			TextView tv = (TextView) ((ViewGroup)rl).getChildAt(1);
			switch (type){
				case 1:
					tv.setText("您还没有待服务的订单");
					break;
				case 2:
					tv.setText("您还没有服务中的订单");
					break;
				case 3:
					tv.setText("您还没有已结束的订单");
					break;
			}
		}
	}

	/**
	 * 给ListView设置点击事件
	 */
	protected void setOnItemClickListener(ListView listView) {
		if (listView != null) {
			listView.setOnItemClickListener(this);
		}
	}

	protected void goQDActivity(View view) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtils.goToActivity(getActivity(), MainActivity.class);
			}
		});
	}

	public void callBackActivity(String orderId) {
		((OrderListActivity) getActivity()).onItemClick(orderId);
	}

	/**
	 * 加载效果
	 */
	public void startLoading() {
		if (dialog == null && getActivity()!=null) {//防止报dialog的nullPointer异常
			dialog = new LoadingProgress(getActivity(), R.style.loading);
		}
		try {
			dialog.show();
		} catch (RuntimeException e) {
			dialog = null;
		}
	}

	/**
	 * 对话框消失
	 */
	public void stopLoading() {
		if (dialog != null && dialog.isShowing()) {
			try {
				dialog.dismiss();
				dialog = null;
			} catch (RuntimeException e) {
				dialog = null;
			}
		}
	}

	/**
	 * 配置listView的下拉事件上拉下拉都可以
	 */
	protected void configListViewRefreshListener(
			PullToRefreshListView mPullToRefreshListView) {
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (refreshView.isHeaderShown()) {
							String label = DateUtils.formatDateTime(
									getActivity(), System.currentTimeMillis(),
									DateUtils.FORMAT_SHOW_TIME
											| DateUtils.FORMAT_SHOW_DATE
											| DateUtils.FORMAT_ABBREV_ALL);
							refreshView.getLoadingLayoutProxy()
									.setLastUpdatedLabel(label);
							loadDatas();
							MDUtils.servicePageMD(getActivity(), "0", "0",
									MDConstans.ACTION_PULL_TO_REFRESH);
						} else {
							loadMore();
							MDUtils.servicePageMD(getActivity(), "0", "0",
									MDConstans.ACTION_LOAD_MORE_REFRESH);
						}
					}
				});
	}

	protected void configListViewCannotLoadMore(
			final PullToRefreshListView mPullToRefreshListView) {
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (refreshView.isHeaderShown()) {
							String label = DateUtils.formatDateTime(
									getActivity(), System.currentTimeMillis(),
									DateUtils.FORMAT_SHOW_TIME
											| DateUtils.FORMAT_SHOW_DATE
											| DateUtils.FORMAT_ABBREV_ALL);
							refreshView.getLoadingLayoutProxy()
									.setLastUpdatedLabel(label);
							loadDatas();
							MDUtils.OrderListPageMD(orderState, "0", "0",
									MDConstans.ACTION_PULL_TO_REFRESH);
						} else {
							LogUtils.LogE("ashenashenashen", "loadMore");
							if (handler == null)
								handler = new Handler();
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									mPullToRefreshListView.onRefreshComplete();
									LogUtils.LogE("ashenashenashen", "complete");
								}
							}, 500);
						}
					}
				});
	}


	@Override
	public void onLoginInvalidate() {
		Log.e("shenzhixin","hahaha");
		OrderListActivity ola = (OrderListActivity) getActivity();
		ola.onLoginInvalidate();
	}

	/**
	 * 给listView设置动画
	 * @param lv
	 * @param adapter
	 */
	protected void setListViewWithAnimation(ListView lv,BaseAdapter adapter){
		if(lv==null || adapter==null) return;
//		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
//		SwingRightInAnimationAdapter swingRightInAnimationAdapter = new SwingRightInAnimationAdapter(swingBottomInAnimationAdapter);
//		swingRightInAnimationAdapter.setAbsListView(lv);
		lv.setAdapter(adapter);
	}
}
