package com.huangyezhaobiao.model;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;

import android.content.Context;

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
	protected HttpRequest<String> createHttpRequest() {

		return new HttpRequest<String>(HttpRequest.METHOD_GET, "", this);
	}

}
