package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.ListStorageBaseModel;
import com.huangye.commonlib.model.callback.StorageModelCallBack;
import com.huangyezhaobiao.bean.push.PushToStorageBean;

public class CenterMessageStorageModel extends ListStorageBaseModel<PushToStorageBean>{

	public CenterMessageStorageModel(Context context,
			StorageModelCallBack callback) {
		super(context, callback);
	}

}
