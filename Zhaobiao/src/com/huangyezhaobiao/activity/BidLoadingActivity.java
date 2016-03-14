package com.huangyezhaobiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.view.MyCustomDialog;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.KnockViewModel;

/**
 * 抢单等待界面
 * 
 * @author linyueyang
 *
 */
public class BidLoadingActivity extends QBBaseActivity {

	private TextView txt_head;
	private Button close;
	private LinearLayout back_layout;
	private ZhaoBiaoDialog dialog;
	private PushToPassBean passBean;
	
	private PushToPassBean receivePassBean;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bid_loading);
		Intent intent = this.getIntent();
		receivePassBean=(PushToPassBean)intent.getSerializableExtra("passBean");
		initView();
		initListener();
	}

	@Override
	public void initView() {
		layout_back_head = getView(R.id.layout_head);
		txt_head = (TextView) findViewById(R.id.txt_head);
		txt_head.setText(R.string.bidding_line);
		close = (Button) findViewById(R.id.loading_close);
		back_layout = (LinearLayout) this.findViewById(R.id.back_layout);
		tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);
	}

	@Override
	public void initListener() {
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		back_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	@Override
	public void onNotificationCome(PushBean pushBean) {
		if (null != pushBean) {
			int type = pushBean.getTag();
			if (type == 100 && StateUtils.getState(BidLoadingActivity.this) == 1) {
				MyCustomDialog dialog = MyCustomDialog.getInstance(this, new MyCustomDialog.OnCustomDialogListener() {
					// 点击抢单后的回调
					@Override
					public void back(PushToPassBean bean) {
						passBean = bean;
						kvm = new KnockViewModel(callBack, BidLoadingActivity.this);
						kvm.knock(bean, AppConstants.BIDSOURCE_WINDOW);
					}
				});
				dialog.show();
			} else if (type == 101) {
				passBean = pushBean.toPushPassBean();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("passBean", passBean);
				intent.putExtras(bundle);
				if (pushBean.toPushStorageBean().getStatus() == 1) {
					intent.setClass(BidLoadingActivity.this, BidSuccessActivity.class);
					startActivity(intent);
				} else {
					intent.setClass(BidLoadingActivity.this, BidFailureActivity.class);
					startActivity(intent);
				}
			} else {
				tbl.setPushBean(pushBean);
				tbl.setVisibility(View.VISIBLE);
			}
		}
	}



}
