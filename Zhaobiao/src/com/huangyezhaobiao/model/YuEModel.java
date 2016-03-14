package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
/**
 * 58余额的获取model
 * @author 58
 *
 */
public class YuEModel extends NetWorkModel{
	public YuEModel(NetworkModelCallBack baseSourceModelCallBack,
			Context context) {
		super(baseSourceModelCallBack, context);
	}


	
	

	@Override
	protected HttpRequest<String> createHttpRequest() {
		String aa = URLConstans.GET_BALANCE_API + UrlSuffix.getApiBalance(context);
		HttpRequest<String> request = new HttpRequest<String>(HttpRequest.METHOD_GET, aa, this);
		return request;
	}
	
	
	

}
