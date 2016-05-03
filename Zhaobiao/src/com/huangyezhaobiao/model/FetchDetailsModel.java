package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.LogUtils;

public class FetchDetailsModel extends NetWorkModel{
	private String orderId;
	public FetchDetailsModel(NetworkModelCallBack baseSourceModelCallBack,
			Context context) {
		super(baseSourceModelCallBack, context);
	}
	
	/**
	 * "3088047297886421281"
	 * @param orderId
	 */
	public void setOrderId(String orderId){
		this.orderId = orderId;
		//2016.5.3 add
		String url = URLConstans.MESSAGE_CENTER_DETEAILS_URL+UrlSuffix.getAppCenterDetailsSuffix(orderId);
		//2015.5.3 add end
		LogUtils.LogE("ashenDetail", "url:" + url);
		setRequestURL(url);
		LogUtils.LogE("ashenFetch", "model......orderId:" + orderId);
	}


	@Override
	protected HttpRequest<String> createHttpRequest() {
		/**
		 * http://192.168.118.41/app/order/orderdetail?orderid=3088047252787691809&token=1
		 */
		//2016.5.3 add
		String url = URLConstans.MESSAGE_CENTER_DETEAILS_URL+UrlSuffix.getAppCenterDetailsSuffix(orderId);
		//2016.5.3 add end
		LogUtils.LogE("ashenDetail", "url:" + url);
		return new HttpRequest<String>(HttpRequest.METHOD_GET, url,this);
	}



}
