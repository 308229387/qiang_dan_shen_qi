package com.huangyezhaobiao.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.OrderLVAdapter;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.PullToRefreshListViewUtils;

import java.util.List;

/**
 * 待服务列表
 * @author shenzhx
 *
 */
public class ReadyServiceFragment extends QiangDanBaseFragment {
	private View root_fragment_all;
	private PullToRefreshListView lv_all_fragment;
	private String a ;
	private ListView lv;
	private View layout_no_internet,layout_no_internet_click;
	private SwipeRefreshLayout srl;
	private OrderLVAdapter adapter;
	@Override
	protected void loadDatas() {
		lvm.refresh();
		LogUtils.LogE("hjhjhjhj", "line..40");
		LogUtils.LogE("ashenService", "loadDatas");
}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(root_fragment_all==null){
			root_fragment_all = inflater.inflate(R.layout.layout_all_fragment, null);
			srl             = (SwipeRefreshLayout) root_fragment_all.findViewById(R.id.srl);
			srl.setRefreshing(true);
			layout_no_internets = root_fragment_all.findViewById(R.id.layout_no_internets);
			layout_no_internets.setVisibility(View.VISIBLE);
			lv_all_fragment = (PullToRefreshListView) root_fragment_all.findViewById(R.id.lv_all_fragment);
			lv = lv_all_fragment.getRefreshableView();
			layout_no_internet = root_fragment_all.findViewById(R.id.layout_no_internet);
			layout_no_internet_click = root_fragment_all.findViewById(R.id.layout_no_internet_click_refresh);
			adapter = new OrderLVAdapter(getActivity(),this,0);
			configRefreshableListView(lv_all_fragment);//配置pullLv
			configSwipeRefreshLayoutView(srl,lv);//配置swipeRefreshLayout
			lv.setAdapter(adapter);
			lv.setDividerHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
			LogUtils.LogE("ashenService", "setAdapter");
			setOnItemClickListener(lv);
			goQDActivity(layout_no_internet_click);
		} else {

		}
		
		return root_fragment_all;
	}

	@Override
	public void onRefreshSuccess(Object t) {
		if(layout_no_internets!=null){
			layout_no_internets.setVisibility(View.GONE);
		}
		List<QDBaseBean> beans = (List<QDBaseBean>) t;
		LogUtils.LogE("ashensssss", "sizeeeee:" + beans.size());
		adapter.refreshSuccess(beans);

		LogUtils.LogE("ashenService", "onRefreshSuccess");
		lv_all_fragment.onRefreshComplete();
		srl.setRefreshing(false);
		judgeHasOrNotOrder(lv, layout_no_internet,beans.size(),1);
	}

	@Override
	public void onLoadingMoreSuccess(Object res) {
		if(layout_no_internets!=null){
			layout_no_internets.setVisibility(View.GONE);
		}
<<<<<<< Updated upstream
		List<QDBaseBean> beans = (List<QDBaseBean>) res;
=======
		List<QDBaseBean> addBeans = (List<QDBaseBean>) res;

		if(addBeans == null || addBeans.size() ==0 ){
			ToastUtils.makeText(getActivity(),"没有更多数据了",Toast.LENGTH_SHORT).show();
		}
		beans.addAll(addBeans);
>>>>>>> Stashed changes
		adapter.loadMoreSuccess(beans);
		lv_all_fragment.onRefreshComplete();
	}

	@Override
	public void onLoadingStart() {
		startLoading();
	}

	@Override
	public void onLoadingSuccess(Object t) {
		stopLoading();
	}

	@Override
	public void onLoadingError(String msg) {
		stopLoading();
		if(layout_no_internets!=null){
			layout_no_internets.setVisibility(View.GONE);
		}
		if(lv_all_fragment!=null &&lv_all_fragment.isRefreshing())
			lv_all_fragment.onRefreshComplete();
	}

	@Override
	public void onLoadingCancel() {
		stopLoading();
		if(lv_all_fragment!=null &&lv_all_fragment.isRefreshing())
			lv_all_fragment.onRefreshComplete();
	}

	@Override
	protected void loadMore() {
		lvm.loadMore();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(root_fragment_all!=null && root_fragment_all.getParent()!=null)
			((ViewGroup)root_fragment_all.getParent()).removeView(root_fragment_all);
		LogUtils.LogE("ashen", "destroy");
		LogUtils.LogE("ashenService", "destroy");
		stopLoading();
	}
	

	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		LogUtils.LogE("hjhjhjhj", "line..124");
		if(root_fragment_all!=null){
			if(isVisibleToUser){
				LogUtils.LogE("hjhjhjhj", "line..127");
				orderState = Constans.READY_SERVICE;
				root_fragment_all.setVisibility(View.VISIBLE);
			}else{
				root_fragment_all.setVisibility(View.GONE);
			}
		}
		if(isVisibleToUser && lv!=null){
			configListView(lv, getAnimationController(-400,0));
		}
		LogUtils.LogE("hjhjhjhj", "line..137");
		super.setUserVisibleHint(isVisibleToUser);
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orderState = Constans.READY_SERVICE;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/*String orderId = adapter.getSources().get(position).getOrderId();
		callBackActivity(orderId);*/
	}

	@Override
	public void onNoInterNetError() {
		stopLoading();
		if(lv_all_fragment!=null &&lv_all_fragment.isRefreshing())
			lv_all_fragment.onRefreshComplete();
<<<<<<< Updated upstream
		if(layout_no_internets!=null) {
			layout_no_internets.setVisibility(View.VISIBLE);
		}
=======
//		if(layout_no_internets!=null) {
//			layout_no_internets.setVisibility(View.VISIBLE);
//		}
		ToastUtils.makeText(getActivity(), "当前没有网络", Toast.LENGTH_SHORT).show();
		page = 1;
		List<OrderBean> list = sqlUtils.findDatas(OrderBean.class,sort,page,limit,"userId",userId,"orderState","1",null);
		onRefreshSuccess(list);
>>>>>>> Stashed changes
	}

	@Override
	public void onAdapterRefreshSuccess() {
		if (lv_all_fragment != null && adapter != null) {
			setListViewWithAnimation(lv_all_fragment.getRefreshableView(),adapter);
		}
	}

	@Override
	public void onAdapterLoadMoreSuccess() {
		
	}

	@Override
	public void onAdapterViewClick(int id, PushToPassBean bean) {
		
	}


	@Override
	public void canLoadMore() {
	//	configRefreshableListView(lv_all_fragment);
		PullToRefreshListViewUtils.PullToListViewCanLoadMore(lv_all_fragment);
		configListViewRefreshListener(lv_all_fragment);
	}

	@Override
	public void loadMoreEnd() {
		PullToRefreshListViewUtils.PullToListViewCannotLoadMore(lv_all_fragment);
		configListViewCannotLoadMore(lv_all_fragment);
		
	}

}
