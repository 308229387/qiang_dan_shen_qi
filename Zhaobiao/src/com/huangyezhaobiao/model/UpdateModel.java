package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;

public class UpdateModel extends NetWorkModel{

	public UpdateModel(NetworkModelCallBack baseSourceModelCallBack,
			Context context) {
		super(baseSourceModelCallBack, context);
	}

	@Override
	protected ZhaoBiaoRequest<String> createHttpRequest() {
		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_GET, URLConstans.BASE_URL_APP+"getversion/apk?appId=1", this);
	}

}
