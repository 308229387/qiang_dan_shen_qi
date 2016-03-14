package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.ListNetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HttpRequest;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
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
		String url = URLConstans.MESSAGE_CENTER_URL + UrlSuffix.getAppCenterSuffix(context,""+current_load_page);
		setRequestURL(url);
		LogUtils.LogE("ashenTest", "loadmore url:" + "http://192.168.118.41/app/order/orderlist?userid=32904878844161&orderstate=" + QiangDanBaseFragment.orderState + "&pageNum=1&token=1");
		getDatas();
	}

	@Override
	public void refresh() {
		//根据m
		current_load_page = 1;
		LogUtils.LogE("ashen", "refresh..page" + current_load_page);
		String url = URLConstans.MESSAGE_CENTER_URL + UrlSuffix.getAppCenterSuffix(context,"1");
		LogUtils.LogE("shenzhixintest", "url:" + url);
		//::http://192.168.118.41/app/order/orderlist?userid/27353503259910&orderstate/1&&pageNum=1&token=1
		//"http://192.168.118.41/app/order/orderlist?userid=24454277549826&orderstate="+QiangDanBaseFragment.orderState+"&pageNum=1&token=1"
		setRequestURL(url);
		LogUtils.LogE("ashenTest", "refresh url:" + "http://192.168.118.41/app/order/orderlist?userid=24454277549826&orderstate=" + QiangDanBaseFragment.orderState + "&pageNum=1&token=1");
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

	@Override
	public void onLoadingFailure(String err) {

		baseSourceModelCallBack.onLoadingFailure(err);
	}
}
