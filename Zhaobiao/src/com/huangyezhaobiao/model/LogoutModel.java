package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;

public class LogoutModel extends NetWorkModel {

	public LogoutModel(NetworkModelCallBack baseSourceModelCallBack,
			Context context) {
		super(baseSourceModelCallBack, context);
	}

	@Override
	protected ZhaoBiaoRequest<String> createHttpRequest() {
		//2016.5.3 add
//		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_GET, URLConstans.LOGOUT_API_URL+ UrlSuffix.getLogoutSuffix(context),this);
		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_GET, URLConstans.LOGOUT_API_URL,this);
		//2016.5.3 add end
	}

}
