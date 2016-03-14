package com.huangyezhaobiao.vm;

import android.content.Context;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.SumplymentInfoJoinBean;
import com.huangyezhaobiao.bean.popdetail.BottomViewBean;
import com.huangyezhaobiao.bean.popdetail.CleaningBidDetailBean;
import com.huangyezhaobiao.bean.popdetail.CommonAffiliateInfoBean;
import com.huangyezhaobiao.bean.popdetail.CommonDecorateInfoBean;
import com.huangyezhaobiao.bean.popdetail.CommonDecorateProjectBean;
import com.huangyezhaobiao.bean.popdetail.CommonDecorationPartBean;
import com.huangyezhaobiao.bean.popdetail.CommonInternationalRegisterInfoBean;
import com.huangyezhaobiao.bean.popdetail.CommonPersonalRegisterInfoBean;
import com.huangyezhaobiao.bean.popdetail.LogBean;
import com.huangyezhaobiao.bean.popdetail.NannyBidDetailBean;
import com.huangyezhaobiao.bean.popdetail.QDDetailBaseBean;
import com.huangyezhaobiao.bean.popdetail.SumplymentInfoBean;
import com.huangyezhaobiao.bean.popdetail.TimeFeeBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.inter.OrderDetailCallBack;
import com.huangyezhaobiao.lib.ZBBaseDetailViewModel;
import com.huangyezhaobiao.model.PopDetailModel;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

public class QIangDanDetailViewModel extends ZBBaseDetailViewModel<QDDetailBaseBean> {

	LogBean log;
	BottomViewBean bottom;
	OrderDetailCallBack back;
	PushToPassBean passBean;

	public QIangDanDetailViewModel(NetWorkVMCallBack callBack, OrderDetailCallBack back, Context context) {
		super(callBack, context);

		this.back = back;
	}

	public void getdata(PushToPassBean passBean) {
		// ((PopDetailModel)t).setOrderId(orderId);
		// http://serverdomain/api/getBidDetail?bidId=&pushId=&pushTurn=&token
		if(passBean == null){
			onLoadingFailure("有异常");
			return;
		}
		long bidId = passBean.getBidId();
		long pushId = passBean.getPushId();
		int pushTurn = passBean.getPushTurn();
		t.setRequestURL(URLConstans.BASE_URL+"api/getBidDetail?" +"userId="+ UserUtils.getUserId(context)+"&bidId=" + bidId + "&pushId=" + pushId
				+ "&pushTurn=" + pushTurn + "&"+ UrlSuffix.getCommonSuffix(context));
		t.getDatas();
	}

	@Override
	protected void initKey() {
		key = "newtype";//没什么卵用

	}

	@Override
	protected void registerSourceDirs() {
		sourcesDir.put("common_info_decorate_area", CommonDecorateInfoBean.class);
		sourcesDir.put("sumplyment_info_area", SumplymentInfoBean.class);
		sourcesDir.put("time_fee_area", TimeFeeBean.class);
		sourcesDir.put("common_info_international_register_area", CommonInternationalRegisterInfoBean.class);
		sourcesDir.put("common_info_personal_register_area", CommonPersonalRegisterInfoBean.class);
		//2015.8.17 add
		sourcesDir.put("common_info_join_invest_area", CommonAffiliateInfoBean.class);
		sourcesDir.put("sumplyment_info_join_invest_area", SumplymentInfoJoinBean.class);

		//2015.8.25 add
		sourcesDir.put("common_info_decorate_project_area", CommonDecorateProjectBean.class);//工程装修
		sourcesDir.put("common_info_decorate_part_area", CommonDecorationPartBean.class);//局部装修
		//2015.12.5 add common_info_babySitter_area
		sourcesDir.put("common_info_babySitter_area", NannyBidDetailBean.class);//保姆月嫂
		sourcesDir.put("common_info_washClean_area", CleaningBidDetailBean.class);//保洁清洗
	}

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		return new PopDetailModel(this, context);
	}

	@Override
	protected List<View> transferListBeanToListView(List<QDDetailBaseBean> list) {

		List<View> viewList = new ArrayList<View>();

		for (QDDetailBaseBean bean : list) {
			// bean.set
			viewList.add(bean.initView(context));
		}

		return viewList;
	}

	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		try {
			JSONObject object = JSON.parseObject(bean.getOther());
			log = JsonUtils.jsonToObject(object.getString("log"), LogBean.class);
			bottom = JsonUtils.jsonToObject(object.getString("bottom_view_area"), BottomViewBean.class);

			back.back(log, bottom);
		} catch (Exception e) {

		}
		super.onLoadingSuccess(bean, model);
	}

}
