package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.ListNetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.LogUtils;

/**
 * 信息中心的抢单信息的model
 * @author shenzhixin
 *
 */
public class CenterQiangDanListModel extends ListNetWorkModel{
	private String orderState;
	public CenterQiangDanListModel(NetworkModelCallBack baseSourceModelCallBack,
			Context context) {
		super(baseSourceModelCallBack, context);
	}

	/**
	 * 返回当前是第几页
	 * @return
	 */
	public int getCurrentPage(){
		return current_load_page;
	}
	
	
	@Override
	public void loadMore() {
		current_load_page++;
		LogUtils.LogE("ashen", "loadmore..page" + current_load_page);
		//2016.5.3 add
		String url = URLConstans.MESSAGE_CENTER_URL + UrlSuffix.getAppCenterSuffix(""+current_load_page);
		//2016.5.3 add end
		setRequestURL(url);
		getDatas();
	}

	@Override
	public void refresh() {
		//根据m
		current_load_page = 1;
		LogUtils.LogE("ashen", "refresh..page" + current_load_page);
		//2016.5.3 add
		String url = URLConstans.MESSAGE_CENTER_URL + UrlSuffix.getAppCenterSuffix("1");
		//2016.5.3 add end
		LogUtils.LogE("shenzhixintest", "url:" + url);
		//::http://192.168.118.41/app/order/orderlist?userid/27353503259910&orderstate/1&&pageNum=1&token=1
		//"http://192.168.118.41/app/order/orderlist?userid=24454277549826&orderstate="+QiangDanBaseFragment.orderState+"&pageNum=1&token=1"
		setRequestURL(url);
		getDatas();
	}



	@Override
	public void loadCache() {
		
	}

	@Override
	protected ZhaoBiaoRequest<String> createHttpRequest() {
		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_GET,"",this);
	}

	/**
	 * 设置订单的状态，进行不同的url获取
	 * @param orderState
	 */
	public void setOrderState(String orderState){
		this.orderState = orderState;
	}

	@Override
	public void onLoadingFailure(String err) {

		baseSourceModelCallBack.onLoadingFailure(err);
	}
}
