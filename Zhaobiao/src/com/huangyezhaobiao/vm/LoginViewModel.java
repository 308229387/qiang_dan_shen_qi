package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.PassportBean;
import com.huangyezhaobiao.model.LoginModel;
import com.huangyezhaobiao.utils.Encrypt;
import com.huangyezhaobiao.utils.PasswordEncrypt;
import com.huangyezhaobiao.utils.PhoneUtils;
import com.huangyezhaobiao.utils.UserUtils;

import java.util.HashMap;

public class LoginViewModel extends SourceViewModel{

	private static final String TAG = "LoginViewModel";
	/** added by chenguangming*/
	private Context context;
	private CheckLoginViewModel checkLoginViewModel;
	private NetWorkVMCallBack vmCallBack;

	private boolean isBackground;
	public LoginViewModel(NetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
		this.vmCallBack = callBack;
		this.context = context;
	}

	public void login(String param1,String param2,boolean isBackground){
		this.isBackground = isBackground;
		loginOldPassport(param1,param2,isBackground);
	}

	/** 存储在SharedPrefrences中的用户名和密码*/
	private String accountencrypt;
	private String username;
	/***
	 * 登录到旧版passport
	 * */
	private void loginOldPassport(String username,String password,boolean isBackground){
		// 设置请求方式:Post
		t.setRequestMethodPost();
		HashMap<String, String> params_map = new HashMap<String, String>();
		// 对密码加密
		String p3 = PasswordEncrypt.encryptPassword(password);
		this.username = username;
		params_map.put("username", username);
		if(!isBackground){
			this.accountencrypt = p3;
		} else {
			this.accountencrypt = password;
		}
		params_map.put("p3", accountencrypt);
		params_map.put("ctype", "2");
		// 获取设备号
		String mid = PhoneUtils.getIMEI(context);
		params_map.put("mid", mid);
		// 加密串
		String midSource = username + mid + "2" + "58V5";
		String vcode = Encrypt.MD532(midSource).substring(8, 16);
		params_map.put("vcode", vcode);
		params_map.put("source", "58app-android");
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
		PassportBean passportBean = (PassportBean) bean;
		checkLoginViewModel = new CheckLoginViewModel(callBack,context);
		/** 存userid ppu*/
		UserUtils.setPassportUserId(context,passportBean.getUserId());
		UserUtils.setPPU(context,passportBean.getPpu());
		UserUtils.setSessionTime(context,System.currentTimeMillis());
		UserUtils.setAccountName(context,username);
		UserUtils.setAccountEncrypt(context,accountencrypt);
		if(!isBackground){
			checkLoginViewModel.login();
		}

	}

	@Override
	public void onLoadingFailure(String err) {
		vmCallBack.onLoadingError(err);
	}
}