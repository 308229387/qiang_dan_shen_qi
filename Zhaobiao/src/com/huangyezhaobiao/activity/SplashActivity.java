package com.huangyezhaobiao.activity;

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

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.CommonUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.VersionUtils;

import air.com.wuba.bangbang.common.impush.DeamonService;

/**
 * 引导界面，每次都进入这个界面，闪屏3秒后会进入界面
 * ---第一次 guideActivity ---或者版本变化了 guideActivity
 * else
 * ---没登录 LoginActivity
 * else
 * ---登录了 MainActivity
 *
 * @author shenzhixin
 * 
 */
public class SplashActivity extends CommonBaseActivity {
	private static final long DELAYED_TIMES = 3 * 1000;
	private SharedPreferences sp;
	private Handler handler = new Handler();
	private Context context;
	//private boolean isTime = false;//到3秒钟了
	// boolean isRegisterSuccess = false;//注册推送是否成功
	private View      rl_splash;
	private ImageView iv_splash;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rl_splash = findViewById(R.id.el_splash);
		iv_splash = (ImageView) findViewById(R.id.iv_splash);
		//个推的注册
		GePushProxy.initliazePush(this.getApplicationContext());
		//strictmode for develop
		/*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog()
				 ////打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
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
				//isTime = true;
			//	if (isTime && isRegisterSuccess) {
					goLogic();
					//isTime = false;
			//	}
				return;
			}
		}, DELAYED_TIMES);

		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		getWindow().setBackgroundDrawable(null);
	}

	private void goLogic() {
		sp = getSharedPreferences(Constans.APP_SP, 0);
		String currentVersionName = "1.0.0";
		try {
            currentVersionName = VersionUtils.getVersionName(context);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
		String saveVersionName = sp.getString(Constans.VERSION_NAME,
                "1.0.0");
		String mCurrentVersionName = currentVersionName
                .replace(".", "");
		Log.e("ashenVersion", "cvN:" + currentVersionName);
		Log.e("ashenVersion",
                "isFirst:" + sp.getBoolean("isFirst", true));
		Log.e("lly",
				"isFirst:" + UserUtils.hasValidate);

		if (sp.getBoolean("isFirst", true) || !CommonUtils.compareTwoNumbersGuide(saveVersionName,
				mCurrentVersionName) ){//进入引导界面
            ActivityUtils.goToActivity(context, GuideActivity.class);
        }else if(TextUtils.isEmpty(UserUtils.getUserId(context))){//如果没有登录过
            ActivityUtils.goToActivity(context, LoginActivity.class);
        }else if(!UserUtils.isValidate(context)){//还未验证，走验证界面
            ActivityUtils.goToActivity(context, MobileValidateActivity.class);
        }else{//走主界面
            ActivityUtils.goToActivity(context, MainActivity.class);
        }
		//finish();
		sp.edit().putString(Constans.VERSION_NAME,
				currentVersionName).commit();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void initView() {

	}

	@Override
	public void initListener() {

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
