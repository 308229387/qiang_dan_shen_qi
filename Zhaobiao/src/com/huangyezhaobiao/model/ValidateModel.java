package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;

public class ValidateModel extends NetWorkModel {

	public ValidateModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
		super(baseSourceModelCallBack, context);
	}

//	public void getCode() {
//		// http://serverdomain/api/validate?userId=&mobile=&code=&token=
//		setRequestURL("http://192.168.118.41/api/sendCode");
//	}
//
//	public void validate() {
//		// setRequestURL("http://192.168.118.41/api/getBidDetail?bidId&token=123");
//		setRequestURL("http://192.168.118.41/api/validate");
//	}

	@Override
	protected ZhaoBiaoRequest<String> createHttpRequest() {
		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_GET, "", this);
	}

}
