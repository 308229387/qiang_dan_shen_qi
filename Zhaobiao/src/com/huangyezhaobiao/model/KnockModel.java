package com.huangyezhaobiao.model;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;
import com.huangyezhaobiao.url.URLConstans;

import android.content.Context;

public class KnockModel extends NetWorkModel{

	public KnockModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
		super(baseSourceModelCallBack, context);
	}

	@Override
	protected HttpRequest<String> createHttpRequest() {
		return new HttpRequest<String>(HttpRequest.METHOD_POST, URLConstans.BASE_URL+"app/order/bidding", this);
	}
	//?bidid=3088606247077085275&userid=24454277549825&pushid=3087873867107074139&pushturn=1&token=123


}
