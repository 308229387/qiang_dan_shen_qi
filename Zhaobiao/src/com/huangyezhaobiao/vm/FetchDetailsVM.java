package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.mydetail.CenterAffiliateDetailBean;
import com.huangyezhaobiao.bean.DetailsLogBean;
import com.huangyezhaobiao.bean.mydetail.CleaningOrderDetailBean;
import com.huangyezhaobiao.bean.mydetail.IACIndividualDetailBean;
import com.huangyezhaobiao.bean.mydetail.IACInternationalDetailBean;
import com.huangyezhaobiao.bean.mydetail.NannyOrderDetailBean;
import com.huangyezhaobiao.bean.mydetail.OrderDetailDecorateBean;
import com.huangyezhaobiao.bean.mydetail.OrderDetailDecoratePartBean;
import com.huangyezhaobiao.bean.mydetail.OrderDetailDecorateProjectBean;
import com.huangyezhaobiao.bean.mydetail.PriceAreaBean;
import com.huangyezhaobiao.bean.mydetail.ServiceTypeBean;
import com.huangyezhaobiao.bean.popdetail.QDDetailBaseBean;
import com.huangyezhaobiao.lib.ZBBaseDetailViewModel;
import com.huangyezhaobiao.model.FetchDetailsModel;
import com.huangyezhaobiao.utils.DetailsLogBeanUtils;
import com.huangyezhaobiao.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个需要明天写好
 * @author shenzhixin
 *
 */
public class FetchDetailsVM extends ZBBaseDetailViewModel<QDDetailBaseBean>{
	
	public FetchDetailsVM(NetWorkVMCallBack callBack, Context context,long orderId) {
		super(callBack, context);
		((FetchDetailsModel)t).setOrderId(orderId+"");
	}

	
	public void setOrderId(){
		
	}
	@Override
	protected void registerSourceDirs() {
		sourcesDir.put("serviceType_area", ServiceTypeBean.class);
		sourcesDir.put("orderdetail_decorate_area", OrderDetailDecorateBean.class);
		sourcesDir.put("price_area", PriceAreaBean.class);
		sourcesDir.put("orderdetail_personal_register_area", IACIndividualDetailBean.class);
		sourcesDir.put("orderdetail_international_register_area", IACInternationalDetailBean.class);
		//2015.8.17
		sourcesDir.put("orderdetail_join_invest_area", CenterAffiliateDetailBean.class);
		//2015.8.25 add
		sourcesDir.put("orderdetail_decorate_project_area", OrderDetailDecorateProjectBean.class);//工程装修的订单详情
		sourcesDir.put("orderdetail_decorate_part_area", OrderDetailDecoratePartBean.class);//局部装修的订单详情
		//2015.12.7 add
		sourcesDir.put("orderdetail_babySitter_area", NannyOrderDetailBean.class);
		sourcesDir.put("orderdetail_washClean_area", CleaningOrderDetailBean.class);
	}

	@Override
	protected List<View> transferListBeanToListView(List<QDDetailBaseBean> list) {
		List<View> viewList = new ArrayList<View>();
		for(QDDetailBaseBean bean :list){
			viewList.add(bean.initView(context));
		}
		return viewList;
	}

	@Override
	protected void initKey() {
		key = "newtype";
	}

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		return new FetchDetailsModel(this, context);
	}
	
	public void fetchDetailDatas(){
		t.getDatas();
	}

	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		String other = bean.getOther();//得到一些埋点的信息
		if(!TextUtils.isEmpty(other)){
			JSONObject object = JSON.parseObject(other);
			LogUtils.LogE("ashenDeggg", "other:" + other);
			DetailsLogBean log = JsonUtils.jsonToObject(object.getString("log"),DetailsLogBean.class);
			DetailsLogBeanUtils.bean.setBidID(log.getBidID());
			DetailsLogBeanUtils.bean.setBidState(log.getBidState());
			DetailsLogBeanUtils.bean.setCateID(log.getCateID());
		}
		super.onLoadingSuccess(bean, model);
	}
	

}
