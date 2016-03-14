package com.huangye.commonlib.model;

import java.util.List;

import com.huangye.commonlib.delegate.StorageCallBack;
import com.huangye.commonlib.model.callback.StorageModelCallBack;

import android.content.Context;

public class ListStorageBaseModel<T> extends StorageBaseModel<T> implements StorageCallBack{

	public ListStorageBaseModel(Context context, StorageModelCallBack callback) {
		super(context, callback);
	}
	


}
