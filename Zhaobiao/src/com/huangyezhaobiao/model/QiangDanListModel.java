package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.ListNetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.BidListUtils;
import com.huangyezhaobiao.utils.UserUtils;
/**
 * 抢单信息的model
 * @author 58
 *
 */
public class QiangDanListModel extends ListNetWorkModel{
	public QiangDanListModel(NetworkModelCallBack baseSourceModelCallBack,
			Context context) {
		super(baseSourceModelCallBack, context);
	}
	private String orderState;
	@Override
	public void loadMore() {
		super.loadMore();
		//Log.e("ashen", "loadmore..page"+current_load_page);
		//String userId = "3086909153025785949";
		//String userId = UserUtils.userId;
		setRequestURL(URLConstans.BASE_URL+"api/getBids?userId="+UserUtils.userId+
				"&pushId="+BidListUtils.pushId+
				"&bidId="+BidListUtils.bidId+
				"&bidState="+BidListUtils.bidState+URLConstans.AND  + UrlSuffix.getCommonSuffix(context));
		getDatas();
	}

	@Override
	public void refresh() {
		//根据m
		super.refresh();
		//String userId = "3086909153025785949";
		//String userId = UserUtils.userId;
		//Log.e("ashen", "refresh..page"+current_load_page);
		setRequestURL(URLConstans.BASE_URL+"api/getBids?userId="+UserUtils.userId+
				"&pushId="+-1+
				"&bidId="+-1+
				"&bidState="+-1+URLConstans.AND  + UrlSuffix.getCommonSuffix(context));
		getDatas();
	}

	

	@Override
	public void loadCache() {
		
	}

	@Override
	protected HttpRequest<String> createHttpRequest() {
		return new HttpRequest<String>(HttpRequest.METHOD_GET,"",this);
	}

	/**
	 * 设置订单的状态，进行不同的url获取
	 * @param orderState
	 */
	public void setOrderState(String orderState){
		this.orderState = orderState;
	}
}
