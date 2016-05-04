package com.huangyezhaobiao.activity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.LoginEditFilter;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.LoginBean;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.AnimationController;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.PhoneUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.LoadingProgress;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;
import com.huangyezhaobiao.vm.CheckLoginViewModel;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.HashMap;

/**
 * 登陆页面
 * @author linyueyang
 *
 */
public class LoginActivity extends CommonBaseActivity implements NetWorkVMCallBack, onDialogClickListener {
	private EditText username;
	private EditText password;
	private ImageButton nCloseBtn;
	private ImageButton pCloseBtn;
	private CheckBox    cb_usage;
	private TextView    tv_accept_text_usage;

	private TextView tv_how_to_become_vip;
	private Button loginbutton;
	private View        ll_root;
	private ImageView   iv_icon;
//	private LoginViewModel loginViewModel;
	private CheckLoginViewModel checkLoginViewModel;
	private ZhaoBiaoDialog dialog;
	private LoadingProgress loading;
	private AnimationController animationController;
	private String userName;
	private TextInputLayout textInputLayout_username;
	private TextInputLayout textInputLayout_password;

	private int hasValidated;
	private static final String OLD_PASSPORT = "oldpassport";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		EventbusAgent.getInstance().register(this);
		initView();
		initListener();
//		loginViewModel = new LoginViewModel(this, this);
		checkLoginViewModel = new CheckLoginViewModel(this, this);
		dialog = new ZhaoBiaoDialog(this, "提示", "登录失败，您输入的账户名和密码不符!");
		dialog.setCancelButtonGone();
		dialog.setOnDialogClickListener(this);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		getWindow().setBackgroundDrawable(null);
		//关掉service
		stopService(new Intent(LoginActivity.this, MyService.class));
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
		username = textInputLayout_username.getEditText();
		password = textInputLayout_password.getEditText();
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new LoginEditFilter(this,"");
		username.setFilters(filters);
		password.setFilters(filters);
		if(!TextUtils.isEmpty(UserUtils.getUserName(LoginActivity.this)))
		{
			username.setText(UserUtils.getUserName(LoginActivity.this));
		}
		//	password = (EditText) findViewById(R.id.password);
		//	username.clearFocus();
		nCloseBtn = (ImageButton) findViewById(R.id.nCloseBtn);
		pCloseBtn = (ImageButton) findViewById(R.id.pCloseBtn);
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
				BDMob.getBdMobInstance().onMobEvent(LoginActivity.this,BDEventConstans.EVENT_ID_LOGIN);
				String name = username.getText().toString();
				userName = name;
				String passwords = password.getText().toString();
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
				if(!cb_usage.isChecked()){
					dialog.setMessage("接受抢单神器协议方可使用软件!");
					dialog.show();
					return;
				}
				startLoading();
				//test
				//name = "琼nl";
				//passwords = "qwer123";
				//test
//				loginViewModel.login(name, passwords);
				checkLoginViewModel.login(name, passwords);
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

	//网络层回调

	@Override
	public void onLoadingStart() {
		startLoading();
	}

	private static final String TAG ="LoginActivity.onLoadingSuccess";
	@Override
	public void onLoadingSuccess(Object t) {
		stopLoading();
		if (t instanceof LoginBean) {
			LoginBean loginBean = (LoginBean)t;
			long userId = loginBean.getUserId();
			String companyName = loginBean.getCompanyName();
			 hasValidated =  loginBean.getHasValidated();
			UserUtils.saveUser(this, userId + "", companyName, userName);
			// 用于测试，写死数据"24454277549825",实际用UserUtils.getUserId(LoginActivity.this)
			MiPushClient.setAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
			//个推注册别名
			boolean result = GePushProxy.bindPushAlias(getApplicationContext(),userId+"_"+ PhoneUtils.getIMEI(this));
			Toast.makeText(this,"注册别名结果:"+result,Toast.LENGTH_SHORT).show();
			startLoading();
			//判断是否验证过手机
			if(hasValidated==1) {
				ActivityUtils.goToActivity(LoginActivity.this, MobileValidateActivity.class);
			}
			else{
				UserUtils.hasValidate(getApplicationContext());
				ActivityUtils.goToActivity(LoginActivity.this, MainActivity.class);
			}
			finish();
		}
	}

	@SuppressLint("ShowToast")
	@Override
	public void onLoadingError(String msg) {

		stopLoading();
		//TODO:判断一下是不是在当前界面
		try {
			if(dialog!=null && !TextUtils.isEmpty(msg)){
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
		Toast.makeText(this, getString(R.string.no_network),Toast.LENGTH_SHORT).show();
		stopLoading();
	}

	@Override
	public void onLoginInvalidate() {
		stopLoading();
		Toast.makeText(this, getString(R.string.login_login_invalidate),Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onDialogOkClick() {
		dialog.dismiss();
	}

	@Override
	public void onDialogCancelClick() {
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(dialog!=null && dialog.isShowing()){
			dialog.dismiss();
			dialog = null;
		}
		if(loading!=null){
			loading = null;
		}
		EventbusAgent.getInstance().unregister(this);
		releaseSources();
	}

	private void releaseSources() {
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
			loading = new LoadingProgress(LoginActivity.this,
					R.style.loading);
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
}
