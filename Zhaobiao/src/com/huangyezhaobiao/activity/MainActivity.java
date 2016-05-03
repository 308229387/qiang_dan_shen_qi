package com.huangyezhaobiao.activity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangye.commonlib.utils.UserConstans;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.PopAdapter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.AccountExpireBean;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.ActivityInterface;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter.AdapterListener;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;
import com.huangyezhaobiao.netmodel.NetStateManager;
import com.huangyezhaobiao.presenter.MainPresenter;
import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.BidListUtils;
import com.huangyezhaobiao.utils.KeyguardUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.PullToRefreshListViewUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.LoadingProgress;
import com.huangyezhaobiao.view.MyCustomDialog;
import com.huangyezhaobiao.view.QDWaitDialog;
import com.huangyezhaobiao.view.SegmentControl;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.TitleMessageBarLayout.OnTitleBarClickListener;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;
import com.huangyezhaobiao.vm.GrabListViewModel;
import com.huangyezhaobiao.vm.KnockViewModel;
import com.huangyezhaobiao.vm.LogoutViewModel;
import com.huangyezhaobiao.vm.UpdateViewModel;
import com.huangyezhaobiao.vm.YuEViewModel;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;
import java.util.Map;

/**
 * 主页面
 * @author 58
 *
 */
public class MainActivity extends CommonFragmentActivity implements
		ActivityInterface, OnClickListener, ListNetWorkVMCallBack,
		INotificationListener, OnTitleBarClickListener,INetStateChangedListener, AdapterListener, onDialogClickListener {
	//当用户被挤掉时,显示这个对话框
	private ZhaoBiaoDialog exitDialog;
	private ZhaoBiaoDialog confirmExitDialog;
	private ZhaoBiaoDialog yueNotEnoughDialog;
	 private ZhaoBiaoDialog updateMessageDialog;
	private ZhaoBiaoDialog autoSettingDialog;
	private ZhaoBiaoDialog accountExpireDialog;
	//由于更新所导致的强制退出对话框
	private ZhaoBiaoDialog exitForUpdateDialog;
	private TitleMessageBarLayout tbl;
	private SegmentControl mSegmentControl;// head中间服务与休息选择器
	private ImageView refreshbutton; // head右侧消息按钮
	private ImageView userbutton; // head左侧用户按钮 
	private PullToRefreshListView mPullToRefreshListView; // 抢单列表
	private GrabListViewModel listViewModel; // 抢单列表的viewModel
	private KnockViewModel knockViewModel;//抢单的viewModel
	private PushToPassBean passBean;//抢单流程传递参数
	private PopAdapter adapter; // 列表适配
	private RelativeLayout myOrder; // 抢单弹窗
	private RelativeLayout mywallet;//我的钱包
	private ImageView iv_refresh;// 刷新按钮
	private RelativeLayout help;
	private RelativeLayout rl_no_bid;//没有标地时的提示
	private View about;
	private View rl_exit;
	private TextView tv_yue;
	private YuEViewModel yuEViewModel;
	private BiddingApplication app;

	private UpdateViewModel updateViewModel;

	private UpdateManager updateManager;
	private TextView tv_unread;
	private TextView smallred;
	private TextView tv_userCompany;
	protected View layout_back_head;
	@SuppressLint("NewApi")
	private LoadingProgress loading;
	protected Handler handler;
	private LogoutViewModel lvm;
	private ScreenReceiver receiver;
	KeyguardManager keyguardManager;
	KeyguardManager.KeyguardLock keyguardLock;
	private DrawerLayout   dl_main_drawer;
	private SwipeRefreshLayout srl;
	private RelativeLayout navigation_rl;
	private RelativeLayout sliding_settings;
	private ProgressDialog progressDialog;
	private MainPresenter  mainPresenter;
	private View           rl_qd;

	//private AccountExpireVM accountExpireVM;

	private ViewStub       viewStub_no_data;
	private View root;
	private MyCustomDialog popDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initExitForUpdateDialog();
		//wjl
		mainPresenter   = new MainPresenter();
		progressDialog  = new QDWaitDialog(this);
		keyguardManager = (KeyguardManager)getApplication().getSystemService(KEYGUARD_SERVICE);
		keyguardLock = keyguardManager.newKeyguardLock("");
		//检查第一次登陆有这个提示，dialog
		/*if(isFirstLogin()){
			exitForUpdateDialog.show();
	 	}*/
		app = (BiddingApplication) getApplication();
		app.registerNetStateListener();
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initControl();
		initEvent();
		listViewModel = new GrabListViewModel(this, this);
		yuEViewModel = new YuEViewModel(this, this);
		//accountExpireVM = new AccountExpireVM(this,this);
		updateViewModel = new UpdateViewModel(this, this);
		lvm = new LogoutViewModel(this, this);
		adapter = new PopAdapter(this, this);
		ListView mListView = mPullToRefreshListView.getRefreshableView();
		mListView.setDividerHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));

		registerScreenOffReceiver();
		configExitDialog();
		getWindow().setBackgroundDrawable(null);
		startBindService();
		initAutoSettingDialog();
		String expireState = SPUtils.getVByK(this, GlobalConfigBean.KEY_WLT_EXPIRE);
		String expireMsg   = SPUtils.getVByK(this,GlobalConfigBean.KEY_WLT_EXPIRE_MSG);
		onLoadingSuccess(new AccountExpireBean(expireState,expireMsg));

	}

	private void initExitForUpdateDialog() {
		exitForUpdateDialog = new ZhaoBiaoDialog(this,"提示","版本已升级，本次更新修复了一些bug，进一步优化了弹窗播报的接收。请重新登录，以使更新生效！");
		exitForUpdateDialog.setCancelButtonGone();
		exitForUpdateDialog.setOnDialogClickListener(new onDialogClickListener() {
			@Override
			public void onDialogOkClick() {
				exitForUpdateDialog.dismiss();
				lvm.logout();
				SharedPreferencesUtils.clearLoginToken(getApplicationContext());
				//退出时注销个推
				GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
				//退出时注销小米推送
				MiPushClient.unsetAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
				UserUtils.clearUserInfo(getApplicationContext());
				Toast.makeText(MainActivity.this, getString(R.string.logout_now), Toast.LENGTH_SHORT).show();
				ActivityUtils.goToActivity(MainActivity.this, LoginActivity.class);
				finish();
				SPUtils.saveAlreadyMainActivity(MainActivity.this);

			}
			@Override
			public void onDialogCancelClick() {

			}
		});

	}

	/**
	 * 初始化自定义设置界面的dialog
	 */
	private void initAutoSettingDialog() {

		autoSettingDialog = new ZhaoBiaoDialog(this,"自定义设置","是否需要进行自定义的接单设置?");
		autoSettingDialog.setOnDialogClickListener(new onDialogClickListener() {
			@Override
			public void onDialogOkClick() {
				autoSettingDialog.dismiss();
				mainPresenter.goToAutoSettings(MainActivity.this);
				SPUtils.saveAutoSetting(MainActivity.this);
			}

			@Override
			public void onDialogCancelClick() {
				autoSettingDialog.dismiss();
				SPUtils.saveAutoSetting(MainActivity.this);
			}
		});
		accountExpireDialog = new ZhaoBiaoDialog(this,"提示","");
		accountExpireDialog.setCancelButtonGone();
		accountExpireDialog.setOnDialogClickListener(new onDialogClickListener() {
			@Override
			public void onDialogOkClick() {
				accountExpireDialog.dismiss();
			}

			@Override
			public void onDialogCancelClick() {

			}
		});
	}

	/**
	 * 开启myService的进程
	 */
	private void startBindService() {
		Intent intent = new Intent(this, MyService.class);
		startService(intent);
	}


	/**
	 * 配置退出的对话框
	 */
	private void configExitDialog() {
		exitDialog = new ZhaoBiaoDialog(this,getString(R.string.sys_noti),getString(R.string.force_exit));
		exitDialog.setOnDialogClickListener(this);
		exitDialog.setCancelButtonGone();
		exitDialog.setCancelable(false);
	}



	/**
	 * 判断是不是第一次登陆进来---
	 * 如果是第一次，就返回true,并且置为false
	 * 否则直接置为false
	 * @return默认为true
	 */
	private boolean isFirstLogin() {
		return SPUtils.isFirstMainActivity(this);
	}

	/**
	 * 更新后第一次进入
	 * @return
	 */
	private boolean isUpdateFirst(){
		Log.e("shenzhixinUI", "is:" + SPUtils.isFirstUpdate(this));
		return SPUtils.isFirstUpdate(this);
	}
	public void loadDatas() {
		if (listViewModel != null)
			listViewModel.refresh();
	}

	@Override
	protected void onDestroy() {

		app.unRegisterNetStateListener();//解除网络的变化Listener
		app.stopTimer();//停止文件的上传
		if(updateManager!=null){//bug:防止activity被无故杀死时dialog造成内存泄露
			updateManager.dismissConfirmDownloadDialog();
			updateManager.cancelDownloading(this);
		}
		unregisterScreenOffReceiver();
		super.onDestroy();
		releaseSource();
		System.gc();
	}

	private void releaseSource() {

	}

	@Override
	protected void onResume() {

		UserConstans.USER_ID = UserUtils.getUserId(this);
		UserUtils.getUserCompany(this);
		//accountExpireVM.validateAccount();
		//获得锁的权限
	    LogUtils.LogE("ashen", "onResume...");
		LogUtils.LogE("ashen", "lock...");
		listViewModel.refresh();
		readNumbers();
		if(updateManager==null){
			updateViewModel.checkVersion();
		}
		if(updateManager!=null && !updateManager.isDownloadDialogShowing()){
			updateViewModel.checkVersion();
		}
		app.setCurrentNotificationListener(this);
		NetStateManager.getNetStateManagerInstance().setINetStateChangedListener(this);
		tbl.setVisibility(View.GONE);
		if(NetUtils.isNetworkConnected(this)){
			NetConnected();
		}else{
			NetDisConnected();
		}

		super.onResume();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	// 初始化组建
	public void initControl() {
		rl_qd              = findViewById(R.id.rl_qd);
		sliding_settings   = (RelativeLayout) findViewById(R.id.sliding_settings);
		viewStub_no_data   = (ViewStub) findViewById(R.id.viewStub_no_data);
		srl                = (SwipeRefreshLayout) findViewById(R.id.srl);
		navigation_rl      = (RelativeLayout) findViewById(R.id.navigation_rl);
		dl_main_drawer     = (DrawerLayout) findViewById(R.id.dl_main_drawer);
		layout_back_head = findViewById(R.id.layout_head);
		tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);
		mSegmentControl = (SegmentControl) findViewById(R.id.segment_control);
		mywallet          = (RelativeLayout) findViewById(R.id.mywallet);
		//初始化服务模式
		StateUtils.state = 1;
		tbl.setVisibility(View.GONE);
		tbl.setTitleBarListener(this);
		refreshbutton = (ImageView) this.findViewById(R.id.refreshbutton);
		userbutton = (ImageView) this.findViewById(R.id.userbutton);
		mPullToRefreshListView = (PullToRefreshListView) this
				.findViewById(R.id.mainlist);
		myOrder = (RelativeLayout) this.findViewById(R.id.myorder);
		help = (RelativeLayout) findViewById(R.id.help);
		about = findViewById(R.id.about);
		PullToRefreshListViewUtils.initListView(mPullToRefreshListView);
		// shenzhx
		iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
		rl_exit = findViewById(R.id.rl_exit);
		tv_yue = (TextView) findViewById(R.id.tv_yue);
		tv_unread = (TextView)findViewById(R.id.tv_unread);
		smallred = (TextView)findViewById(R.id.smallred);
		tv_userCompany = (TextView)findViewById(R.id.tv_userCompany);
		tv_userCompany.setText(UserUtils.getUserCompany(this));
		readNumbers();
		setNavigationHeight(navigation_rl);
	}

	private void setNavigationHeight(RelativeLayout navigation_rl) {
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		ViewGroup.LayoutParams layoutParams = navigation_rl.getLayoutParams();
		layoutParams.height = screenHeight-50;
		navigation_rl.setLayoutParams(layoutParams);

	}



	// 初始化事件监听
	public void initEvent() {
		srl.setColorSchemeResources
				(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
		srl.setProgressBackgroundColor(R.color.red);
		srl.setProgressViewEndTarget(true, 150);
		if("1".equals(SPUtils.getServiceState(this))) {
			mSegmentControl.service(0);
		}else{
			mSegmentControl.service(1);
		}
		mSegmentControl
				.setmOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
					@Override
					public void onSegmentControlClick(int index) {
						switch (index){
							case 0://服务模式
								BDMob.getBdMobInstance().onMobEvent(MainActivity.this,BDEventConstans.EVENT_ID_SERVICE_MODE);
								break;
							case 1:
								BDMob.getBdMobInstance().onMobEvent(MainActivity.this,BDEventConstans.EVENT_ID_REST_MODE);
								break;
						}
						onChangeView(index);
					}
				});
		refreshbutton.setOnClickListener(new OnClickListener() {// 创建监听对象
			public void onClick(View v) {
				//跳转到我的订单中心
				BDMob.getBdMobInstance().onMobEvent(MainActivity.this,BDEventConstans.EVENT_ID_MY_BIDDING);
				ActivityUtils.goToActivity(MainActivity.this, OrderListActivity.class);
			}
		});
		userbutton.setOnClickListener(new OnClickListener() {// 创建监听对象
			public void onClick(View v) {
				dl_main_drawer.openDrawer(GravityCompat.START);
			}
		});
		configListViewRefreshListener();

		myOrder.setOnClickListener(new OnClickListener() {// 创建监听对象
			public void onClick(View v) {
				// shenzhixin 2015-6-25 注释 写到自己的业务逻辑里面
				ActivityUtils.goToActivity(MainActivity.this,
						MessageCenterActivity.class);
			}
		});
		help.setOnClickListener(this);
		about.setOnClickListener(this);
		rl_exit.setOnClickListener(this);
		iv_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//获取余额
				BDMob.getBdMobInstance().onMobEvent(MainActivity.this, BDEventConstans.EVENT_ID_MANUAL_REFRESH_BALANCE);
				yuEViewModel.getBalance();
				tv_yue.setText(R.string.fetching);
				// 要判断是否在刷新中，如果已经在刷新中就不要在刷新了，出一个提示就可以
				// 刷新，去取数据，取到后赋值。ui也要变化，iv消失，pb出现，等结束后再恢复。
				MDUtils.myUserCenterMD(MainActivity.this, MDConstans.ACTION_REFRESH_YUE);
			}
		});

		dl_main_drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				yuEViewModel.getBalance();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
			}

			@Override
			public void onDrawerStateChanged(int newState) {
			}
		});
		sliding_settings.setOnClickListener(this);
		mywallet.setOnClickListener(this);
	}

	/**
	 * 配置listView的下拉事件上拉下拉都可以
	 */
	private void configListViewRefreshListener() {
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (refreshView.isHeaderShown()) {
						} else {
							listViewModel.loadMore();
							MDUtils.servicePageMD(MainActivity.this, "0", "0", MDConstans.ACTION_LOAD_MORE_REFRESH);
						}
					}
				});
		ListView listView = mPullToRefreshListView.getRefreshableView();
		srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				BDMob.getBdMobInstance().onMobEvent(MainActivity.this,BDEventConstans.EVENT_ID_BIDDING_LIAT_PAGE_MANUAL_REFRESH);
				listViewModel.refresh();
				MDUtils.servicePageMD(MainActivity.this, "0", "0", MDConstans.ACTION_PULL_TO_REFRESH);
			}
		});
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				View firstView = view.getChildAt(firstVisibleItem);
				//	Log.e("shenzhixinqqqq","firstVisible:"+firstVisibleItem+",firstView:"+firstView);
				if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
					srl.setEnabled(true);
				} else {
					srl.setEnabled(false);
				}
			}
		});
	}

	public void configListViewCannotLoadMore(){
		mPullToRefreshListView
		.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					String label = DateUtils.formatDateTime(
							getApplicationContext(),
							System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME
									| DateUtils.FORMAT_SHOW_DATE
									| DateUtils.FORMAT_ABBREV_ALL);
					refreshView.getLoadingLayoutProxy()
							.setLastUpdatedLabel(label);
					listViewModel.refresh();
					MDUtils.servicePageMD(MainActivity.this, "0", "0", MDConstans.ACTION_PULL_TO_REFRESH);
				} else {
					if (handler == null)
						handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPullToRefreshListView.onRefreshComplete();
						}
					}, 500);
				}
			}
		});
	}
	//侧滑栏小红点，头像小红点
	private void readNumbers() {
		int num = UnreadUtils.getAllNum(MainActivity.this);
		if(num==0){
			tv_unread.setVisibility(View.GONE);
			smallred.setVisibility(View.GONE);
		}else{
			smallred.setVisibility(View.VISIBLE);
			smallred.setText(num + "");
			tv_unread.setVisibility(View.VISIBLE);
			tv_unread.setText(num + "");
			if (num > 99) {
				smallred.setText("99+");
				tv_unread.setText("99+");
			}
		}
	}

	// 判断选择的是服务还是休息
	private void onChangeView(int index) {
		try {
			// 测试界面，实际开发中是从layout中读取的，下同。
			switch (index) {
			case 0:
				StateUtils.state = 1;
				break;
			case 1:
				StateUtils.state  = 2;
				break;
			default:
				break;
			}
			SPUtils.setServiceState(this,StateUtils.state+"");
			listViewModel.refresh();
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.mywallet://点击了我的钱包
				ActivityUtils.goToActivity(this, MyWalletActivity.class);
				break;
			case R.id.rl_no_bid://点击刷新
				listViewModel.refresh();
				break;
		case R.id.help:// 跳到帮助
			ActivityUtils.goToActivity(this, HelpActivity.class);
			break;
		case R.id.about:// 关于
			ActivityUtils.goToActivity(this, AboutActivity.class);
			break;
		case R.id.rl_exit:// 退出
			BDMob.getBdMobInstance().onMobEvent(this,BDEventConstans.EVENT_ID_LOGOUT);
			confirmExitDialog = new ZhaoBiaoDialog(this, getString(R.string.hint), getString(R.string.logout_make_sure));
			confirmExitDialog.setOnDialogClickListener(new onDialogClickListener() {
				@Override
				public void onDialogOkClick() {
					confirmExitDialog.dismiss();
					lvm.logout();
					SharedPreferencesUtils.clearLoginToken(getApplicationContext());
					//退出时注销个推
					GePushProxy.unBindPushAlias(getApplicationContext(),UserUtils.getUserId(getApplicationContext()));
					//退出时注销小米推送
					MiPushClient.unsetAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
					UserUtils.clearUserInfo(getApplicationContext());
					Toast.makeText(MainActivity.this, getString(R.string.logout_now), Toast.LENGTH_SHORT).show();
					MDUtils.myUserCenterMD(MainActivity.this, MDConstans.ACTION_EXIT);
					ActivityUtils.goToActivity(MainActivity.this, LoginActivity.class);
					finish();
				}
				@Override
				public void onDialogCancelClick(){
					confirmExitDialog.dismiss();
				}
			});
				confirmExitDialog.show();
			break;
			case R.id.sliding_settings://设置,跳转到Settings页面
				Intent intent = SettingsActivity.onNewIntent(this);
				startActivity(intent);
				break;
		}
	}

	@Override
	public void onLoadingStart() {
		startLoading();
	}

	@SuppressLint("ShowToast")
	@Override
	public void onLoadingSuccess(Object t) {
		if(t instanceof AccountExpireBean){
			AccountExpireBean accountExpireBean = (AccountExpireBean) t;
			String expireState = accountExpireBean.getExpireState();
			if("1".equals(expireState)){
				//网灵通过期
				accountExpireDialog.setMessage(accountExpireBean.getMsg());
				accountExpireDialog.show();
			}
		}
		if (t instanceof Map<?, ?>) {
			Map<String, String> maps = (Map<String, String>) t;
			String balance = maps.get("balance");

			if(!TextUtils.isEmpty(balance)){
				tv_yue.setText(balance);
				stopLoading();
			}else{
				updateManager = UpdateManager.getUpdateManager();
				String currentVersion = maps.get("currentVersion");
				String url = maps.get("url");
				try {
					boolean flag = updateManager.isUpdateNow(this, currentVersion,url);
					if(!flag){
						//判断是不是第一次进入主界面
						showFirst();
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
					finish();
				}
			}
		}
		if (t instanceof Integer){
			int status = (Integer) t;
			Toast.makeText(MainActivity.this,"status:"+status,Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("passBean", passBean);
			intent.putExtras(bundle);
			dismissQDWaitDialog();
			if(null!=popDialog)
				popDialog.dismiss();
			if(status==3){//抢单成功
				intent.setClass(MainActivity.this, BidSuccessActivity.class);
				startActivity(intent);
				Toast.makeText(MainActivity.this,"抢单成功",Toast.LENGTH_SHORT).show();
			}
			else if(status==1){
				Log.e("shenzhixin","bidGOne");
				intent.setClass(MainActivity.this, BidGoneActivity.class);
				startActivity(intent);
			}
			else if(status==2) {
				yueNotEnoughDialog = new ZhaoBiaoDialog(this, getString(R.string.hint),getString(R.string.not_enough_balance));
				yueNotEnoughDialog.setCancelButtonGone();
				yueNotEnoughDialog.setOnDialogClickListener(new onDialogClickListener() {
					@Override
					public void onDialogOkClick() {
						yueNotEnoughDialog.dismiss();
					}
					@Override
					public void onDialogCancelClick() {
					}
				});
				yueNotEnoughDialog.show();
				if(passBean!=null){
					MDUtils.YuENotEnough(passBean.getCateId()+"", passBean.getBidId()+"");
				}
			}
			else if(status==4){
				Toast.makeText(MainActivity.this, getString(R.string.bidding_already_bid), Toast.LENGTH_SHORT).show();
			}else if(status==5){//抢单失败
				intent.setClass(MainActivity.this, BidFailureActivity.class);
				startActivity(intent);
				Toast.makeText(MainActivity.this,"抢单并没有成功",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(MainActivity.this, getString(R.string.bidding_exception), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onLoadingError(String msg) {
		mPullToRefreshListView.onRefreshComplete();
		if(srl!=null && srl.isRefreshing()) {
			srl.setRefreshing(false);
		}
		stopLoading();
		if(!TextUtils.isEmpty(msg)){
			Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
		}
		dismissQDWaitDialog();
	}

	@Override
	public void onLoadingCancel() {
		mPullToRefreshListView.onRefreshComplete();
		stopLoading();
	}

	@Override
	public void onRefreshSuccess(Object t) {
		srl.setRefreshing(false);
		stopLoading();
		List<QDBaseBean> list = (List<QDBaseBean>)t;
		adapter.refreshSuccess(list);
		mPullToRefreshListView.onRefreshComplete();
		if(null==list || list.size()==0){
			//加载ViewStub
			if(root==null) {
				root = viewStub_no_data.inflate();
				root.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						listViewModel.refresh();
					}
				});
			}
				root.setVisibility(View.VISIBLE);

		}else{
			if(root!=null){
				root.setVisibility(View.GONE);
			}
		}
		//为获取下一页列表数据添加方法
		QDBaseBean bean;
		if(null!=list&&list.size()>0){
			bean = list.get(list.size()-1);
			BidListUtils.bidId = bean.getBidId();
			BidListUtils.pushId = bean.getPushId();
			BidListUtils.bidState = bean.getBidState();
		}
		//判断是否可以继续加载更多
		if(null==list || list.size()==0 || list.size()%20!=0){
			canNotLoad();
		}
		else{
			canLoad();
		}
	}

	public void canLoad() {
		PullToRefreshListViewUtils.PullToListViewCanLoadMore(mPullToRefreshListView);
		configListViewRefreshListener();
	}

	public void canNotLoad() {
		PullToRefreshListViewUtils.PullToListViewCannotLoadMore(mPullToRefreshListView);
		configListViewCannotLoadMore();
	}

	@Override
	public void onLoadingMoreSuccess(Object t) {
		List<QDBaseBean> list = (List<QDBaseBean>)t;
		adapter.loadMoreSuccess(list);
		mPullToRefreshListView.onRefreshComplete();
		stopLoading();
		//为获取下一页列表数据添加方法
		QDBaseBean bean;
		if(null!=list&&list.size()>0){
			bean = list.get(list.size()-1);
			BidListUtils.bidId    = bean.getBidId();
			BidListUtils.pushId   = bean.getPushId();
			BidListUtils.bidState = bean.getBidState();
		}
		//判断是否可以继续加载更多
		if(null==list||list.size()==0||list.size()%20!=0){
			canNotLoad();
		}
		else{
			canLoad();
		}

		/**
		 *
		 */
	}

	private void dismissQDWaitDialog(){
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		rl_qd.setVisibility(View.GONE);
	}

	@Override
	public void onNoInterNetError() {
		Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		stopLoading();
		dismissQDWaitDialog();
	}

	@Override
	public void onLoginInvalidate() {
		//Toast.makeText(this, "错误了", 0).show();
		GePushProxy.unBindPushAlias(getApplicationContext(),UserUtils.getUserId(getApplicationContext()));
		showExitDialog();
	}


	@Override
	protected void onPause() {

		super.onPause();
		app.removeINotificationListener();
		NetStateManager.getNetStateManagerInstance().removeINetStateChangedListener();

	}


	@Override
	public void onTitleBarClicked(TitleBarType type) {
	}

	@Override
	public void onTitleBarClosedClicked() {
		tbl.setVisibility(View.GONE);
	}

	@Override
	public void NetConnected() {
		if(tbl.getType()==TitleBarType.NETWORK_ERROR){
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
				tbl.showNetError();
				tbl.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onAdapterRefreshSuccess() {
		mPullToRefreshListView.setAdapter(adapter);
	}

	@Override
	public void onAdapterLoadMoreSuccess() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onAdapterViewClick(int id,PushToPassBean bean) {
		//点击了抢单
		BDMob.getBdMobInstance().onMobEvent(this,BDEventConstans.EVENT_ID_BIDDING_LIST_PAGE_BIDDING);
			passBean = bean;
			knockViewModel = new KnockViewModel(MainActivity.this, MainActivity.this);
			knockViewModel.knock(bean, AppConstants.BIDSOURCE_LIST);
			rl_qd.setVisibility(View.VISIBLE);
	}

	@Override
	public void onNotificationCome(PushBean pushBean) {
		if(listViewModel!=null)
			listViewModel.refresh();
		if (null != pushBean) {
			int type = pushBean.getTag();
			Log.e("GetuiSdkDemo", "type:" + type + ",state:" + StateUtils.getState(MainActivity.this));
			if (type == 100 && StateUtils.getState(MainActivity.this) == 1) {
				Intent intent = new Intent(this,PushInActivity.class);
				startActivity(intent);
			}
			else{
				tbl.setPushBean(pushBean);
				tbl.setVisibility(View.VISIBLE);
				PushUtils.pushList.clear();
			}
		}else{
			Toast.makeText(this,"实体bean为空",Toast.LENGTH_SHORT).show();
		}
		readNumbers();
	}

	/**
	 * 加载效果
	 */
	public void startLoading() {
		if (loading == null) {
			loading = new LoadingProgress(MainActivity.this,
					R.style.loading);
		}


		try {
			loading.show();
		} catch (RuntimeException e) {
			loading = null;
		}
	}

	/**
	 * 对话框消失
	 */
	public void stopLoading() {
		if (!this.isFinishing() && loading != null && loading.isShowing()) {
			loading.dismiss();
			loading = null;
		}
	}






	//因为规则改动MainActivity屏蔽能不能加载更多的方法
	@Override
	public void canLoadMore() {

	}

	@Override
	public void loadMoreEnd() {

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

	@Override
	public void onDialogOkClick() {
		dismissExitDialog();

	}

	@Override
	public void onDialogCancelClick() {

	}

	private class ScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.LogE("shenzhixin", "action:" + intent.getAction());
			//getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			if (intent == null) return;
			if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
				KeyguardUtils.SCREEN_ON = true;
				KeyguardUtils.need_lock = false;
				LogUtils.LogE("shenzhixin", "screenOn:" + KeyguardUtils.need_lock);

			} else {
				KeyguardUtils.need_lock = true;
				KeyguardUtils.SCREEN_ON = false;
				KeyguardUtils.notLock = false;
				openKeyguard();
				closeKeyguard();
				LogUtils.LogE("shenzhixin", "screenOff:" + KeyguardUtils.need_lock);
				BiddingApplication biddingApplication = (BiddingApplication) getApplication();
				if(biddingApplication.activity instanceof LockActivity) {
					LockActivity lockActivity = (LockActivity) biddingApplication.activity;
					if (lockActivity != null) {
						LogUtils.LogE("shenzhixin", "lock:" + (lockActivity == null));
						lockActivity.closeLock();
					}
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
	 * 显示退出的对话框
	 */
	private void showExitDialog(){
		if(exitDialog!=null && !exitDialog.isShowing()){
			try {
				exitDialog.show();
			}catch (Exception e){
				Toast.makeText(this,getString(R.string.force_exit),Toast.LENGTH_SHORT).show();
				//TODO:退出登录ActivityUtils.goToActivity(MainActivity.this, LoginActivity.class);
				finish();
				//退出登录后的几件事
				/**
				 * 1.清除LoginToken
				 * 2.清除用户信息
				 */
				SharedPreferencesUtils.clearLoginToken(this);
				UserUtils.clearUserInfo(this);
			}

		}
	}

	/**
	 * 显示退出的对话框
	 */
	private void dismissExitDialog(){
		if(exitDialog!=null && exitDialog.isShowing()){
			try {
				exitDialog.dismiss();
			}catch (Exception e){
				Toast.makeText(this,getString(R.string.force_exit),Toast.LENGTH_SHORT).show();
				//TODO:退出登录
			}finally {
				ActivityUtils.goToActivity(MainActivity.this, LoginActivity.class);
				finish();
				//退出登录后的几件事
				/**
				 * 1.清除LoginToken
				 * 2.清除用户信息
				 */
				SharedPreferencesUtils.clearLoginToken(this);
				UserUtils.clearUserInfo(this);

			}
		}
	}


	/**
	 * 第一次登录时的提示
	 */
	private void showFirst(){
		boolean flag = false;
		if (flag) {//需要弹窗
			//弹对话框
			updateMessageDialog = new ZhaoBiaoDialog(this, getString(R.string.update_hint), getString(R.string.update_message));
			updateMessageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					//弹自定义界面的弹
					if(SPUtils.isAutoSetting(MainActivity.this)) {
						autoSettingDialog.show();
					}
				}
			});
			updateMessageDialog.setCancelable(false);
			updateMessageDialog.setCancelButtonGone();
			updateMessageDialog.setOnDialogClickListener(new onDialogClickListener() {
				@Override
				public void onDialogOkClick() {
					updateMessageDialog.dismiss();
					SPUtils.saveAlreadyFirstUpdate(MainActivity.this);
				}

				@Override
				public void onDialogCancelClick() {
				}
			});
			updateMessageDialog.show();

		}else{//直接弹自定义设置那块几面
			if(SPUtils.isAutoSetting(this)) {
				autoSettingDialog.show();
			}
		}
	}
}
