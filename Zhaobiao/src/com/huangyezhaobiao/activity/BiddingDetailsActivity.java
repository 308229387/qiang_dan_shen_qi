package com.huangyezhaobiao.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.mediator.RefundMediator;
import com.huangyezhaobiao.presenter.FetchDetailsPresenter;
import com.huangyezhaobiao.receiver.PhoneReceiver;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.CallAlertDialog;
import com.huangyezhaobiao.view.CallClassifyDialog;
import com.huangyezhaobiao.view.CallDialog;
import com.huangyezhaobiao.view.InputCallDialog;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.WaitingTransfer;
import com.huangyezhaobiao.vm.CallPhoneViewModel;
import com.huangyezhaobiao.vm.FetchDetailsVM;
import com.wuba.loginsdk.external.LoginClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 抢单详情
 * @author shenzhixin
 *
 */
public class BiddingDetailsActivity extends QBBaseActivity implements
		OnClickListener, NetWorkVMCallBack {
	public static String orderState ;
	public static String time;
	private FetchDetailsVM vm;
	private FetchDetailsPresenter fetchDetailsPresenter;
	private LinearLayout ll;
	private LinearLayout back_layout;
	private TextView txt_head;
	private String orderId;
	private String type;
//	private TelephoneVModel tvm;

	private RelativeLayout order_detail_message ,order_detail_telephone;

	private CallDialog dialog;
	private InputCallDialog InputDialog;
	private CallAlertDialog alertDialog;
	private CallClassifyDialog callClassifyDialog;

	private WaitingTransfer transferDialog;
	//双呼
	private CallPhoneViewModel phoneViewModel;

	private Handler handler = new Handler();

	String  mobile; //商家电话

	public static String clientPhone; //客户电话

	 boolean flag =true; //发短信界面不弹窗


	private long call_Show_time,call_dismiss_time ;

	private long input_Show_time,input_dismiss_time ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fetch_details);
		fetchDetailsPresenter = new FetchDetailsPresenter(this);
		initView();
		initListener();
		String isSon = UserUtils.getIsSon(this);
		if(!TextUtils.isEmpty(isSon) && TextUtils.equals("1",isSon)){
			mobile = LoginClient.doGetUserPhoneOperate(this);
		}else{
			mobile = SPUtils.getVByK(this, GlobalConfigBean.KEY_USERPHONE);
		}

		phoneViewModel = new CallPhoneViewModel(vmCallback,this);

		PhoneReceiver.addToMonitor(interaction);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventbusAgent.getInstance().register(this);
		orderId = getIntent().getStringExtra(Constans.ORDER_ID);

		try {
			if(TextUtils.isEmpty(orderId)){//orderId如果为空
                vm = new FetchDetailsVM(this, this, 0l);
            }else{
				SPUtils.setOrderId(this, orderId);
                vm = new FetchDetailsVM(this, this, Long.parseLong(SPUtils.getOrderId(this)));
            }
			vm.fetchDetailDatas();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		flag = true;
	}


	public void initListener() {
		back_layout.setOnClickListener(this);
		order_detail_message.setOnClickListener(this);
		order_detail_telephone.setOnClickListener(this);
	}

	public void initView() {
		layout_back_head = getView(R.id.layout_head);
		back_layout = getView(R.id.back_layout);
		back_layout.setVisibility(View.VISIBLE);
		txt_head = getView(R.id.txt_head);
		txt_head.setText(R.string.bidding_details);

		ll = (LinearLayout) findViewById(R.id.ll);
		tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);
		order_detail_message = (RelativeLayout) findViewById(R.id.order_detail_message);
		order_detail_telephone = (RelativeLayout) findViewById(R.id.order_detail_telephone);

	}


	private void initDialog() {
		    dialog = new CallDialog(this);
			dialog.setMessage(getString(R.string.transfer_answer_number) + mobile);
			dialog.setPositiveText("接听");
			dialog.setNagativeText("取消");
			dialog.setCancelable(false);
			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface d) {
					dialog = null;
					call_dismiss_time = System.currentTimeMillis();
					HYMob.getBaseDataListForPage(BiddingDetailsActivity.this, HYEventConstans.PAGE_MY_ORDER_LIST, call_dismiss_time - call_Show_time);
				}
			});
			dialog.setOnDialogClickListener(new CallDialog.onDialogClickListener() {

				@Override
				public void onDialogOkClick() {
					SPUtils.setAppMobile(BiddingDetailsActivity.this, mobile);
					if (phoneViewModel != null) {
						phoneViewModel.call(orderId, mobile);
					}
					dialog.dismiss();
					startTransfering();
					// 等15秒
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							stopTransfering();
						}
					}, 15000);

					HYMob.getDataList(BiddingDetailsActivity.this, HYEventConstans.PAGE_DIALOG_CALL);
				}

				@Override
				public void onDialogCancelClick() {
					dialog.dismiss();
					HYMob.getDataList(BiddingDetailsActivity.this, HYEventConstans.EVENT_CALL_CANCEL);
				}

				@Override
				public void changeNumberClick() {
					initInputNumberDailog();
					dialog.dismiss();
					HYMob.getDataList(BiddingDetailsActivity.this, HYEventConstans.EVENT_CHANGE_NUMBER);
				}
			});
			dialog.show();
		   call_Show_time =  System.currentTimeMillis();

	}

	private void  initInputNumberDailog(){
		    InputDialog = new InputCallDialog(this);
			InputDialog.setMessage("修改接听电话");
			InputDialog.setPositiveText("确定");
			InputDialog.setNagativeText("取消");
			InputDialog.setCancelable(false);
			InputDialog.setnumberButtonGone();
			InputDialog.setInputNumberVisible();

			InputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					InputDialog = null;
					input_dismiss_time = System.currentTimeMillis();
					HYMob.getBaseDataListForPage(BiddingDetailsActivity.this, HYEventConstans.PAGE_DIALOG_INPUT, input_dismiss_time - input_Show_time);
				}
			});
			InputDialog.setOnDialogClickListener(new InputCallDialog.onDialogClickListener() {

				@Override
				public void onDialogOkClick() {
					String phone = InputDialog.getInputNumber();
					SPUtils.setAppMobile(BiddingDetailsActivity.this, phone);
					if (phoneViewModel != null) {
						phoneViewModel.call(orderId, phone);
					}
					InputDialog.dismiss();

					HYMob.getDataList(BiddingDetailsActivity.this, HYEventConstans.EVENT_CALL_CONFIRM);
					startTransfering();

					// 等15秒
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							stopTransfering();
						}
					}, 15000);
				}

				@Override
				public void onDialogCancelClick() {
					InputDialog.dismiss();
					initDialog();
					HYMob.getDataList(BiddingDetailsActivity.this, HYEventConstans.EVENT_CALL_CANCEL);
				}

				@Override
				public void changeNumberClick() {
				}
			});
			InputDialog.show();
		    input_Show_time = System.currentTimeMillis();
	}


	/**
	 * 需要同步
	 *
	 * @return
	 */
	private boolean needAsync() {
		//当前时间戳
		Date currentTimeLine = null;
		//从sp取时间戳
		Date latTimeLine = null;

		SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		try {
			currentTimeLine = new Date(System.currentTimeMillis());
			if(!TextUtils.isEmpty(time)){
				latTimeLine = sfd.parse(time);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return TimeUtils.beyond2Hour(currentTimeLine, latTimeLine);//没有在时间戳范围内

	}


	private void initAlertCallDialog(){
			alertDialog = new CallAlertDialog(this);
			alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					alertDialog.dismiss();
				}
			});
			alertDialog.setMessage(getString(R.string.alert_customer_call));
			alertDialog.setCancelable(false);
			alertDialog.setCancelButtonGone();
//			alertDialog.setOkButtonBackground(Color.parseColor("#FFE3A1"));
			alertDialog.setPositiveText("呼叫用户");
			alertDialog.setOkButtonCOlor(Color.parseColor("#86661D"));
			alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					alertDialog = null;
				}
			});
			alertDialog.setOnDialogClickListener(new CallAlertDialog.onDialogClickListener() {
				@Override
				public void onDialogOkClick() {
					mobile = SPUtils.getAppMobile(BiddingDetailsActivity.this);
					phoneViewModel.call(orderId,mobile);
					alertDialog.dismiss();

					startTransfering();

					// 等15秒
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							stopTransfering();
						}
					}, 15000);
				}

				@Override
				public void onDialogCancelClick() {
				}
			});
			alertDialog.show();

	}


	/**
	 * 加载效果
	 */
	public void startTransfering() {

		transferDialog = new WaitingTransfer(this);
		transferDialog.setCancelable(false);
		transferDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				transferDialog = null;
			}
		});
		transferDialog.show();

	}

	/**
	 * 对话框消失
	 */
	public void stopTransfering() {
		if (!this.isFinishing() && transferDialog != null && transferDialog.isShowing()) {
			transferDialog.dismiss();
			transferDialog = null;
		}
	}


	private NetWorkVMCallBack vmCallback = new NetWorkVMCallBack() {
		@Override
		public void onLoadingStart() {
		}

		@Override
		public void onLoadingSuccess(Object t) {
			if (t instanceof Integer) {
				int status = (Integer) t;
				if(status == 0){

				}else{
					stopTransfering();
					Toast.makeText(BiddingDetailsActivity.this, "电话转接失败", Toast.LENGTH_SHORT).show();
				}

			}
		}

		@Override
		public void onLoadingError(String msg) {

		}

		@Override
		public void onLoadingCancel() {

		}

		@Override
		public void onNoInterNetError() {

		}

		@Override
		public void onLoginInvalidate() {

		}

		@Override
		public void onVersionBack(String version) {

		}

	};

	/**
	 * 拒绝接听电话
	 */
	PhoneReceiver.BRInteraction  interaction = new PhoneReceiver.BRInteraction() {
		@Override
		public void sendAction(String action) {

			if(action.equals(PhoneReceiver.CALL_UP)){
				stopTransfering();
			}else if(action.equals(PhoneReceiver.CALL_OVER)){
				stopTransfering();
				if(!needAsync() && alertDialog == null){ //两小时内
					initAlertCallDialog();
				}
			}
		}
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_layout:
				onBackPressed();
				break;
			case R.id.order_detail_message:
				flag = false;
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.setData(Uri.parse("smsto:" + clientPhone));
//				sendIntent.putExtra("sms_body", "");
				startActivity(sendIntent);

				HYMob.getDataListByCall(BiddingDetailsActivity.this, HYEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_MESSAGE, String.valueOf(orderId), "1");

				break;
			case R.id.order_detail_telephone:
				initDialog();
				HYMob.getDataListByCall(BiddingDetailsActivity.this, HYEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_PHONE, String.valueOf(orderId), "1");
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
		stopLoading();
		if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
			super.onLoadingError(msg);
		}else if(!TextUtils.isEmpty(msg)){
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onLoadingCancel() {
		stopLoading();
	}

	@Override
	public void onNoInterNetError() {

		Toast.makeText(this, getString(R.string.setting_network),Toast.LENGTH_SHORT).show();
		stopLoading();
	}

	@Override
	public void onLoginInvalidate() {
		showExitDialog();
	}

	@Override
	public void onVersionBack(String version) {
	}

	@Override
	protected void onStop() {
		super.onStop();
//		EventbusAgent.getInstance().unregister(this);
		HYMob.getBaseDataListForPage(BiddingDetailsActivity.this, HYEventConstans.PAGE_MY_ORDER_DETAIL, stop_time - resume_time);
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopTransfering();
		EventbusAgent.getInstance().unregister(this);
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
//			case EVENT_TELEPHONE_FROM_DETAIL://打电话详情界面
//				TelephoneBean bean = (TelephoneBean) action.getData();
//				tvm.telephone(orderId,bean.getSource());
//				break;
		}
	}

	@Override
	public void onNotificationCome(PushBean pushBean) {
		if (null != pushBean) {
			tbl.setPushBean(pushBean);
			tbl.setVisibility(View.VISIBLE);
			if(pushBean.getTag()==100){
				if(!flag){  //发短信不弹窗
					PushUtils.pushList.clear();
				}else {
					super.onNotificationCome(pushBean);
				}

			}
		}
	}


	@Override
	protected void onDestroy() {
		PhoneReceiver.releaseMonitor(interaction);
		super.onDestroy();
		if (!this.isFinishing() && dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
		if (!this.isFinishing() && InputDialog != null && InputDialog.isShowing()) {
			InputDialog.dismiss();
			InputDialog = null;
		}
		if (!this.isFinishing() && alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
			alertDialog = null;
		}

		if (!this.isFinishing() && callClassifyDialog != null && callClassifyDialog.isShowing()) {
			callClassifyDialog.dismiss();
			callClassifyDialog = null;
		}

		if (!this.isFinishing() && transferDialog != null && transferDialog.isShowing()) {
			transferDialog.dismiss();
			transferDialog = null;
		}
	}

}
