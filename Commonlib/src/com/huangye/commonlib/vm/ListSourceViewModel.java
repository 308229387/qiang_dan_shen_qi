package com.huangye.commonlib.vm;

import android.content.Context;

import com.huangye.commonlib.model.ListNetWorkModel;
import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.NetWorkModel.TAG;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.utils.LogUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;

import java.util.ArrayList;
import java.util.List;

public abstract class ListSourceViewModel<T> extends SourceViewModel implements NetworkModelCallBack{
	protected ListNetWorkModel model;
	protected ListNetWorkVMCallBack callBack;
	protected List<T> allDatas = new ArrayList<T>();
	protected int pageCount;//总共的页数
	public ListSourceViewModel(ListNetWorkVMCallBack callBack,Context context){
		super((NetWorkVMCallBack)callBack,context);
		model = (ListNetWorkModel) initListNetworkModel(context);
		this.callBack = callBack;
	}
	

	
	public void loadMore(){
		model.type = TAG.LOADMORE;
		LogUtils.LogE("sourceModel", "ViewModel......loadMore");
		model.loadMore();
		
	}
	
	public void refresh() {
		model.type = TAG.REFRESH;
		model.refresh();
	}
	
	@Override
	public void onLoadingCancell() {
		callBack.onLoadingCancel();
	}

	@Override
	public void onLoadingStart() {
		callBack.onLoadingStart();
	}

	@Override
	public void onLoadingFailure(String err) {
		callBack.onLoadingError(err);
	}

	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		LogUtils.LogE("asasas", "ss:" + bean.getData());
		//List<Map<String,String>> t = jsonTransferToListMap(bean);
		//List<T> ts = JsonUtils.jsonToObjectList(bean.getData(), clazz);
		
		List<T> ts = transferToListBean(bean.getData());
		
		LogUtils.LogE("asdfggg", "list size:" + ts.size());
		callBack.onLoadingSuccess(ts);
		if(model.type==TAG.REFRESH){
			allDatas = ts;
			callBack.onRefreshSuccess(allDatas);
			callBack.canLoadMore();
		}else if(model.type == TAG.LOADMORE){
			allDatas.addAll(ts);
			callBack.onLoadingMoreSuccess(allDatas);
		}
		pageCount = getPageCount(bean);
		if(canListLoadMore()){
			callBack.canLoadMore();
		}else{
			callBack.loadMoreEnd();
		}
	}

	/**
	 * 得到可加载的总页数
	 * @param bean
	 * @return
	 */
	protected abstract int getPageCount(NetBean bean);
	
	protected abstract List<T> transferToListBean(String t);
	
	//protected abstract List<T> transferListMapToListBean(List<Map<String, String>> t);



	/**
	 * 判断还可不可以加载更多的提示了
	 * @return
	 */
	protected boolean canListLoadMore(){
		LogUtils.LogE("ashjdhsj", "pageCount:" + pageCount + ",currentPage:" + model.getCurrentPage());
		return pageCount>model.getCurrentPage();
	}



	
	

	
	

	
	
	
	
}
