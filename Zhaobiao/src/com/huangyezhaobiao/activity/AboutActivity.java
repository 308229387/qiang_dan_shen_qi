package com.huangyezhaobiao.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.VersionUtils;
/**
 * 关于页面activity
 */
public class AboutActivity extends QBBaseActivity implements OnClickListener {
	private LinearLayout back_layout;//haha
	private TextView     txt_head,tv_version;
	private String name;
	private RelativeLayout rl_gongneng,rl_check_version;
	private RelativeLayout software_usage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);
		initView();
		initListener();
	}
	@Override
	public void initView() {
		layout_back_head = getView(R.id.layout_head);
		back_layout 	 = getView(R.id.back_layout);
		txt_head    	 = getView(R.id.txt_head);
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
		txt_head.setText(R.string.about);
		try {
			name = getString(R.string.version_code)+VersionUtils.getVersionName(this);
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
			Toast.makeText(this, getString(R.string.already_new_version), Toast.LENGTH_SHORT).show();

			break;
		case R.id.rl_gongneng://跳转到功能界面 都是h5;
			ActivityUtils.goToActivity(this, IntroduceFunctionActivity.class);
			break;
			case R.id.software_usage://使用协议
				ActivityUtils.goToActivity(this, SoftwareUsageActivity.class);
				break;
		}
	}

}
