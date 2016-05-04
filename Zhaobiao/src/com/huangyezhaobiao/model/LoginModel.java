package com.huangyezhaobiao.model;

import android.content.Context;
import android.util.Log;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.bean.PassportBean;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.lidroid.xutils.http.ResponseInfo;

import org.apache.http.Header;
import org.apache.http.HeaderElement;

public class LoginModel extends NetWorkModel{

	private static final String TAG = LoginModel.class.getName();
	public LoginModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
		super(baseSourceModelCallBack, context);
	}
	
	@Override
	protected ZhaoBiaoRequest<String> createHttpRequest() {
		/** 登录旧版的passport */
		Log.v(TAG,"createHttpRequest()");
		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_POST,"http://192.168.120.3:8193/pso/domclientunionlogin",this);
		/** 登录新版的passport */
		// return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_POST,"https://passport.58.com/login/dologin",this);
	}

	@Override
	public void onLoadingSuccess(ResponseInfo<String> result) {
		super.onLoadingSuccess(result);
		Log.v(TAG,"onLoadingSuccess = " + jsonResult);

		PassportBean passportBean = new PassportBean();
		passportBean.setCode(jsonResult.getString("code"));
		passportBean.setErrorMsg(jsonResult.getString("errorMsg"));
		passportBean.setUserId(jsonResult.getString("userId"));

		Header[] headers = result.getAllHeaders();
		for (Header h:headers){
			if(h.getValue().startsWith("PPU")){
				HeaderElement[] headerElement = h.getElements();
				for (HeaderElement element:headerElement){
					Log.v(TAG,element.getValue());
					passportBean.setPpu(element.getValue());
				}
				break;
			}
		}
		baseSourceModelCallBack.onLoadingSuccess(passportBean,this);
	}
}
