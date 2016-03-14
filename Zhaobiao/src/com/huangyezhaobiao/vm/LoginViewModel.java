package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.LoginBean;
import com.huangyezhaobiao.model.LoginModel;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.PasswordEncrypt;
import com.huangyezhaobiao.utils.PhoneUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

public class LoginViewModel extends SourceViewModel{

	public LoginViewModel(NetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
		// TODO Auto-generated constructor stub
	}

	
	public void login(String username,String password){
		t.setRequestMethodPost();
		HashMap<String, String> params_map = new HashMap<String, String>();
		//password=admin&userName=admin&platform=android&deviceId=211&token=111
		//http://192.168.118.41/app/order/bidding?bidid=3089019022954856539&userid=24454277549825&pushid=3089020612659380315&pushturn=1&token=1438437884399
		LogUtils.LogE("ashenlogin", "username:" + username + ",password:" + password);
		try {
			String userName = URLEncoder.encode(username, "utf-8");
			String userName1 = URLEncoder.encode(userName,"utf-8");
			params_map.put("password", PasswordEncrypt.encryptPassword(password));
			LogUtils.LogE("ashenlogin", "username after :" + userName1);
			params_map.put("userName", userName1);
			params_map.put("platform", "1");
			params_map.put("deviceId", PhoneUtils.getIMEI(context));
			params_map.put("token", new Date().getTime() + "");
			params_map.put("UUID", PhoneUtils.getIMEI(context));
			params_map.put("platform","1");
			params_map.put("version","2");
			t.configParams(params_map);
			t.getDatas();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//?password=admin&userName=admin&platform=android&deviceId=123&token=123
	}
	
	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		
		return new LoginModel(this, context);
	}
	
	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		int status = bean.getStatus();
		if(status==0){
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
