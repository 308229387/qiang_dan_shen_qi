package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;

public class PopDetailModel extends NetWorkModel {

	public PopDetailModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
		super(baseSourceModelCallBack, context);
	}

	@Override
	protected HttpRequest<String> createHttpRequest() {
		return new HttpRequest<String>(HttpRequest.METHOD_GET, "", this);
	}

}
