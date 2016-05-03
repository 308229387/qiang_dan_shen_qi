package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;

public class PopDetailModel extends NetWorkModel {

	public PopDetailModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
		super(baseSourceModelCallBack, context);
	}

	@Override
	protected ZhaoBiaoRequest<String> createHttpRequest() {
		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_GET, "", this);
	}

}
