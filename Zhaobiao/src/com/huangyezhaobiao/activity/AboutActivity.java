package com.huangyezhaobiao.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.VersionUtils;
import com.huangyezhaobiao.vm.UpdateViewModel;

import java.util.HashMap;

/**
 * 关于页面activity
 */
public class AboutActivity extends QBBaseActivity implements OnClickListener {
	private LinearLayout back_layout;//haha
	private TextView     txt_head,tv_version;
	private String name;
	private RelativeLayout rl_gongneng,rl_check_version;
	private RelativeLayout software_usage;
	private UpdateViewModel updateViewModel;  //版本更新
	/**
	 * 是否强制更新
	 */
	private boolean forceUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initView();
		initListener();
		updateViewModel = new UpdateViewModel(vmCallBack, this);
	}
	@Override
	public void initView() {
		layout_back_head = getView(R.id.layout_head);
		back_layout 	 = getView(R.id.back_layout);
		back_layout.setVisibility(View.VISIBLE);
		txt_head    	 = getView(R.id.txt_head);

		txt_head.setText(R.string.about);

		tv_version  	 = getView(R.id.tv_version);
		rl_gongneng      = getView(R.id.rl_gongneng);
		rl_check_version = getView(R.id.rl_check_version);
		tbl              = getView(R.id.tbl);
		software_usage   = getView(R.id.software_usage);
	}
	@Override
	public void initListener() {
		back_layout.setOnClickListener(this);
		rl_check_version.setOnClickListener(this);
		rl_gongneng.setOnClickListener(this);
		try {
			name = "v"+VersionUtils.getVersionName(this);
		} catch (NameNotFoundException e) {
			name = getString(R.string.error_get_versioncode);
			e.printStackTrace();
		}
		tv_version.setText(name);
		software_usage.setOnClickListener(this);

	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			onBackPressed();
			break;
		case R.id.rl_check_version://跳转到检查版本更新的界面
		 if(updateViewModel != null){
			 updateViewModel.checkVersion();
		 }
			break;
		case R.id.rl_gongneng://跳转到功能界面 都是h5;
			ActivityUtils.goToActivity(this, IntroduceFunctionActivity.class);
			HYMob.getDataList(this, HYEventConstans.EVENT_ID_INTRODUCTION);
			break;
			case R.id.software_usage://使用协议
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(AppConstants.H5_TITLE, getString(R.string.h5_login_softwareusage));
				map.put(AppConstants.H5_WEBURL, URLConstans.SOFTWARE_USEAGE_PROTOCOL);  //2016.5.3 add
				ActivityUtils.goToActivityWithString(this, SoftwareUsageActivity.class,map);
				break;
		}
	}

	private NetWorkVMCallBack vmCallBack = new NetWorkVMCallBack() {
		@Override
		public void onLoadingStart() {
			startLoading();
		}

		@Override
		public void onLoadingSuccess(Object t) {
			stopLoading();
		}

		@Override
		public void onLoadingError(String msg) {
			stopLoading();
		}

		@Override
		public void onLoadingCancel() {
			stopLoading();
		}

		@Override
		public void onNoInterNetError() {
			Toast.makeText(AboutActivity.this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onLoginInvalidate() {

		}

		@Override
		public void onVersionBack(String version) {
			stopLoading();
			String versionCode = "";
			int currentVersion = -1; //当前版本号

			int versionNum = -1;
			//获取当前系统版本号
			try {
				currentVersion = Integer.parseInt(VersionUtils.getVersionCode(AboutActivity.this));
			} catch (Exception e) {

			}
			if (currentVersion == -1) return;


			//当前是MainActivity，获取服务器header返回的版本号
			if (version != null) {

				if (version.contains("F")) {
					forceUpdate = true;
					String[] fs = version.split("F");
					versionCode = fs[0];
					try {
						versionNum = Integer.parseInt(versionCode);
					} catch (Exception e) {

					}
				}else{
					try {
						versionNum = Integer.parseInt(version);
					} catch (Exception e) {

					}
				}

				if (versionNum == -1) {
					return;
				}

				UpdateManager.getUpdateManager().isUpdateNow(AboutActivity.this, versionNum, currentVersion, URLConstans.DOWNLOAD_ZHAOBIAO_ADDRESS, forceUpdate);
				Boolean flag = UpdateManager.needUpdate;
				if (!flag) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AboutActivity.this, getString(R.string.already_new_version), Toast.LENGTH_SHORT).show();
						}
					});

				}

			}
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_ABOUT, stop_time - resume_time);
	}
}
