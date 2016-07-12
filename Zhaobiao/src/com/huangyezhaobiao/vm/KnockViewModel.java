package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.activity.BidSuccessActivity;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.model.KnockModel;
import com.huangyezhaobiao.utils.BidUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.UserUtils;

import java.util.Date;
import java.util.HashMap;

public class KnockViewModel extends SourceViewModel {
	private Context context;
	public KnockViewModel(NetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 *
	 * @param bean
	 * @param bidSource 从哪抢的单子，0为弹窗抢，1为列表抢,2为详情页抢
	 */
	public void knock(PushToPassBean bean,String bidSource) {
		t.setRequestMethodPost();
		HashMap<String, String> params_map = new HashMap<String, String>();
//		String userId = UserUtils.userId; // UserUtils.getUserId(context);//
											// 24454277549825L;//Long.parseLong(UserUtils.getUserId(context));//27353503259910L
		long bidId = bean.getBidId();// bean.getBidId();//3088606247077150811L;//3089019022954856539L
		long pushId = bean.getPushId();// bean.getPushId();//3087873205681979483L;//3089020612659380315L
		int pushTurn = bean.getPushTurn();// 1;//bean.getPushTurn();
//		params_map.put("userid", "" + userId);
		params_map.put("bidId", "" + bidId);
		params_map.put("pushId", "" + pushId);
		params_map.put("pushturn", "" + pushTurn);
		params_map.put("token", new Date().getTime() + "");
//		params_map.put("UUID", PhoneUtils.getIMEI(context));
//		params_map.put("platform","1");
//		params_map.put("version","2");
		//2016.2.23 RD shenzhixin add params userState and bidSource
		String serviceState = SPUtils.getServiceState(context);
		switch (serviceState){
			case "1"://服务模式
				params_map.put("userState","0");
				break;
			case "2"://休息模式
				params_map.put("userState","1");
				break;
		}

		params_map.put("bidSource",bidSource);
		t.configParams(params_map);
		t.getDatas();
		//把id存起来
		BidUtils.bidLists.add(pushId+"");
		//
		LogUtils.LogE("shenzhixin", params_map.toString());
	}

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		return new KnockModel(this, context);
	}

	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		int status = bean.getStatus();
		if (status == 0) {
			JSONObject object = JSON.parseObject(bean.getData());
			if (null != object) {
				int knockStatus = object.getInteger("status");
				long orderId = object.getLong("orderId");
				BidSuccessActivity.orderId = orderId;
				callBack.onLoadingSuccess(knockStatus);
			}
		} else {
			String msg = bean.getMsg();
			if (!TextUtils.isEmpty(msg)) {
				callBack.onLoadingError(bean.getMsg());
			} else {
				callBack.onLoadingError("连接失败!");
			}
		}
	}

	@Override
	public void onVersionBack(String value) {
		//doNothing
	}
}
