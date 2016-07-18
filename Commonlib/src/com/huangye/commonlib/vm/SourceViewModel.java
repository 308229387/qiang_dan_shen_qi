package com.huangye.commonlib.vm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;

import java.util.List;
import java.util.Map;

public abstract class SourceViewModel implements NetworkModelCallBack{
	protected NetWorkVMCallBack callBack;
	protected NetWorkModel t;
	protected Context context;
	public SourceViewModel(NetWorkVMCallBack callBack,Context context){
		this.callBack = callBack;
		t = initListNetworkModel(context);
		this.context = context;

	}
	protected abstract NetWorkModel initListNetworkModel(Context context);

	@Override
	public void onLoadingCancell() {
		if(callBack!=null)
			callBack.onLoadingCancel();
	}

	@Override
	public void onLoadingStart() {
		if(callBack!=null)
			callBack.onLoadingStart();

	}

	@Override
	public void onLoadingFailure(String err) {
		if(callBack!=null)
			callBack.onLoadingError(err);
	}

	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		int status = bean.getStatus();
		if(callBack!=null) {
			if (status == 0) {
				callBack.onLoadingSuccess(jsonTransferToMap(bean));
			}else{
				String msg = bean.getMsg();
				if (!TextUtils.isEmpty(msg)) {
					callBack.onLoadingError(bean.getMsg());
				} else {
					callBack.onLoadingError("连接失败!");
				}
			}
		}
	}

	@Override
	public void noInternetConnect() {
		if(callBack!=null) {
			callBack.onNoInterNetError();
		}
	}

	/**
	 * 把json转化成map
	 * @param bean
	 * @return
	 */
	protected Map<String,String> jsonTransferToMap(NetBean bean){
		Log.e("adsss", "balance:"+bean.getData());
		return JsonUtils.jsonToMap(bean.getData());
	}

	/**
	 * 把json转化成list<Map>
	 * @param bean
	 * @return
	 */
	protected List<Map<String,String>> jsonTransferToListMap(NetBean bean){
		return JsonUtils.jsonToListMap(bean.getData());
	}

	/**
	 * 详情页的转化，把Json转化成list<map>
	 * @param bean
	 * @return
	 */
	protected List<Map<String,String>> jsonTransferToDetailsListMap(NetBean bean){
		return JsonUtils.jsonToNewListMap(bean.getData());
	}

	@Override
	public void onModelLoginInvalidate() {
		if(callBack != null)
		callBack.onLoginInvalidate();
	}


	@Override
	public void onVersionBack(String value) {
		if (callBack != null)
		callBack.onVersionBack(value);
	}
}
