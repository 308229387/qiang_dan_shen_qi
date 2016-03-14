package com.huangye.commonlib.test;

import com.huangye.commonlib.R;
import com.huangye.commonlib.model.ListStorageBaseModel;
import com.huangye.commonlib.model.callback.ListStorageModelCallBack;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class MyStoreModel extends ListStorageBaseModel<DecorationBean>{
	
	
	
	
	public MyStoreModel(Context context, ListStorageModelCallBack callback) {
		super(context, callback);
		
	}

	@Override
	public void getDataSuccess(Object o) {
		storageModelCallBack.getDataSuccess(o);
		
	}
	
	
}
