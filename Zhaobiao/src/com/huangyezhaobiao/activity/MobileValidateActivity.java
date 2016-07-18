package com.huangyezhaobiao.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.ValidateViewModel;
import com.wuba.loginsdk.external.LoginClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机验证页面
 * 
 * @author linyueyang
 *
 */
public class MobileValidateActivity extends CommonBaseActivity implements NetWorkVMCallBack {
	private Button commit;
	private LinearLayout back_layout;
	private TextView txt_head;
	private EditText mobile;
	private EditText code;
	private Button getcode;
	private int countDown;
	private ValidateViewModel viewModel;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_valid);
		initView();
		initListener();
		viewModel = new ValidateViewModel(this,this);
		getWindow().setBackgroundDrawable(null);
	}


	public void initView() {
		back_layout = (LinearLayout) this.findViewById(R.id.back_layout);
		back_layout.setVisibility(View.VISIBLE);
		txt_head = (TextView) findViewById(R.id.txt_head);
		txt_head.setText(R.string.mobile_validate);

		commit = (Button) findViewById(R.id.commit);
		mobile = (EditText) findViewById(R.id.validate_mobile);
		code = (EditText) findViewById(R.id.validate_code);
		getcode = (Button) findViewById(R.id.validate_getcode);

	}

	@Override
	public void initListener() {
		getcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//获取验证码
				BDMob.getBdMobInstance().onMobEvent(MobileValidateActivity.this, BDEventConstans.EVENT_ID_MOBILE_BIND_PAGE_GETCODE);

				HYMob.getDataList(MobileValidateActivity.this, HYEventConstans.EVENT_ID_MOBILE_BIND_PAGE_GETCODE);

				String mobiletext = mobile.getText().toString();
				if (isMobile(mobiletext)) {
					viewModel.getCode(UserUtils.userId, mobiletext,false);
				} else {
					ToastUtils.makeImgAndTextToast(MobileValidateActivity.this,  getString(R.string.input_correct_mobile), R.drawable.validate_error, Toast.LENGTH_SHORT).show();
				}
			}
		});

		commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击了提交
				BDMob.getBdMobInstance().onMobEvent(MobileValidateActivity.this, BDEventConstans.EVENT_ID_MOBILE_BIND_PAGE_SUBMIT);

				HYMob.getDataList(MobileValidateActivity.this, HYEventConstans.EVENT_ID_MOBILE_BIND_PAGE_SUBMIT);

				String mobiletext = mobile.getText().toString();
				String codetext = code.getText().toString();
				if (!isMobile(mobiletext)) {
					ToastUtils.makeImgAndTextToast(MobileValidateActivity.this, getString(R.string.input_correct_mobile), R.drawable.validate_error, Toast.LENGTH_SHORT).show();
				}
				else if(!isCode(codetext)){
					ToastUtils.makeImgAndTextToast(MobileValidateActivity.this, getString(R.string.input_correct_validate), R.drawable.validate_error, Toast.LENGTH_SHORT).show();
				}
				else{
					viewModel.validate(UserUtils.userId, mobiletext, codetext);
				}
			}

		});
		
		back_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				//ActivityUtils.goToActivity(MobileValidateActivity.this, LoginActivity.class);
			}
		});

	}
	
	
	private void countdown(){
		countDown = 60;
        handler.postDelayed(runnable, 0);  
	}
	
	Handler handler = new Handler();  
    Runnable runnable = new Runnable() {  
		@Override  
        public void run() {  
        	countDown--;  
        	ColorStateList color;
            if(countDown>=0){
            	getcode.setClickable(false);
            	getcode.setText(countDown+"s后重新发送"); 
            	color = MobileValidateActivity.this.getResources().getColorStateList(R.color.whitedark);
            	getcode.setTextColor(color);
            	handler.postDelayed(this, 1000);  
            }
            else{
            	getcode.setClickable(true);
            	color = MobileValidateActivity.this.getResources().getColorStateList(R.color.red);
            	getcode.setTextColor(color);
             	getcode.setText(R.string.get_validate);
            	handler.removeCallbacks(runnable);
            }
            
            
        }  
    };  
	
	protected boolean isCode(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[0-9]{6}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	 }
                                                                                                                                                                                                                                                                                 	/**                
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}


	@Override
	public void onLoadingStart() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadingSuccess(Object t) {
		Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show();
		if (t instanceof String){
			String status = (String)t;
			if(status.equals("0")){
				ToastUtils.makeImgAndTextToast(MobileValidateActivity.this, getString(R.string.validate_code_already_send), R.drawable.validate_done, Toast.LENGTH_SHORT).show();
				countdown();
			}
			else if(status.equals("1")){
				ToastUtils.makeImgAndTextToast(MobileValidateActivity.this, getString(R.string.get_validate_exception_times), R.drawable.validate_done, Toast.LENGTH_SHORT).show();
			}
			else{
				ToastUtils.makeImgAndTextToast(MobileValidateActivity.this, getString(R.string.get_validate_exception), R.drawable.validate_done, Toast.LENGTH_SHORT).show();
			}
			
		}else if(t instanceof Map){
			Map<String, String> map = (Map<String, String>) t;
			String status = map.get("status");
			if(status.equals("0")){
				ActivityUtils.goToActivity(MobileValidateActivity.this, MainActivity.class);
				finish();
				UserUtils.hasValidate(this);
				MiPushClient.setAlias(MobileValidateActivity.this, UserUtils.getUserId(MobileValidateActivity.this), null);
			}
			else{
				ToastUtils.makeImgAndTextToast(MobileValidateActivity.this, map.get("msg"), R.drawable.validate_error, 0).show();
			}
			
		}
		
	}

	@Override
	public void onLoadingError(String msg) {

		if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
			super.onLoadingError(msg);
		}else if(!TextUtils.isEmpty(msg)){
			ToastUtils.makeImgAndTextToast(MobileValidateActivity.this, msg, R.drawable.validate_error, 0).show();
		}


	}

	@Override
	public void onLoadingCancel() {
		
	}

	@Override
	public void onNoInterNetError() {
		Toast.makeText(this,getString(R.string.no_network),Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoginInvalidate() {
		GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
		showExitDialog();
	}


	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_FIRST_BIND_MOBILE, stop_time - resume_time);
	}
}
