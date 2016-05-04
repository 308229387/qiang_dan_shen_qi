package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
/**
 * 58余额的获取model
 * @author 58
 *
 */
public class YuEModel extends NetWorkModel{
	public YuEModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
		super(baseSourceModelCallBack, context);
	}

	@Override
	protected ZhaoBiaoRequest<String> createHttpRequest() {
		String aa = URLConstans.GET_BALANCE_API + UrlSuffix.getApiBalance();
		ZhaoBiaoRequest<String> request = new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_GET, aa, this);
		return request;
	}
}
