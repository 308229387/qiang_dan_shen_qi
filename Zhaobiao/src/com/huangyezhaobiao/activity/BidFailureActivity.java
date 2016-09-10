package com.huangyezhaobiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;

/**
 * 抢单失败界面
 * @author linyueyang
 *
 */
public class BidFailureActivity extends QBBaseActivity{

	private LinearLayout back_layout;
	private TextView txt_head;
	private Button toBidList;
	private PushToPassBean receivePassBean;

	private String bidId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bid_failure);
		Intent intent = this.getIntent(); 
		receivePassBean=(PushToPassBean)intent.getSerializableExtra("passBean");

		if (receivePassBean != null ) {
			bidId = String.valueOf(receivePassBean.getBidId());
		}
		HYMob.getDataListByQiangDan(BidFailureActivity.this, HYEventConstans.EVENT_ID_FAILURE_PAGE, bidId);

		initView();
		initListener();
	}

	@Override
	public void initView() {
		layout_back_head = getView(R.id.layout_head);
		back_layout = (LinearLayout) this.findViewById(R.id.back_layout);
		back_layout.setVisibility(View.VISIBLE);
		txt_head = (TextView) findViewById(R.id.txt_head);
		txt_head.setText("抢单失败");

		toBidList = (Button) findViewById(R.id.failure_bidlist);
		tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);
	}

	@Override
	public void initListener() {
		back_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		toBidList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtils.goToActivity(BidFailureActivity.this, MainActivity.class);
				MDUtils.QDResult(MDConstans.BID_FAILURED, receivePassBean.getCateId()+"", receivePassBean.getBidId()+"", MDConstans.ACTION_CONTINUE_QD);
			}
		});
	}


	@Override
	public void finish() {
		super.finish();
		MDUtils.QDResult(MDConstans.BID_FAILURED, receivePassBean.getCateId()+"", receivePassBean.getBidId()+"", MDConstans.ACTION_CLOSE_RESULT);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
