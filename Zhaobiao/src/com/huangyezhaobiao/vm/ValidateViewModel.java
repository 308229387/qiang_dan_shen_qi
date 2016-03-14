package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.NetWorkModel.TAG;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.model.ValidateModel;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;

import java.util.Map;

public class ValidateViewModel extends SourceViewModel {

	public ValidateViewModel(NetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
	}

	public void getCode(String userId, String mobile,boolean rebind) {
		// //t.setRequestMethodPost();
		// HashMap<String, String> params_map = new HashMap<String, String>();
		// //请求API: http://serverdomain/api/sendCode?mobile=&token=&userId=
		// params_map.put("mobile",mobile);
		// params_map.put("userId", userId);
		// params_map.put("token",new Date().getTime()+"");
		// t.configParams(params_map);
		t.type = TAG.REFRESH;
		if(!rebind) {
			t.setRequestURL(URLConstans.BASE_URL + "api/sendCode?mobile=" + mobile + "&userId=" + userId +"&"+ UrlSuffix.getCommonSuffix(context));
		}else{
			t.setRequestURL(URLConstans.BASE_URL + "api/sendCode?mobile=" + mobile + "&userId=" + userId + "&"+UrlSuffix.getCommonSuffix(context)/*+"&rebind=1"*/);
		}
		t.getDatas();

	}

	public void validate(String userId, String mobile, String code) {
		// t.setRequestMethodPost();
		// HashMap<String, String> params_map = new HashMap<String, String>();
		// //请求API:
		// //http://serverdomain/api/validate?userId=&mobile=&code=&token=
		// params_map.put("mobile",mobile);
		// params_map.put("userId", userId);
		// params_map.put("code",code);
		// params_map.put("token",new Date().getTime()+"");
		// t.configParams(params_map);
		t.type = TAG.LOGIN;
		t.setRequestURL(URLConstans.BASE_URL+"api/validate?mobile=" + mobile + "&userId=" + userId + "&code=" + code
				+ "&"+UrlSuffix.getCommonSuffix(context));
		t.getDatas();
	}

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {

		return new ValidateModel(this, context);
	}

	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {

		int status = bean.getStatus();
		Map<String,String> map = jsonTransferToMap(bean);
		String msgStatus = map.get("status");
		String msgs       = map.get("msg");
		if (status == 0) {
			if (model.type == TAG.REFRESH) {
				if("0".equals(msgStatus)){
					callBack.onLoadingSuccess(jsonTransferToMap(bean).get("status"));
				}else{
					callBack.onLoadingSuccess(msgs);
				}

			} else {
				callBack.onLoadingSuccess(jsonTransferToMap(bean));
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

}
