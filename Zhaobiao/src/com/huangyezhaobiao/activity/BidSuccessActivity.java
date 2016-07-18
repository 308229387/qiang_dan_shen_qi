package com.huangyezhaobiao.activity;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.fragment.home.BiddingFragment;
import com.huangyezhaobiao.fragment.home.MessageFragment;
import com.huangyezhaobiao.fragment.home.OrderListFragment;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.service.AlertService;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.KeyguardUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 抢单成功界面
 * 
 * @author linyueyang
 *
 */
public class BidSuccessActivity extends QBBaseActivity {

	private LinearLayout back_layout;
	private TextView txt_head;
	private Button toOrderList;

	private Button toBidList;
	private PushToPassBean receivePassBean;
	private BroadcastReceiver receiver;
	KeyguardManager keyguardManager;

	private String bidId;
	public static Long orderId;

	public static boolean isReset = false;

	KeyguardManager.KeyguardLock keyguardLock;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bid_success);

		Intent intent = this.getIntent(); 
		receivePassBean=(PushToPassBean)intent.getSerializableExtra("passBean");

		if (receivePassBean != null ) {
			bidId = String.valueOf(receivePassBean.getBidId());
		}
		HYMob.getDataListByQiangDan(BidSuccessActivity.this, HYEventConstans.EVENT_ID_SUCCESS_PAGE, bidId);

//		if(receivePassBean.getCateId() == 4063 || receivePassBean.getCateId() == 4064 ||
//				receivePassBean.getCateId() == 4066){
//			//启动15分钟推送服务
//			Intent newIntent = new Intent(this, AlertService.class);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("passBean", receivePassBean);
//			newIntent.putExtras(bundle);
//			startService(newIntent);
//
//		}
		initView();
		initListener();
		keyguardManager = (KeyguardManager)getApplication().getSystemService(KEYGUARD_SERVICE);
		keyguardLock = keyguardManager.newKeyguardLock("");
		registerScreenOffReceiver();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void initView() {
		layout_back_head = getView(R.id.layout_head);
		back_layout = (LinearLayout) this.findViewById(R.id.back_layout);
		back_layout.setVisibility(View.VISIBLE);
		txt_head = (TextView) findViewById(R.id.txt_head);
		txt_head.setText(R.string.bidding_success_title);

		toOrderList = (Button) findViewById(R.id.success_orderlist);
		toBidList = (Button) findViewById(R.id.success_bidlist);
		tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);
	}

	@Override
	public void initListener() {

		toOrderList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//查看订单
				BDMob.getBdMobInstance().onMobEvent(BidSuccessActivity.this, BDEventConstans.EVENT_ID_SUCCESS_PAGE_LOOK_BIDDING);
				if (receivePassBean != null ) {
					bidId = String.valueOf(receivePassBean.getBidId());
				}

				HYMob.getDataListByQiangDan(BidSuccessActivity.this, HYEventConstans.EVENT_ID_SUCCESS_PAGE_LOOK_BIDDING, bidId);

			    Map<String, String> map = new HashMap<String, String>();

				map.put(Constans.ORDER_ID, String.valueOf(orderId));

				ActivityUtils.goToActivityWithString(BidSuccessActivity.this, FetchDetailsActivity.class, map);


//			ActivityUtils.goToActivity(BidSuccessActivity.this, MainActivity.class);
			}
		});
		toBidList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//继续抢单
				BDMob.getBdMobInstance().onMobEvent(BidSuccessActivity.this, BDEventConstans.EVENT_ID_SUCCESS_PAGE_CONTINUE_BIDDING);
				if (receivePassBean != null ) {
					bidId = String.valueOf(receivePassBean.getBidId());
				}

				HYMob.getDataListByQiangDan(BidSuccessActivity.this, HYEventConstans.EVENT_ID_SUCCESS_PAGE_CONTINUE_BIDDING, bidId);

				isReset = true;
				ActivityUtils.goToActivity(BidSuccessActivity.this, MainActivity.class);

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
	protected void onDestroy() {
		super.onDestroy();
		unregisterScreenOffReceiver();
	}


	private class ScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			if (intent == null) return;
			LogUtils.LogE("shenzhixin", "action:" + intent.getAction());
			if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
				Log.e("shenzhixinUI","line 103..");
				KeyguardUtils.SCREEN_ON = true;
				KeyguardUtils.need_lock = false;
				LogUtils.LogE("shenzhixin", "screenOn:" + KeyguardUtils.need_lock);

			} else {
				Log.e("shenzhixinUI","line 109..");
				KeyguardUtils.need_lock = true;
				KeyguardUtils.SCREEN_ON = false;
				KeyguardUtils.notLock = false;
				openKeyguard();
				closeKeyguard();
				LogUtils.LogE("shenzhixin", "screenOff:" + KeyguardUtils.need_lock);
				BiddingApplication biddingApplication = (BiddingApplication) getApplication();
				LockActivity lockActivity = (LockActivity) biddingApplication.activity;
				if(lockActivity!=null){
					Log.e("shenzhixinUI","line 119..");
					LogUtils.LogE("shenzhixin", "lock:" + (lockActivity == null));
					lockActivity.closeLock();
				}else if(LockActivity.keyguardLock!=null){
					Log.e("shenzhixinUI","line 123..");
					LockActivity.keyguardLock.reenableKeyguard();
				}
			}
		}
	}


	/**
	 * 开锁
	 */
	private void openKeyguard() {
		keyguardLock.disableKeyguard();
	}

	/**
	 * 锁屏代码
	 */
	private void closeKeyguard() {
		keyguardLock.reenableKeyguard();
	}


	/**
	 * 注册屏幕暗时的广播接收者
	 */
	private void registerScreenOffReceiver() {
		if (receiver == null) {
			receiver = new ScreenReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			registerReceiver(receiver, filter);
		}
	}


	/**
	 * 解绑
	 */
	private void unregisterScreenOffReceiver() {
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}

	}
}
