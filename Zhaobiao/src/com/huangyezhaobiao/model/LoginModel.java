package com.huangyezhaobiao.model;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.bean.PassportBean;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.lidroid.xutils.http.ResponseInfo;

import org.apache.http.Header;
import org.apache.http.HeaderElement;

public class LoginModel extends NetWorkModel{

	private static final String PASSPORT_RESULT0 = "0";

	private static final String TAG = LoginModel.class.getName();
	public LoginModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
		super(baseSourceModelCallBack, context);
	}

	@Override
	protected ZhaoBiaoRequest<String> createHttpRequest() {
		/** 登录旧版的passport */
		Log.v(TAG,"createHttpRequest()");
//		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_POST,"http://192.168.120.3:8193/pso/domclientunionlogin",this);//线下passport地址
		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_POST,"https://passport.58.com/pso/domclientunionlogin",this); //正式passport地址
		/** 登录新版的passport */
		// return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_POST,"https://passport.58.com/login/dologin",this);
	}

	@Override
	public void onLoadingSuccess(ResponseInfo<String> result) {
//		super.onLoadingSuccess(result);
		String resultCode = null;
		String resultMsg = null;
		try {
			jsonResult = JSON.parseObject(result.result);
			resultCode = jsonResult.getString("code");
			resultMsg = jsonResult.getString("errorMsg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(resultCode.equals(PASSPORT_RESULT0)){
			PassportBean passportBean = new PassportBean();
			passportBean.setCode(resultCode);
			passportBean.setErrorMsg(resultMsg);
			passportBean.setUserId(jsonResult.getString("userId"));
			Header[] headers = result.getAllHeaders();
			for (Header h:headers){
				if(h.getValue().startsWith("PPU")){
					HeaderElement[] headerElement = h.getElements();
					for (HeaderElement element:headerElement){
						passportBean.setPpu(element.getValue());
					}
					break;
				}
			}
			baseSourceModelCallBack.onLoadingSuccess(passportBean,this);
		} else if(resultCode.equals("9") || resultCode.equals("11")){
			resultMsg = "您的账户存在异常，请至58同城网页登录并验证您的账号后，才能继续登录抢单神器。";
			baseSourceModelCallBack.onLoadingFailure(resultMsg);
		} else {
			baseSourceModelCallBack.onLoadingFailure(resultMsg);
		}
	}

}
