package com.huangyezhaobiao.vm;

import android.content.Context;
import android.util.Log;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.PassportBean;
import com.huangyezhaobiao.model.LoginModel;
import com.huangyezhaobiao.utils.Encrypt;
import com.huangyezhaobiao.utils.PasswordEncrypt;
import com.huangyezhaobiao.utils.PhoneUtils;

import java.util.HashMap;

public class LoginViewModel extends SourceViewModel{

	private static final String TAG = "LoginViewModel";
	/** added by chenguangming*/
	private Context context;
	private CheckLoginViewModel checkLoginViewModel;
	public LoginViewModel(NetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
		this.context = context;
	}

	public void login(String param1,String param2){
		loginOldPassport(param1,param2);
	}
	String password;
	String username;
	/***
	 * 登录到旧版passport
	 * */
	private void loginOldPassport(String username,String password){
		Log.v(TAG,"loginOldPassport.................");
		// 设置请求方式:Post
		t.setRequestMethodPost();
		HashMap<String, String> params_map = new HashMap<String, String>();
		// 对密码加密
		String p3 = PasswordEncrypt.encryptPassword(password);
		this.username = username;
		this.password = p3;
		params_map.put("username", username);
		params_map.put("p3", password);
		params_map.put("ctype", "2");
		// 获取设备号
		String mid = PhoneUtils.getIMEI(context);
		params_map.put("mid", mid);
		// 加密串
		String midSource = username + mid + "2" + "58V5";
		String vcode = Encrypt.MD532(midSource).substring(8, 16);
		params_map.put("vcode", vcode);
		// 公钥版本
		params_map.put("rsakeyversion", "1");
		// 加密类型
		params_map.put("vptype", "RSA2");
		// 登录类型标识，0表示默认由passport判断登录方式，1表示用户名登录，2表示认证手机登录，3表示邮箱登录
		params_map.put("loginflag", "0");
		t.configParams(params_map);
		t.getDatas();
	}


	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		return new LoginModel(this, context);
	}

	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		Log.v(TAG,"onLoadingSuccess(NetBean bean, NetWorkModel model)===>" + bean);
		PassportBean passportBean = (PassportBean) bean;
		checkLoginViewModel = new CheckLoginViewModel(callBack,context);
		checkLoginViewModel.login(passportBean.getUserId(),passportBean.getPpu());
//		checkLoginViewModel.login(username,password);
	}



}
;