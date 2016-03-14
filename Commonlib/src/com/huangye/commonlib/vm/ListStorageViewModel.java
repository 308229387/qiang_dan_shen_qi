package com.huangye.commonlib.vm;

import com.huangye.commonlib.model.ListStorageBaseModel;
import com.huangye.commonlib.vm.callback.StorageVMCallBack;

import android.content.Context;

public abstract class ListStorageViewModel extends StorageViewModel{

	protected ListStorageBaseModel listModel;
	
	public ListStorageViewModel(Context context, StorageVMCallBack callback) {
		super(context, callback);
	}
	
//	public void loadMore(){
//		model.type = TAG.LOADMORE;
//		Log.e("sourceModel", "ViewModel......loadMore");
//		model.loadMore();
//	}
//	
//	public void refresh() {
//		model.type = TAG.REFRESH;
//		model.refresh();
//	}
//	
//	public void canLoadMore(){
//		model.canLoadMore();
//	}
	
	
}
