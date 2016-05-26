package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.TelephoneBean;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.mediator.RefundMediator;
import com.huangyezhaobiao.presenter.FetchDetailsPresenter;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.vm.FetchDetailsVM;
import com.huangyezhaobiao.vm.TelephoneVModel;

import java.util.ArrayList;

/**
 * 抢单详情
 * @author shenzhixin
 *
 */
public class FetchDetailsActivity extends QBBaseActivity implements
		OnClickListener, NetWorkVMCallBack {
	public static String orderState;
	private FetchDetailsVM vm;
	private FetchDetailsPresenter fetchDetailsPresenter;
	private LinearLayout ll;
	private LinearLayout back_layout;
	private TextView txt_head;
	private String orderId;
	private String type;
	private TelephoneVModel tvm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fetch_details);
		fetchDetailsPresenter = new FetchDetailsPresenter(this);
		orderId = getIntent().getStringExtra(Constans.ORDER_ID);
		EventbusAgent.getInstance().register(this);
		initView();
		initListener();
		if(TextUtils.isEmpty(orderId)){//orderId如果为空
			vm = new FetchDetailsVM(this, this, 0l);
		}else{
			vm = new FetchDetailsVM(this, this, Long.parseLong(orderId));
		}
		initTeleVModel();

	}

	private void initTeleVModel() {
		tvm = new TelephoneVModel(null,this);
	}

	public void initListener() {
		back_layout.setOnClickListener(this);
	}

	public void initView() {
		layout_back_head = getView(R.id.layout_head);
		ll = (LinearLayout) findViewById(R.id.ll);
		tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);
		back_layout = getView(R.id.back_layout);
		txt_head = getView(R.id.txt_head);
		txt_head.setText(R.string.bidding_details);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
				onBackPressed();
			break;

		}
	}

	@Override
	public void onLoadingStart() {
		startLoading();
	}

	@Override
	public void onLoadingSuccess(Object t) {
		stopLoading();
		ll.removeAllViews();
		ArrayList<View> views = (ArrayList<View>) t;
		if(views!=null){
			for (int i = 0; i < views.size(); i++) {
				View view = views.get(i);
				if (view != null) {
					if (i == 0) {
						ll.addView(view);
					} else {
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.FILL_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						lp.topMargin = (int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 10, this
										.getResources().getDisplayMetrics());
						view.setLayoutParams(lp);
						ll.addView(view);
					}
				}
			}
		}
	}

	@Override
	public void onLoadingError(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		stopLoading();
	}

	@Override
	public void onLoadingCancel() {
		stopLoading();
	}

	@Override
	public void onNoInterNetError() {
		Toast.makeText(this, getString(R.string.network_error),Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoginInvalidate() {
		showExitDialog();
	}

	@Override
	public void onVersionBack(String version) {
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventbusAgent.getInstance().unregister(this);
		HYMob.getBaseDataListForPage(FetchDetailsActivity.this, HYEventConstans.PAGE_MY_ORDER_DETAIL, stop_time - resume_time);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventbusAgent.getInstance().unregister(this);
		HYMob.getBaseDataListForPage(FetchDetailsActivity.this, HYEventConstans.PAGE_MY_ORDER_DETAIL, stop_time - resume_time);
	}

	public void onEventMainThread(EventAction action){
		switch (action.type){
			case EVENT_TUIDAN_NOT_TIME://未开放时间
				type = RefundMediator.VALUE_TYPE_TIME_CLOSE;
				fetchDetailsPresenter.goToRefundActivity(type,orderId);
				break;
			case EVENT__FIRST_SUBMIT_TUIDAN://第一次退单申请页面
				type = RefundMediator.VALUE_TYPE_FIRST_REFUND;
				fetchDetailsPresenter.goToRefundActivity(type,orderId);
				break;
			case EVENT_ADD_TUIDAN://补单页面
				type = RefundMediator.VALUE_TYPE_ADD_REFUND;
				fetchDetailsPresenter.goToRefundActivity(type,orderId);
				break;
			case EVENT_TUIDAN_RESULT://退单结果页面
				type = RefundMediator.VALUE_TYPE_REFUND_RESULT;
				fetchDetailsPresenter.goToRefundActivity(type,orderId);
				break;
			case EVENT_TELEPHONE_FROM_DETAIL://打电话详情界面
				TelephoneBean bean = (TelephoneBean) action.getData();
				tvm.telephone(orderId,bean.getSource());
				break;

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		vm.fetchDetailDatas();
	}
}
