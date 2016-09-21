package com.huangyezhaobiao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.huangyezhaobiao.adapter.OrderDetailAdapter;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.mediator.RefundMediator;
import com.huangyezhaobiao.presenter.FetchDetailsPresenter;
import com.huangyezhaobiao.receiver.PhoneReceiver;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.CallAlertDialog;
import com.huangyezhaobiao.view.CallClassifyDialog;
import com.huangyezhaobiao.view.CallDialog;
import com.huangyezhaobiao.view.HYListView;
import com.huangyezhaobiao.view.InputCallDialog;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.WaitingTransfer;
import com.huangyezhaobiao.vm.CallPhoneViewModel;
import com.huangyezhaobiao.vm.FetchDetailsVM;
import com.lzy.okhttputils.OkHttpUtils;
import com.wuba.loginsdk.external.LoginClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.respons.OrderDetailRespons;

/**
 * 抢单详情
 * @author shenzhixin
 *
 */
public class BusinessDetailsActivity extends QBBaseActivity implements
		OnClickListener, NetWorkVMCallBack {

	private FetchDetailsPresenter fetchDetailsPresenter;
	private LinearLayout back_layout;
	private TextView txt_head;
	private TextView tv_orderDetail_title;
	private HYListView lv_basic_info;
	private View individer1,individer2;
	private HYListView lv_detail_info;
    private RelativeLayout rl_detail_price;
	private TextView tv_order_fee,tv_order_fee_content,tv_original_fee_content;
	private LinearLayout ll_contact_record;
    private HYListView lv_contact_record;
	private RelativeLayout order_detail_message ,order_detail_telephone;
	/** 初次进入时候的蒙版背景 */
	private RelativeLayout rl_call_layout;
	/** 初次进入时的蒙版图片 */
	private ImageView  iv_call_alert;

	private OrderDetailAdapter adapter;

	private String orderId;
	private String type;
	public static String orderState ;
	private String callState;
	public static String time;
	private  String  mobile; //商家电话
	public  String clientPhone; //客户电话

	private boolean flag =true; //发短信界面不弹窗

	private long call_Show_time,call_dismiss_time ;
	private long input_Show_time,input_dismiss_time ;

	private CallDialog dialog;
	private InputCallDialog InputDialog;
	private CallClassifyDialog callClassifyDialog;
	private WaitingTransfer transferDialog;

	//双呼
	private CallPhoneViewModel phoneViewModel;
	private Handler handler = new Handler();

	public final static List<String> callCheckedId = new ArrayList<>(); //电话状态列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
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
		getData(orderId);
		flag = true;
	}


	public void getData(String orderId) {
//		OkHttpUtils.get("http://zhaobiao.58.com/appbatch/order/orderdetail")
		OkHttpUtils.get(Urls.GET_NEW_ORDER_DETAILS)
                .params("orderId", orderId)
				.execute(new getOrderDetailRespons(this, true));
	}


  private class getOrderDetailRespons extends DialogCallback<OrderDetailRespons>{

	  public getOrderDetailRespons(Activity context, Boolean needProgress) {
		  super(context, needProgress);
	  }

	  @Override
	  public void onResponse(boolean isFromCache, OrderDetailRespons orderDetailRespons, Request request, @Nullable Response response) {
         if(orderDetailRespons != null){

			 List<OrderDetailRespons.bean>  basiclist =  orderDetailRespons.getBasicDetail();
			 if(basiclist != null && basiclist.size() >0){
				 setBasicListData(basiclist);
			 }
			 List<OrderDetailRespons.bean>  orderlist =  orderDetailRespons.getOrderDetail();
			 if(orderlist != null && orderlist.size() >0){
				 setOrderListData(orderlist);
			 }
			 OrderDetailRespons.PriceBean price = orderDetailRespons.getPriceDetail();
			 if(price!= null){
				 setPriceData( price);
			 }
             List<OrderDetailRespons.bean>  callList =  orderDetailRespons.getCallList();
			 if(callList!= null && callList.size() >0){
				 setRecordListData(callList);
			 }else{
				 ll_contact_record.setVisibility(View.GONE);
			 }
			 callState = orderDetailRespons.getOrderState();
			 clientPhone = orderDetailRespons.getPhone();
		 }

	  }
  }

	private void setBasicListData(List<OrderDetailRespons.bean>  basiclist){
		tv_orderDetail_title.setVisibility(View.VISIBLE);
		adapter = new OrderDetailAdapter(BusinessDetailsActivity.this,basiclist);
		lv_basic_info.setAdapter(adapter);
		lv_basic_info.setFocusable(false);
	}

	private void setOrderListData(List<OrderDetailRespons.bean>  orderlist){
		individer1.setVisibility(View.VISIBLE);
		adapter = new OrderDetailAdapter(BusinessDetailsActivity.this,orderlist);
		lv_detail_info.setAdapter(adapter);
		lv_detail_info.setFocusable(false);
	}

	private void setPriceData(OrderDetailRespons.PriceBean price){
		individer2.setVisibility(View.VISIBLE);
		rl_detail_price.setVisibility(View.VISIBLE);
		String price_title = price.getTitle();
		if(!TextUtils.isEmpty(price_title)){
			tv_order_fee.setText(price_title);
		}
		String price_fee= price.getPromotionPrice();
		if(!TextUtils.isEmpty(price_fee)){
			tv_order_fee_content.setText("￥"+price_fee);
		}
		String price_original_fee = price.getOriginPrice();
		if(!TextUtils.isEmpty(price_original_fee)){
			tv_original_fee_content.setText(price_original_fee);
		}

		tv_original_fee_content.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		if(TextUtils.equals(price_fee,price_original_fee)){
			tv_original_fee_content.setVisibility(View.GONE);
		}
	}

	private void setRecordListData(  List<OrderDetailRespons.bean>  callList){
		ll_contact_record.setVisibility(View.VISIBLE);
		adapter = new OrderDetailAdapter(BusinessDetailsActivity.this,callList);
		lv_contact_record.setAdapter(adapter);
		lv_contact_record.setFocusable(false);
	}

	public void initListener() {
		back_layout.setOnClickListener(this);
		order_detail_message.setOnClickListener(this);
		order_detail_telephone.setOnClickListener(this);
	}

	public void initView() {
		initHeadView();
		initMessageBar();
		initBusinessInfoView();
		initPriceInfoView();
		initContactRecordView();
		initCallAndMessageView();
		initMineLayoutView();
		initMineImageView();
	}

	private void initHeadView(){
		layout_back_head = getView(R.id.layout_head);
		back_layout = getView(R.id.back_layout);
		back_layout.setVisibility(View.VISIBLE);
		txt_head = getView(R.id.txt_head);
		txt_head.setText(R.string.bidding_details);
	}

	private void initMessageBar(){
		tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);
	}

	private void initBusinessInfoView(){
		tv_orderDetail_title  = (TextView) findViewById(R.id.tv_orderDetail_title);
		lv_basic_info = (HYListView) findViewById(R.id.lv_basic_info);
		individer1 = findViewById(R.id.individer1);
		lv_detail_info = (HYListView) findViewById(R.id.lv_detail_info);
	}

	private void initPriceInfoView(){
		individer2 = findViewById(R.id.individer2);
		rl_detail_price = (RelativeLayout) findViewById(R.id.rl_detail_price);
		tv_order_fee = (TextView) findViewById(R.id.tv_order_fee);
		tv_order_fee_content = (TextView) findViewById(R.id.tv_order_fee_content);
		tv_original_fee_content = (TextView) findViewById(R.id.tv_original_fee_content);
	}

	private void initContactRecordView(){
		ll_contact_record = (LinearLayout) findViewById(R.id.ll_contact_record);
		lv_contact_record = (HYListView) findViewById(R.id.lv_contact_record);
	}

	private void initCallAndMessageView(){
		order_detail_message = (RelativeLayout) findViewById(R.id.order_detail_message);
		order_detail_telephone = (RelativeLayout) findViewById(R.id.order_detail_telephone);
	}


	private void initMineLayoutView(){
		//蒙版相关初始化
		rl_call_layout = getView(R.id.rl_call_layout);
		rl_call_layout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	private void initMineImageView(){
		iv_call_alert = getView(R.id.iv_call_alert);
		iv_call_alert.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rl_call_layout.setVisibility(View.GONE);
				getSharedPreferences("Setting", Context.MODE_PRIVATE).edit().putBoolean("detail_call", true).commit();
				initCallClassifyDialog();

			}
		});
	}

	public void setMineMask() {
		SharedPreferences sharedPreferences = getSharedPreferences(
				"Setting", Context.MODE_PRIVATE);
		boolean isread = sharedPreferences.getBoolean("detail_call", false);
		if (!isread) {
			rl_call_layout.setVisibility(View.VISIBLE);
		} else {
			rl_call_layout.setVisibility(View.GONE);
			initCallClassifyDialog();
		}
	}


	private void closeCallProgress(){
		// 等15秒
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				stopTransfering();
				setMineMask();
			}
		}, 15000);
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
					HYMob.getBaseDataListForPage(BusinessDetailsActivity.this, HYEventConstans.PAGE_MY_ORDER_LIST, call_dismiss_time - call_Show_time);
				}
			});
			dialog.setOnDialogClickListener(new CallDialog.onDialogClickListener() {

				@Override
				public void onDialogOkClick() {
					HYMob.getDataList(BusinessDetailsActivity.this, HYEventConstans.PAGE_DIALOG_CALL);
					SPUtils.setAppMobile(BusinessDetailsActivity.this, mobile);
					if (phoneViewModel != null) {
						phoneViewModel.call(orderId, mobile);
					}
					dialog.dismiss();
					startTransfering();
				}

				@Override
				public void onDialogCancelClick() {
					dialog.dismiss();
					HYMob.getDataList(BusinessDetailsActivity.this, HYEventConstans.EVENT_CALL_CANCEL);
				}

				@Override
				public void changeNumberClick() {
					initInputNumberDailog();
					dialog.dismiss();
					HYMob.getDataList(BusinessDetailsActivity.this, HYEventConstans.EVENT_CHANGE_NUMBER);
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
					HYMob.getBaseDataListForPage(BusinessDetailsActivity.this, HYEventConstans.PAGE_DIALOG_INPUT, input_dismiss_time - input_Show_time);
				}
			});
			InputDialog.setOnDialogClickListener(new InputCallDialog.onDialogClickListener() {

				@Override
				public void onDialogOkClick() {
					HYMob.getDataList(BusinessDetailsActivity.this, HYEventConstans.EVENT_CALL_CONFIRM);
					String phone = InputDialog.getInputNumber();
					SPUtils.setAppMobile(BusinessDetailsActivity.this, phone);
					if (phoneViewModel != null) {
						phoneViewModel.call(orderId, phone);
					}
					InputDialog.dismiss();
					startTransfering();
				}

				@Override
				public void onDialogCancelClick() {
					InputDialog.dismiss();
					initDialog();
					HYMob.getDataList(BusinessDetailsActivity.this, HYEventConstans.EVENT_CALL_CANCEL);
				}

				@Override
				public void changeNumberClick() {
				}
			});
			InputDialog.show();
		    input_Show_time = System.currentTimeMillis();
	}


	private boolean isAlreadyFinish(){
			return (!TextUtils.isEmpty(callState) && !TextUtils.equals(callState, "1")
					&& !TextUtils.equals(callState, "2") && !TextUtils.equals(callState, "90"));
	}

	private boolean isFirstShow(){
		return (TextUtils.equals(callState, "0") || TextUtils.equals(callState, "10")
				|| TextUtils.equals(callState, "11"));
	}


	private void initCallClassifyDialog() {
		if (this != null && !this.isFinishing() && callClassifyDialog == null) {
			callCheckedId.clear();
			if (isAlreadyFinish()) {//已结束不弹窗
				if (isFirstShow()) {  //第一次弹窗
					callClassifyDialog = new CallClassifyDialog(this, true);
				} else {
					callClassifyDialog = new CallClassifyDialog(this, false);
				}

				callClassifyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						callClassifyDialog = null;
					}
				});
				callClassifyDialog.setCancelable(false);
				callClassifyDialog.setOnDialogClickListener(new CallClassifyDialog.onDialogClickListener() {
					@Override
					public void onDialogSubmitClick() {
						String callMark = null;
						if (callCheckedId != null && callCheckedId.size() > 0) {
							for (String s : callCheckedId) {
								callMark = s;
							}
						}
						String message = callClassifyDialog.getInputMessage();
						if (!TextUtils.isEmpty(orderId)
								&& !TextUtils.isEmpty(callMark)) {
							getData(orderId, callMark, message);
						} else {
							ToastUtils.showToast("请对此商机进行分类~");
						}
					}
				});
				callClassifyDialog.show();
			}
		}

	}

	public void getData(String orderId,String mark,String desc) {
//		OkHttpUtils.get("http://zhaobiao.58.com/appbatch/order/callStateUpload")
		OkHttpUtils.get(Urls.SUBMIT_CALL_TYPE)
				.params("orderId", orderId)
				.params("mark", mark)
				.params("desc", desc)
				.execute(new submitCallResultRespons(BusinessDetailsActivity.this, true));
	}

	private class submitCallResultRespons extends DialogCallback<String> {

		public submitCallResultRespons(Activity context, Boolean needProgress) {
			super(context, needProgress);
		}

		@Override
		public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
			if(callClassifyDialog != null){
				callClassifyDialog.dismiss();
			}
			getData(orderId);
		}
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
			startTransfering();
		}

		@Override
		public void onLoadingSuccess(Object t) {
			if (t instanceof Integer) {
				int status = (Integer) t;
				if(status == 0){
					closeCallProgress();
				}else{
					stopTransfering();
					setMineMask();
					Toast.makeText(BusinessDetailsActivity.this, "电话转接失败", Toast.LENGTH_SHORT).show();
				}

			}
		}

		@Override
		public void onLoadingError(String msg) {
			stopTransfering();
			Toast.makeText(BusinessDetailsActivity.this, "电话转接失败", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onLoadingCancel() {
			stopTransfering();
		}

		@Override
		public void onNoInterNetError() {
			handler.postDelayed(new Runnable() {// 等5秒
				@Override
				public void run() {
					stopTransfering();
				}
			}, 5000);
			Toast.makeText(BusinessDetailsActivity.this, "请检查您的网络，再试一下哦~", Toast.LENGTH_SHORT).show();
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
				setMineMask();
			}else if(action.equals(PhoneReceiver.CALL_OVER)){
				stopTransfering();
				setMineMask();
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
				if(!TextUtils.isEmpty(clientPhone)){
					Intent sendIntent = new Intent(Intent.ACTION_VIEW);
					sendIntent.setData(Uri.parse("smsto:" + clientPhone));
//				sendIntent.putExtra("sms_body", "");
					startActivity(sendIntent);
				}
				HYMob.getDataListByCall(BusinessDetailsActivity.this, HYEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_MESSAGE, String.valueOf(orderId), "1");

				break;
			case R.id.order_detail_telephone:
				initDialog();
				HYMob.getDataListByCall(BusinessDetailsActivity.this, HYEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_PHONE, String.valueOf(orderId), "1");
				break;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
//		EventbusAgent.getInstance().unregister(this);
		HYMob.getBaseDataListForPage(BusinessDetailsActivity.this, HYEventConstans.PAGE_MY_ORDER_DETAIL, stop_time - resume_time);
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
