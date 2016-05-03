package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;

public class KnockModel extends NetWorkModel{

	public KnockModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
		super(baseSourceModelCallBack, context);
	}

	@Override
	protected ZhaoBiaoRequest<String> createHttpRequest() {
		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_POST, URLConstans.BASE_URL+"app/order/bidding", this);
	}
	//?bidid=3088606247077085275&userid=24454277549825&pushid=3087873867107074139&pushturn=1&token=123


}
