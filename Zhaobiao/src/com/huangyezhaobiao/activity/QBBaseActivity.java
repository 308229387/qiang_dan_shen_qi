package com.huangyezhaobiao.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangye.commonlib.utils.UserConstans;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;
import com.huangyezhaobiao.netmodel.NetStateManager;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.LoadingProgress;
import com.huangyezhaobiao.view.QDWaitDialog;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.TitleMessageBarLayout.OnTitleBarClickListener;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;
import com.huangyezhaobiao.vm.KnockViewModel;
import com.wuba.loginsdk.external.LoginClient;

/**
 * 抢标的最基类activity,多一些这个项目的新东西
 * 实现一个接口---处理消息的
 * loading
 * dialog
 *
 * @author szx
 *
 */
public abstract class QBBaseActivity extends CommonBaseActivity implements INotificationListener,OnTitleBarClickListener,INetStateChangedListener{
	private BiddingApplication app;
	protected TitleMessageBarLayout tbl;
	private LoadingProgress dialog; 
	protected KnockViewModel kvm;
	private ZhaoBiaoDialog zbdialog;
	protected ZhaoBiaoDialog exitDialog;
	private PushToPassBean passBean;
//	private MyCustomDialog popDialog;
	protected View layout_back_head;
//	private YuEViewModel    yuEViewModel;
	private ProgressDialog ProgressDialog;
	protected void dismissQDWaitDialog(){
		if(ProgressDialog!=null && ProgressDialog.isShowing()){
			ProgressDialog.dismiss();
		}
	}
	protected NetWorkVMCallBack callBack = new NetWorkVMCallBack() {
		@Override
		public void onNoInterNetError() {
			Toast.makeText(QBBaseActivity.this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
			dismissQDWaitDialog();
		}

		@Override
		public void onLoginInvalidate() {
			GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
			showExitDialog();
		}

		@Override
		public void onVersionBack(String version) {
			Log.e("shenyy","version:"+version);
		}

		@Override
		public void onLoadingSuccess(Object t) {
			if (t instanceof Integer) {
				dismissQDWaitDialog();
				int status = (Integer) t;
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("passBean", passBean);
				intent.putExtras(bundle);
//				if(null!=popDialog ){
//					popDialog.dismiss();
//				}
				if(status==3){//成功
					Toast.makeText(QBBaseActivity.this,"成功",Toast.LENGTH_SHORT).show();
					intent.setClass(QBBaseActivity.this, BidSuccessActivity.class);
					startActivity(intent);
				}
				else if(status==1){
					intent.setClass(QBBaseActivity.this, BidGoneActivity.class);
					startActivity(intent);
				}
				else if(status==2) {
					zbdialog = new ZhaoBiaoDialog(QBBaseActivity.this, getString(R.string.not_enough_balance));
					zbdialog.setCancelButtonGone();
					zbdialog.setOnDialogClickListener(new onDialogClickListener() {
						@Override
						public void onDialogOkClick() {
							zbdialog.dismiss();
						}
						
						@Override
						public void onDialogCancelClick() {
							
						}
					});
					
					zbdialog.show();
					if(passBean!=null)
						MDUtils.YuENotEnough(""+passBean.getCateId(), ""+passBean.getBidId());
				}
				else if(status==4){
					Toast.makeText(QBBaseActivity.this, getString(R.string.bidding_already_bid), Toast.LENGTH_SHORT).show();
				}else if(status==5){
					Toast.makeText(QBBaseActivity.this,"并没有抢到单",Toast.LENGTH_SHORT).show();
					intent.setClass(QBBaseActivity.this, BidFailureActivity.class);
					startActivity(intent);
				}
				else{
					Toast.makeText(QBBaseActivity.this, getString(R.string.bidding_exception), Toast.LENGTH_SHORT).show();
				}
			}
		}
		@Override
		public void onLoadingStart() {
		}
		@Override
		public void onLoadingError(String msg) {

			if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
				if(LogoutDialog!=null){
					LogoutDialog.setMessage("PPU过期，请重新登录");
					LogoutDialog.show();
				}
			}else if(!TextUtils.isEmpty(msg)){
				Toast.makeText(QBBaseActivity.this, msg, Toast.LENGTH_SHORT).show();
			}

			dismissQDWaitDialog();
		}
		@Override
		public void onLoadingCancel() {
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		kvm = new KnockViewModel(callBack, this);
		ProgressDialog = new QDWaitDialog(this);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
//		yuEViewModel = new YuEViewModel(callBack,this);
		getWindow().setBackgroundDrawable(null);
	}




	@Override
	public void onTitleBarClosedClicked() {
		if(tbl!=null)
			tbl.setVisibility(View.GONE);
	}
	
	@Override
	protected void onResume() {
		UserConstans.USER_ID = UserUtils.getUserId(this);
		app = (BiddingApplication) getApplication();
		app.setCurrentNotificationListener(this);
		NetStateManager.getNetStateManagerInstance().setINetStateChangedListener(this);
		if(tbl!=null){
			tbl.setVisibility(View.GONE);
			tbl.setTitleBarListener(this);
		}
		if(NetUtils.isNetworkConnected(this)){
			NetConnected();
		}else{
			NetDisConnected();
		}
		super.onResume();

		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
			int height = Utils.getStatusBarHeight(this);
			int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
			Log.e("shenzhixin", "layout_back_head..." + (layout_back_head == null) + ",height:" + height);
			if (layout_back_head != null) {
				layout_back_head.setPadding(0, height + more, 0, 0);
			}
		}
//		yuEViewModel.getBalance();
	}




	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onPause() {
		if (app != null)
			app.removeINotificationListener();
		NetStateManager.getNetStateManagerInstance().removeINetStateChangedListener();
		super.onPause();


	}

	@Override
	public void onTitleBarClicked(TitleBarType type) {
		
	}
	
	/**
	 * 加载效果
	 */
	public void startLoading() {
		if (dialog == null) {
			dialog = new LoadingProgress(QBBaseActivity.this,
					R.style.loading);
		}
		if(dialog!=null && !this.isFinishing()){
			dialog.show();
			LogUtils.LogE("ashenDialog", "show");
		}
	}

	/**
	 * 对话框消失
	 */
	public void stopLoading() {
		if (!this.isFinishing() && dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
	@Override
	public void NetConnected() {
		if(tbl!=null && tbl.getType()==TitleBarType.NETWORK_ERROR){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					tbl.setVisibility(View.GONE);
				}
			});
		}
	}
	@Override
	public void NetDisConnected() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(tbl!=null){
					tbl.showNetError();
					tbl.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	/**
	 * 正常逻辑下推送消息到来时的逻辑
	 * 各个activity可以复写
	 */
	@Override
	public void onNotificationCome(PushBean pushBean) {
		LogUtils.LogV("nnnnnn",String.valueOf(pushBean.getTag()));
		if (null != pushBean) {
			int type = pushBean.getTag();
			if (type == 100 && StateUtils.getState(QBBaseActivity.this) == 1) {
				/*popDialog = MyCustomDialog.getInstance(this, new OnCustomDialogListener() {
					//点击抢单后的回调
					@Override
					public void back(PushToPassBean bean) {
						
						passBean = bean;
						kvm = new KnockViewModel(callBack, QBBaseActivity.this);
						kvm.knock(bean);
						//
						ProgressDialog.show();
					}
				});
				popDialog.show();*/
				Intent intent = new Intent(this,PushInActivity.class);
				startActivity(intent);
			}
			else{
				if(tbl!=null){
					tbl.setPushBean(pushBean);
					tbl.setVisibility(View.VISIBLE);
				}
			//	if(StateUtils.getState(QBBaseActivity.this) == 0){
					PushUtils.pushList.clear();
				//}
			}
		}
		
	}


	//	@Override
//	public void back(PushToPassBean bean) {
//		kvm.knock(bean, AppConstants.BIDSOURCE_WINDOW);
//	}

}
