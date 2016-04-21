package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.LogUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.utils.PhoneUtils;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.LoginBean;
import com.huangyezhaobiao.model.CheckLoginModel;
import com.huangyezhaobiao.utils.PasswordEncrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class CheckLoginViewModel extends SourceViewModel{

	private static final String TAG = "CheckLoginViewModel";
	/** added by chenguangming*/
	private Context context;
	public CheckLoginViewModel(NetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
		this.context = context;
	}

	public void login(String param1,String param2){
		loginZhaobiao(param1,param2);
	}

	/**
	 * 登录到招标
	 * */
	private void loginZhaobiao(String param1,String param2){
		t.setRequestMethodPost();
		HashMap<String, String> params_map = new HashMap<String, String>();
		LogUtils.LogE("ashenlogin", "username:" + param1 + ",password:" + param2);
		try {
			String userName = URLEncoder.encode(param1, "utf-8");
			String userName1 = URLEncoder.encode(userName,"utf-8");
			params_map.put("password", PasswordEncrypt.encryptPassword(param2));
			LogUtils.LogE("ashenlogin", "username after :" + userName1);
			params_map.put("userName", userName1);
			params_map.put("platform", "1");
			params_map.put("deviceId", PhoneUtils.getIMEI(context));
			params_map.put("token", PhoneUtils.getIMEI(context));
			params_map.put("UUID", PhoneUtils.getIMEI(context));
			params_map.put("platform","1");
			params_map.put("version","2");
			t.configParams(params_map);
			t.getDatas();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


//		Log.v(TAG,"userId = " + userId +", ppu = " + ppu);
//		t.setRequestMethodPost();
//		HashMap<String, String> params_map = new HashMap<String, String>();
//		LogUtils.LogE(TAG, "userId = " + userId + ",ppu = " + ppu);
//		params_map.put("userId", userId);
//		params_map.put("platform", "1");
//		params_map.put("deviceId", PhoneUtils.getIMEI(context));
//		params_map.put("token", PhoneUtils.getIMEI(context));
//		params_map.put("UDID", PhoneUtils.getIMEI(context));
//		params_map.put("platform", "1");
//		params_map.put("version", "2");
//		params_map.put("ppu", ppu);
//		t.configParams(params_map);
//		t.getDatas();
	}

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		return new CheckLoginModel(this, context);
	}

	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		Log.v(TAG,"onLoadingSuccess(NetBean bean, NetWorkModel model)===>");
		int status = bean.getStatus();
		if(status==0){
			Log.v(TAG,"onLoadingSuccess = " + bean.getData());
			callBack.onLoadingSuccess(JsonUtils.jsonToObject(bean.getData(), LoginBean.class));
		}else{
			String msg = bean.getMsg();
			if(!TextUtils.isEmpty(msg)){
				callBack.onLoadingError(bean.getMsg());
			}else{
				callBack.onLoadingError("连接失败!");
			}
		}
	}



}
;