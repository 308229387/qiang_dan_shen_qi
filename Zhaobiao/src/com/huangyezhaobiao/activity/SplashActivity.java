package com.huangyezhaobiao.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.log.LogInvocation;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.CommonUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.VersionUtils;
import com.huangyezhaobiao.vm.UpdateViewModel;
import com.wuba.loginsdk.external.LoginClient;

import air.com.wuba.bangbang.common.impush.DeamonService;

/**
 * 引导界面，每次都进入这个界面，闪屏3秒后会进入界面
 * ---第一次 guideActivity ---或者版本变化了 guideActivity
 * else
 * ---没登录 LoginActivity
 * else
 * ---登陆了--没绑定手机号 ---mobileValidateActivity
 * else
 * ---登录了--绑定了手机号--- MainActivity
 * @author shenzhixin
 * 
 */
public class SplashActivity extends Activity {
	private static final long DELAYED_TIMES = 3 * 1000;
	private SharedPreferences sp;
	private Handler handler = new Handler();
	private Context context;
	//private boolean isTime = false;//到3秒钟了
	// boolean isRegisterSuccess = false;//注册推送是否成功
	private View      rl_splash;
	private ImageView iv_splash;

//	private LoadingProgress loading;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rl_splash = findViewById(R.id.el_splash);
		iv_splash = (ImageView) findViewById(R.id.iv_splash);
		//个推的注册
		GePushProxy.initliazePush(this.getApplicationContext());
		//strictmode for develop
		/*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
				 ////打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
				.penaltyLog()
				.build());*/
		//strictmode
		EventbusAgent.getInstance().register(this);
		//判断网络，如果没有网络，就弹出说没有网络
		/*if(!NetworkTools.isNetworkConnected(this)){
			ToastUtils.makeImgAndTextToast(this,"没有网络，请开启网络后再次打开抢单神器", R.drawable.validate_error,0).show();
			return;
		}*/
		//注册推送并且fork子进程进行跟踪
		Log.e("szxhahaha", "uiuiuiuiui");

		Intent intent = new Intent(this, DeamonService.class);
		startService(intent);

		context = this;

		// 等三秒
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				goLogic();
			}
		}, DELAYED_TIMES);

		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		getWindow().setBackgroundDrawable(null);
		/** 启动Service */
//		if(!isMobServiceRunning("com.huangyezhaobiao.service.MobService")){
//			Intent mobService = new Intent(context, MobService.class);
//			mobService.putExtra("from","");
//			context.startService(mobService);
//		}

//		BiddingApplication.getBiddingApplication().addActivity(this);
	}


	private void goLogic(){
		sp = getSharedPreferences(Constans.APP_SP, 0);
		String currentVersionName = "1.0.0";
		try {
			currentVersionName = VersionUtils.getVersionName(context);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String mCurrentVersionName = currentVersionName.replace(".", "");


		String saveVersionName = sp.getString(Constans.VERSION_NAME,
				"1.0.0");
		if (!CommonUtils.compareTwoNumbersGuide(saveVersionName,
				mCurrentVersionName) ){//进入引导界面

			ActivityUtils.goToActivity(context, GuideActivity.class);

		} else if (TextUtils.isEmpty(UserUtils.getUserId(this))
				|| !UserUtils.isValidate(this)
				|| TextUtils.isEmpty(LoginClient.doGetPPUOperate(BiddingApplication.getAppInstanceContext()))
				) {//如果没有登录过
			ActivityUtils.goToActivity(context, BlankActivity.class);

		} else {//走主界面
			ActivityUtils.goToActivity(context, MainActivity.class);
		}

		sp.edit().putString(Constans.VERSION_NAME, currentVersionName).commit();
	}

	//	/**
//	 * 加载效果
//	 */
//	public void startLoading() {
//		if (loading == null) {
//			loading = new LoadingProgress(SplashActivity.this,
//					R.style.loading);
//		}
//		if(loading !=null && !this.isFinishing()){
//			loading.show();
//		}
//	}
//
//	/**
//	 * 对话框消失
//	 */
//	public void stopLoading() {
//		if (!this.isFinishing() && loading != null && loading.isShowing()) {
//			loading.dismiss();
//			loading = null;
//		}
//	}

	/** 判断Service是否在运行*/
	private boolean isMobServiceRunning(String serviceName) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}


	@Override
	protected void onPause() {
		super.onPause();
		BDMob.getBdMobInstance().onPauseActivity(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		BDMob.getBdMobInstance().onResumeActivity(BiddingApplication.getBiddingApplication());
		HYMob.getDataList(this, HYEventConstans.EVENT_ID_APP_OPEND);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventbusAgent.getInstance().unregister(this);
		rl_splash.setBackgroundResource(0);
		iv_splash.setImageResource(0);
		//销毁数据
		LogInvocation.destroy();

	}

	public void onEventMainThread(EventAction action){
		/*switch (action.type){
			case REGISTER_SUCCESS:
				isRegisterSuccess = true;
				if(isRegisterSuccess && isTime)
				{
					//ToastUtils.makeImgAndTextToast(this,"注册推送成功", R.drawable.validate_done,0).show();
					goLogic();
					isRegisterSuccess = false;
				}
				break;
			case REGISTER_FAILURE:
				isRegisterSuccess = true;
				ToastUtils.makeImgAndTextToast(this,"注册推送并没有成功，请重新尝试", R.drawable.validate_error,0).show();
				if(isRegisterSuccess && isTime)
				{
					//ToastUtils.makeImgAndTextToast(this,"注册推送成功", R.drawable.validate_done,0).show();
					goLogic();
					isRegisterSuccess = false;
				}
				break;
		}*/
	}


}
