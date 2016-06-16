package com.huangyezhaobiao.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.LoginEditFilter;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.LoginBean;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.log.LogInvocation;
import com.huangyezhaobiao.model.LoginModel;
import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.AnimationController;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.PhoneUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.VersionUtils;
import com.huangyezhaobiao.view.LoadingProgress;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;
import com.huangyezhaobiao.vm.LoginViewModel;
import com.huangyezhaobiao.vm.UpdateViewModel;
import com.lidroid.xutils.BitmapUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.Date;
import java.util.HashMap;

/**
 * 登陆页面
 * @author linyueyang
 *
 */
public class LoginActivity extends CommonBaseActivity implements NetWorkVMCallBack, onDialogClickListener {
	private EditText username;
	private EditText password;
	private EditText verifycode;
	private ImageButton nCloseBtn;
	private ImageButton pCloseBtn;
	private ImageView verifyCodeBtn;

	private CheckBox    cb_usage;
	private TextView    tv_accept_text_usage;

	private TextView tv_how_to_become_vip;
	private Button loginbutton;
	private View        ll_root;
	private ImageView   iv_icon;
	private LoginViewModel loginViewModel;
	private ZhaoBiaoDialog dialog;
	private LoadingProgress loading;
	private AnimationController animationController;
	private String userName;
	private TextInputLayout textInputLayout_username;
	private TextInputLayout textInputLayout_password;
	private TextInputLayout textInputLayout_verifyCode;

	private RelativeLayout rl_login_vcode;

	private BitmapUtils  bitmapUtils;

	/**
	 * 是否需要输入验证码
	 */
	Boolean flag = false;

	private int hasValidated;
	private static final String OLD_PASSPORT = "oldpassport";
	public static LoginActivity loginInstance;

	/**
	 * 是否强制更新
	 */
	private boolean forceUpdate;

	private ZhaoBiaoDialog updateMessageDialog;

	private UpdateViewModel updateViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
//		loginInstance = this;
//		EventbusAgent.getInstance().register(this);

		getWindow().setBackgroundDrawable(null);
		//关掉service
		stopService(new Intent(LoginActivity.this, MyService.class));

		bitmapUtils = new BitmapUtils(LoginActivity.this);
		bitmapUtils.configDefaultLoadingImage(R.drawable.vcode_failure);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.vcode_failure);

		updateViewModel = new UpdateViewModel(this, this);


	}

	@Override
	protected void onResume() {
		super.onResume();
		initView();
		initListener();
		loginViewModel = new LoginViewModel(this, this);
		dialog = new ZhaoBiaoDialog(this, "提示", "登录失败，您输入的账户名和密码不符!");
		dialog.setCancelButtonGone();
		dialog.setOnDialogClickListener(this);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
       if(updateViewModel != null)
		updateViewModel.checkVersion();
	}

	//百度统计
	@Override
	public void initView() {
		ll_root					= findViewById(R.id.ll_root);
		iv_icon 				= (ImageView) findViewById(R.id.iv_icon);
		textInputLayout_username = (TextInputLayout) findViewById(R.id.textInputLayout_username);
		//username = (EditText) findViewById(R.id.username);
		//username = til_username.getEditText();
		textInputLayout_username.setHint("用户名/邮箱/手机号");

		textInputLayout_password = (TextInputLayout) findViewById(R.id.textInputLayout_password);
		textInputLayout_password.setHint("请输入密码");

		textInputLayout_verifyCode = (TextInputLayout) findViewById(R.id.textInputLayout_verifyCode);
		textInputLayout_verifyCode.setHint("请输入验证码");

		rl_login_vcode = (RelativeLayout) findViewById(R.id.rl_login_vcode);

		username = textInputLayout_username.getEditText();
		password = textInputLayout_password.getEditText();
		verifycode = textInputLayout_verifyCode.getEditText();

		InputFilter[] filters = new InputFilter[1];
		filters[0] = new LoginEditFilter(this,"");
		username.setFilters(filters);
		password.setFilters(filters);
		verifycode.setFilters(filters);

		if(!TextUtils.isEmpty(UserUtils.getUserName(LoginActivity.this))) {
			username.setText(UserUtils.getUserName(LoginActivity.this));
		}
		//	password = (EditText) findViewById(R.id.password);
		//	username.clearFocus();
		nCloseBtn = (ImageButton) findViewById(R.id.nCloseBtn);
		pCloseBtn = (ImageButton) findViewById(R.id.pCloseBtn);
		verifyCodeBtn = (ImageView) findViewById(R.id.verifyCodeBtn);

		loginbutton = (Button) findViewById(R.id.loginbutton);
		//	userIcon = (ImageView) findViewById(R.id.userIcon);
		//passwordIcon = (ImageView) findViewById(R.id.passwordIcon);
		animationController = new AnimationController();
		cb_usage   			 = (CheckBox) findViewById(R.id.cb_usage);
		tv_accept_text_usage = (TextView) findViewById(R.id.tv_accept_text_usage);
		tv_accept_text_usage.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tv_how_to_become_vip = (TextView) findViewById(R.id.tv_login_raiders);
		tv_how_to_become_vip.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}

	public static String appendUrlTimestamp(String url) {
		long timestamp = new Date().getTime();
		String newUrl;
		if (url.indexOf("?") > -1) {
			newUrl = url + "&tamp=" + timestamp;
		} else {
			newUrl = url + "?timestamp=" + timestamp;
		}
		return newUrl;
	}

	@Override
	public void initListener() {
		nCloseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				username.setText("");
			}
		});
		pCloseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				password.setText("");
			}
		});

		verifyCodeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getVerifyCode();
			}
		});


		/**
		 * 抢单神器使用协议start
		 * edited by chenguangming
		 */
		tv_accept_text_usage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(AppConstants.H5_TITLE, getString(R.string.h5_login_softwareusage));
//				map.put(AppConstants.H5_WEBURL, URLConstans.SOFTWARE_USAGE);
				map.put(AppConstants.H5_WEBURL, URLConstans.SOFTWARE_USEAGE_PROTOCOL);  //2016.5.3 add
				ActivityUtils.goToActivityWithString(LoginActivity.this, SoftwareUsageActivity.class, map);
//				ActivityUtils.goToActivity(LoginActivity.this,SoftwareUsageActivity.class);
			}
		});

		/**
		 * 抢单神器使用协议end
		 * edited by chenguangming
		 */

		/**
		 * 申请成为抢单神器会员start
		 * created by chenguangming
		 */
		tv_how_to_become_vip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BDMob.getBdMobInstance().onMobEvent(LoginActivity.this, BDEventConstans.EVENT_ID_BECOME_TO_VIP);

				HYMob.getDataList(LoginActivity.this, HYEventConstans.EVENT_ID_BECOME_TO_VIP);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put(AppConstants.H5_TITLE, getString(R.string.h5_login_raiders));
//				map.put(AppConstants.H5_WEBURL, URLConstans.HOW_TO_BECOME_VIP);
				map.put(AppConstants.H5_WEBURL, URLConstans.HOW_TO_BECOME_VIP_MEMBER); //2016.5.2 add
				ActivityUtils.goToActivityWithString(LoginActivity.this, SoftwareUsageActivity.class, map);
//				ActivityUtils.goToActivity(LoginActivity.this,SoftwareUsageActivity.class);
			}
		});
		/**
		 * 申请成为抢单神器会员end
		 * created by chenguangming
		 */

		loginbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//登录按钮
				BDMob.getBdMobInstance().onMobEvent(LoginActivity.this, BDEventConstans.EVENT_ID_LOGIN);

				String name = username.getText().toString();
				userName = name;

				String passwords = password.getText().toString();

				String verifyCodes = verifycode.getText().toString();
				//需要加密
				if (TextUtils.isEmpty(name)) {
					dialog.setMessage("请输入用户名！");
					dialog.show();
					return;
				}
				if (TextUtils.isEmpty(passwords)) {
					dialog.setMessage("请输入密码!");
					dialog.show();
					return;
				}

				if (flag && TextUtils.isEmpty(verifyCodes)) {
					dialog.setMessage("请输入验证码!");
					dialog.show();
					return;
				}
				if(!cb_usage.isChecked()){
					dialog.setMessage("接受抢单神器协议方可使用软件!");
					dialog.show();
					return;
				}
				startLoading();

				if (loginViewModel != null){
					loginViewModel = null;
					System.gc();
				}
				loginViewModel = new LoginViewModel(LoginActivity.this, LoginActivity.this);
				try {
					if(flag){
						loginViewModel.loginOldPassport(name, passwords, verifyCodes);
					}else{
						loginViewModel.loginOldPassport(name, passwords);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		username.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				if (!TextUtils.isEmpty(username.getText().toString())) {
					if (nCloseBtn.getVisibility() != View.VISIBLE)
						animationController.fadeIn(nCloseBtn, 1000, 0);
				} else {
					animationController.fadeOut(nCloseBtn, 1000, 0);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
		});

		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				if (!TextUtils.isEmpty(password.getText().toString())) {
					if (pCloseBtn.getVisibility() != View.VISIBLE)
						animationController.fadeIn(pCloseBtn, 1000, 0);
				} else {
					animationController.fadeOut(pCloseBtn, 1000, 0);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
		});
	}

	private void getVerifyCode(){

		String vCodeKey = LoginModel.vcodekey;
		String url = "";
		if (vCodeKey != null) {
			url = "http://passport.58.com/validcode/get?" + UrlSuffix.getVerifyCodeSuffix("hyzb-android", vCodeKey);
		}
		bitmapUtils.display(verifyCodeBtn, appendUrlTimestamp(url));
	}

	//网络层回调
	@Override
	public void onLoadingStart() {
		//startLoading();
	}

	private static final String TAG ="LoginActivity.onLoadingSuccess";
	@Override
	public void onLoadingSuccess(Object t) {
		stopLoading();

		if (t instanceof LoginBean) {
			Log.e("shenss","login...");
			LoginBean loginBean = (LoginBean)t;
			long userId = loginBean.getUserId();
			String companyName = loginBean.getCompanyName();
			 hasValidated =  loginBean.getHasValidated();
			UserUtils.saveUser(this, userId + "", companyName, userName);

			HYMob.getDataListByLoginSuccess(LoginActivity.this, HYEventConstans.EVENT_ID_LOGIN,"1", userName);

			// 用于测试，写死数据"24454277549825",实际用UserUtils.getUserId(LoginActivity.this)
			MiPushClient.setAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
			//个推注册别名
			boolean result = GePushProxy.bindPushAlias(getApplicationContext(),userId+"_"+ PhoneUtils.getIMEI(this));
			Toast.makeText(this,"注册别名结果:"+result,Toast.LENGTH_SHORT).show();

			 //判断是否验证过手机
			 if(hasValidated==1) {
				 ActivityUtils.goToActivity(LoginActivity.this, MobileValidateActivity.class);
			 } else{
				 UserUtils.hasValidate(getApplicationContext());
				 ActivityUtils.goToActivity(LoginActivity.this, MainActivity.class);
				 // added by chenguangming
				 finish();
			 }

			// finish();
		}
	}

	@SuppressLint("ShowToast")
	@Override
	public void onLoadingError(String msg) {
		stopLoading();
		//TODO:判断一下是不是在当前界面
		try {

			HYMob.getDataListByLoginError(LoginActivity.this, HYEventConstans.EVENT_ID_LOGIN, "0",msg);

			if(dialog!=null &&!TextUtils.isEmpty(msg) && msg.equals("785")) {
				flag = true;

				String resultMsg = "请输入正确的验证码";

				dialog.setMessage(resultMsg);
				dialog.show();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						rl_login_vcode.setVisibility(View.VISIBLE);
						String vCodeKey = LoginModel.vcodekey;
						String url = "";
						if (vCodeKey != null) {
							url = "http://passport.58.com/validcode/get?" + UrlSuffix.getVerifyCodeSuffix("hyzb-android", vCodeKey);
						}
						bitmapUtils.display(verifyCodeBtn, url);
					}
				});
			}else if(dialog!=null && !TextUtils.isEmpty(msg) && msg.equals("786")){
				HYMob.getDataListByLoginError(LoginActivity.this, HYEventConstans.EVENT_ID_LOGIN, "0",msg);
				String resultMsg = "图片验证码输入错误";

				dialog.setMessage(resultMsg);
				dialog.show();
				getVerifyCode();

			}else if(dialog!=null && !TextUtils.isEmpty(msg)){

				dialog.setMessage(msg);
				dialog.show();
			}
		} catch (Exception e) {
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLoadingCancel() {
		stopLoading();
	}

	@SuppressLint("ShowToast")
	@Override
	public void onNoInterNetError() {
		Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		stopLoading();
	}

	@Override
	public void onLoginInvalidate() {
		stopLoading();
		Toast.makeText(this, getString(R.string.login_login_invalidate),Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onDialogOkClick() {
		if(dialog!=null && dialog.isShowing()&&!LoginActivity.this.isFinishing()){
			dialog.dismiss();
		}
	}

	@Override
	public void onDialogCancelClick() {
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		// added by chenguangming
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(dialog!=null && dialog.isShowing() &&!LoginActivity.this.isFinishing() ){
			dialog.dismiss();
			dialog = null;
		}
		if(loading!=null){
			loading = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if(dialog!=null && dialog.isShowing()){
//			dialog.dismiss();
//			dialog = null;
//		}
//		if(loading!=null){
//			loading = null;
//		}
//		EventbusAgent.getInstance().unregister(this);
		releaseSources();
	}

	private void releaseSources() {
		//销毁数据
		LogInvocation.destroy();
		//销毁数据
		nCloseBtn.setBackgroundResource(0);
		pCloseBtn.setBackgroundResource(0);
		cb_usage.setBackgroundResource(0);
		loginbutton.setBackgroundResource(0);
		iv_icon.setImageResource(0);
		ll_root.setBackgroundResource(0);
	}


	/**
	 * 加载效果
	 */
	public void startLoading() {
		if (loading == null) {
			loading = new LoadingProgress(LoginActivity.this, R.style.loading);
		}
		if(loading!=null && !this.isFinishing())
			loading.show();
	}

	/**
	 * 对话框消失
	 */
	public void stopLoading() {
		if (!LoginActivity.this.isFinishing() && loading != null && loading.isShowing()) {
			loading.dismiss();
			loading = null;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 *
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = {0, 0};
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}


		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 *
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}



	public void onEventMainThread(EventAction action){
		stopLoading();
		switch (action.type){
			case ALIAS_SET_SUCCESS://别名注册成功
				//ToastUtils.makeImgAndTextToast(getApplicationContext(), "别名设置成功", R.drawable.validate_done,1).show();
				break;
			case ALIAS_SET_FAILURE://别名注册失败

			//	ToastUtils.makeImgAndTextToast(getApplicationContext(), "别名没有设置成功，请重新登录，否则收不到推送", R.drawable.validate_error, 1).show();
				break;
		}


	}

//	/**
//	 * 第一次登录时的提示
//	 */
//	private void showFirst() {
////      if(!SPUtils.getAppUpdate(this)){
//		if ( SPUtils.isFirstUpdate(this)) {//需要弹窗
//			updateMessageDialog = new ZhaoBiaoDialog(this, getString(R.string.update_hint), getString(R.string.update_message));
////			updateMessageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
////				@Override
////				public void onDismiss(DialogInterface dialog) {
////					//弹自定义界面的弹
////					if (SPUtils.isAutoSetting(LoginActivity.this)) {
////						autoSettingDialog.show();
////					}
////				}
////			});
//			updateMessageDialog.setCancelable(false);
//			updateMessageDialog.setCancelButtonGone();
//			updateMessageDialog.setOnDialogClickListener(new onDialogClickListener() {
//				@Override
//				public void onDialogOkClick() {
//					updateMessageDialog.dismiss();
//					SPUtils.saveAlreadyFirstUpdate(LoginActivity.this, false);
////                    UserUtils.setAppVersion(MainActivity.this, ""); //2.7升级可删
////                       SPUtils.setAppUpdate(MainActivity.this, true);
//				}
//
//				@Override
//				public void onDialogCancelClick() {
//				}
//			});
//			updateMessageDialog.show();
//
//		}
////		else {
////			if (SPUtils.isAutoSetting(this)) {
////				autoSettingDialog.show();
////			}
////		}
//
//	}

	public void onVersionBack(String version) {
		String versionCode = "";
		Log.e("shenyy", "MainActivity version:" + version);
		int currentVersion = -1; //当前版本号

		int versionNum = -1;
		//获取当前系统版本号
		try {
			currentVersion = Integer.parseInt(VersionUtils.getVersionCode(this));
		} catch (Exception e) {

		}
		if (currentVersion == -1) return;


		//当前是MainActivity，获取服务器header返回的版本号
		if (version != null) {
			if (version.contains("F")) {
				forceUpdate = true;
			}
			String[] fs = version.split("F");
			versionCode = fs[0];
			try {
				versionNum = Integer.parseInt(versionCode);
			} catch (Exception e) {

			}
			if (versionNum == -1) {
				return;
			}

			UpdateManager.getUpdateManager().isUpdateNow(this, versionNum, currentVersion, URLConstans.DOWNLOAD_ZHAOBIAO_ADDRESS, forceUpdate);
//          UpdateManager.getUpdateManager().isUpdateNow(this, versionNum, currentVersion, "http://10.252.23.45:8001/2.7.0_zhaobiao.apk", forceUpdate);
//			Boolean flag = UpdateManager.needUpdate;
//			Log.v("www", "flag:" + flag);
//			if(!flag){
//				//判断是不是第一次进入主界面
//				showFirst();
//			}


		}
	}
}
