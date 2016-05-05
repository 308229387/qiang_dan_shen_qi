package com.huangyezhaobiao.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangye.commonlib.utils.UserConstans;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.TelephoneBean;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.fragment.DoneServiceFragment;
import com.huangyezhaobiao.fragment.OnServiceFragment;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
import com.huangyezhaobiao.fragment.ReadyServiceFragment;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;
import com.huangyezhaobiao.netmodel.NetStateManager;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.utils.ViewPageHelper;
import com.huangyezhaobiao.view.CustomViewPager;
import com.huangyezhaobiao.view.QDWaitDialog;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.TitleMessageBarLayout.OnTitleBarClickListener;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;
import com.huangyezhaobiao.vm.KnockViewModel;
import com.huangyezhaobiao.vm.TelephoneVModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单页面，由3个fragment+viewpager组成
 *
 * @author shenzhx
 *
 *         注意: 1.viewpager+fragment组合常见问题(销毁，何时加载，手势冲突)
 *         2.每个界面都是从网络获取数据展示，网络层的东西--封装 3.listView+adapter结合 4.待定
 *
 */
public class OrderListActivity extends CommonFragmentActivity implements
		OnClickListener, INotificationListener, OnTitleBarClickListener,
		INetStateChangedListener, NetWorkVMCallBack, onDialogClickListener {
	private static final int INVALID_WIDTH = -1;
	private static final int INVALID_INDEX = INVALID_WIDTH;
	private static final int FRAGMENT_COUNTS = 3;
	private static final int ALL_INDEX = INVALID_INDEX + 1;
	private static final int CONTACT_INDEX = ALL_INDEX + 1 - 1;
	private static final int ENSURE_INDEX = CONTACT_INDEX + 1;
	private static final int DONE_INDEX = ENSURE_INDEX + 1;
	private TitleMessageBarLayout tbl;
	private LinearLayout back_layout;
	private RelativeLayout /* rl_all, */rl_contact, rl_ensure, rl_done;
	private TextView /* tv_all, */tv_contact, tv_ensure, tv_done;
	private View line_fragment_title;
	private CustomViewPager viewpage_fragment;
	private int line_width = INVALID_WIDTH;
	private ViewPageAdapter adapter;
	private List<Fragment> fragments_sources = new ArrayList<Fragment>();
	private int mCurrentIndex = 0;
	private OnServiceFragment f;
	private BiddingApplication app;
	public  TextView message, txt_head;
	private KnockViewModel knockViewModel;
	private ZhaoBiaoDialog dialog;
	private PushToPassBean passBean;
	private View layout_back_head;
	private ZhaoBiaoDialog exitDialog;
	private TabLayout tabLayout;
	private String[] msg = {"待服务","服务中","已结束"};
	private ProgressDialog progressDialog;
	private TelephoneVModel tViewModel;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		app = (BiddingApplication) getApplication();
		initEnvironment();
		setContentView(R.layout.activity_szx_myorder);
		initView();
		registerViewListener();
		initDatas();
		configViewPager();
		bindListener();
		configUIStyle(CONTACT_INDEX);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			int height = Utils.getStatusBarHeight(this);
			int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
			LogUtils.LogE("shenzhixin", "layout_back_head..." + (layout_back_head == null) + ",height:" + height);
			if (layout_back_head != null) {
				layout_back_head.setPadding(0, height + more, 0, 0);
			}
		}
		configExitDialog();
		getWindow().setBackgroundDrawable(null);
		//注册EventBus
		EventbusAgent.getInstance().register(this);
		initTeleVm();
	}

	private void initTeleVm() {
		tViewModel = new TelephoneVModel(null, this);
	}

	/**
	 * 打电话的点击事件的回调，需要接口
	 * @param action
	 */
	public void onEventMainThread(EventAction action) {
		switch (action.getType()){
			case EVENT_TELEPHONE_FROM_LIST://打电话
				if(action.getData() instanceof  TelephoneBean) {
					TelephoneBean bean = (TelephoneBean) action.getData();
					if(tViewModel!=null){
						tViewModel.telephone(bean.getOrderId(),bean.getSource());
					}
				}
				break;
		}

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		//解绑EventBus
		EventbusAgent.getInstance().unregister(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 配置强制退出的dialog
	 */
	private void configExitDialog() {
		exitDialog = new ZhaoBiaoDialog(this,getString(R.string.sys_noti),getString(R.string.force_exit));
		exitDialog.setOnDialogClickListener(this);
		exitDialog.setCancelButtonGone();
		exitDialog.setCancelable(false);
	}

	@Override
	protected void onPause() {
		app.removeINotificationListener();
		NetStateManager.getNetStateManagerInstance()
				.removeINetStateChangedListener();
		super.onPause();

	}

	protected void onResume() {
		UserConstans.USER_ID = UserUtils.getUserId(this);
		super.onResume();
		tbl.setVisibility(View.GONE);
		app.setCurrentNotificationListener(this);
		NetStateManager.getNetStateManagerInstance()
				.setINetStateChangedListener(this);
		if (NetUtils.isNetworkConnected(this)) {
			NetConnected();
		} else {
			NetDisConnected();
		}

	}

	/**
	 * 监听事件
	 */
	private void bindListener() {
		back_layout.setOnClickListener(this);
		// rl_all.setOnClickListener(this);
		rl_contact.setOnClickListener(this);
		rl_done.setOnClickListener(this);
		rl_ensure.setOnClickListener(this);
	}

	/**
	 * 对viewpager进行配置
	 */
	private void configViewPager() {
		tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
		adapter = new ViewPageAdapter(getSupportFragmentManager());
		viewpage_fragment.setAdapter(adapter);
		viewpage_fragment.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mCurrentIndex = position;
				configUIStyle(position);
				f.setFromX(position);

				switch (position){
					case 0:
						BDMob.getBdMobInstance().onMobEvent(OrderListActivity.this,BDEventConstans.EVENT_ID_UN_SERVICE_TAB);
						break;
					case 1:
						BDMob.getBdMobInstance().onMobEvent(OrderListActivity.this,BDEventConstans.EVENT_ID_IN_SERVICE_TAB);
						break;
					case 2:
						BDMob.getBdMobInstance().onMobEvent(OrderListActivity.this,BDEventConstans.EVENT_ID_DONE_SERVICE_TAB);
						break;
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
									   int positionOffsetPixels) {
				ViewPageHelper.goLine(line_fragment_title, position,
						positionOffset, line_width);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		tabLayout.setupWithViewPager(viewpage_fragment);//将TabLayout和ViewPager关联起来。
		tabLayout.setTabsFromPagerAdapter(adapter);//给Tabs设置适配器


	}

	/**
	 * 把这四个fragment加入到集合中
	 */
	private void initDatas() {
		f = new OnServiceFragment();
		addFragment(new ReadyServiceFragment());
		addFragment(f);
		addFragment(new DoneServiceFragment());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//super.onSaveInstanceState(outState); annotation for fix bug #496

	}

	private void addFragment(Fragment fragment) {
		fragments_sources.add(fragment);
	}

	/**
	 * 给view来绑定listener
	 */
	private void registerViewListener() {
		rl_contact.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						rl_contact.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						line_width = rl_contact.getWidth();
						LayoutParams params = (LayoutParams) line_fragment_title
								.getLayoutParams();
						params.width = line_width;
						line_fragment_title.setLayoutParams(params);
					}
				});
	}

	/**
	 * 寻找view
	 */
	private void initView() {
		progressDialog = new QDWaitDialog(this);
		tabLayout = findView(R.id.tabLayout);
		layout_back_head = findView(R.id.layout_back);
		back_layout = findView(R.id.back_layout);
		// rl_all = findView(R.id.rl_all);
		rl_contact = findView(R.id.rl_contact);
		rl_done = findView(R.id.rl_done);
		rl_ensure = findView(R.id.rl_ensure);
		line_fragment_title = findView(R.id.line_fragment_title);
		viewpage_fragment = findView(R.id.viewpage_fragment);
		viewpage_fragment.setPagingEnabled(true);
		viewpage_fragment.setOffscreenPageLimit(2);
		tv_contact = findView(R.id.tv_contact);
		tv_done = findView(R.id.tv_done);
		tv_ensure = findView(R.id.tv_ensure);
		tbl = findView(R.id.tbl);
		message = (TextView) findViewById(R.id.message);
		txt_head = findView(R.id.txt_head);
		txt_head.setText(R.string.my_bidding);
		tbl.setTitleBarListener(this);

	}

	/**
	 * 进行一些界面环境的配置
	 */
	private void initEnvironment() {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 根据资源Id找到对应的view
	 *
	 * @param res_id
	 * @return
	 */
	public <T> T findView(int res_id) {
		return (T) findViewById(res_id);
	}

	@Override
	public void onDialogOkClick() {
		dismissExitDialog();
		UserUtils.clearUserInfo(OrderListActivity.this);
		SharedPreferencesUtils.clearLoginToken(this);
		ActivityUtils.goToActivity(OrderListActivity.this, LoginActivity.class);
		finish();
	}

	@Override
	public void onDialogCancelClick() {

	}

	private class ViewPageAdapter extends FragmentStatePagerAdapter {

		public ViewPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments_sources.get(arg0);
		}

		@Override
		public int getCount() {
			return FRAGMENT_COUNTS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return msg[position];
		}
	}

	@Override
	public void onClick(View v) {
		QiangDanBaseFragment fragment = (QiangDanBaseFragment) fragments_sources.get(viewpage_fragment.getCurrentItem());
		int index = INVALID_INDEX;
		switch (v.getId()) {
			case R.id.back_layout:
				onBackPressed();
				break;
			case R.id.rl_contact://待服务
				index = CONTACT_INDEX;
				fragment.fetchYuE();
				BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_UN_SERVICE_TAB);
				break;
			case R.id.rl_done://已结束
				BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_DONE_SERVICE_TAB);
				index = DONE_INDEX;
				fragment.fetchYuE();
				break;
			case R.id.rl_ensure://服务中
				BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_IN_SERVICE_TAB);
				index = ENSURE_INDEX;
				fragment.fetchYuE();
				break;
		}
		LogUtils.LogE("viewpage", "index:" + index + "mCurrent:"
				+ mCurrentIndex);
		if (index != INVALID_INDEX && index != mCurrentIndex
				&& index <= DONE_INDEX && index >= ALL_INDEX) {
			// 满足条件，viewpage就动
			viewpage_fragment.setCurrentItem(index);
		}
	}

	/**
	 * 改变ui的样式
	 * @param position
	 */
	public void configUIStyle(int position) {
		switch (position) {
			case CONTACT_INDEX:
				tv_contact.setTextColor(getResources().getColor(R.color.white));
				tv_contact.setBackgroundResource(R.drawable.bg_qiangdan_clicked);
				tv_ensure.setTextColor(getResources().getColor(R.color.black));
				tv_ensure.setBackgroundDrawable(null);
				tv_done.setTextColor(getResources().getColor(R.color.black));
				tv_done.setBackgroundDrawable(null);
				break;
			case ENSURE_INDEX:
				tv_ensure.setTextColor(getResources().getColor(R.color.white));
				tv_ensure.setBackgroundResource(R.drawable.bg_qiangdan_clicked);
				tv_contact.setTextColor(getResources().getColor(R.color.black));
				tv_contact.setBackgroundDrawable(null);
				tv_done.setTextColor(getResources().getColor(R.color.black));
				tv_done.setBackgroundDrawable(null);
				break;
			case DONE_INDEX:
				tv_done.setBackgroundResource(R.drawable.bg_qiangdan_clicked);
				tv_done.setTextColor(getResources().getColor(R.color.gray));
				tv_contact.setTextColor(getResources().getColor(R.color.black));
				tv_contact.setBackgroundDrawable(null);
				tv_ensure.setTextColor(getResources().getColor(R.color.black));
				tv_ensure.setBackgroundDrawable(null);
				break;
		}
	}

	public void onItemClick(String id) {
		ActivityUtils.goToActivity(this, FetchDetailsActivity.class);
	}

	@Override
	public void onNotificationCome(PushBean pushBean) {
		LogUtils.LogE("szxNotification", "notification come.");
		if (null != pushBean) {
			int type = pushBean.getTag();
			LogUtils.LogE("szxNotification", "notification type:" + type + ",state:" + StateUtils.getState(OrderListActivity.this));
			if (type == 100 && StateUtils.getState(OrderListActivity.this) == 1) {
				Intent intent = new Intent(this,PushInActivity.class);
				startActivity(intent);
			} else {
				tbl.setPushBean(pushBean);
				tbl.setVisibility(View.VISIBLE);
				PushUtils.pushList.clear();
			}
		}
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
		if (tbl != null && tbl.getType() == TitleBarType.NETWORK_ERROR) {
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
				if (tbl != null) {
					tbl.showNetError();
					tbl.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onLoadingStart() {

	}


	private void dismissQDWaitDialog(){
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
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

			if (status == 3) {
				intent.setClass(OrderListActivity.this,
						BidSuccessActivity.class);
				startActivity(intent);
				Toast.makeText(OrderListActivity.this,"抢单成功",Toast.LENGTH_SHORT).show();
			} else if (status == 1) {
				intent.setClass(OrderListActivity.this, BidGoneActivity.class);
				startActivity(intent);
			} else if (status == 2) {
				dialog = new ZhaoBiaoDialog(this, getString(R.string.hint), getString(R.string.not_enough_balance));
				dialog.setCancelButtonGone();
				dialog.setOnDialogClickListener(new onDialogClickListener() {

					@Override
					public void onDialogOkClick() {
						dialog.dismiss();

					}

					@Override
					public void onDialogCancelClick() {

					}
				});
				dialog.show();
				if(passBean!=null)
					MDUtils.YuENotEnough(""+passBean.getCateId(), ""+passBean.getBidId());
			}else if(status==5){
				Toast.makeText(OrderListActivity.this,"抢单失败",Toast.LENGTH_SHORT).show();
				intent.setClass(OrderListActivity.this,
						BidFailureActivity.class);
				startActivity(intent);
			}else if(status ==4){
				Toast.makeText(OrderListActivity.this,"您已抢过此单",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(OrderListActivity.this,"抢单异常",Toast.LENGTH_SHORT).show();
			}

		}

	}

	@Override
	public void onLoadingError(String msg) {
		dismissQDWaitDialog();
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoadingCancel() {

	}

	@Override
	public void onNoInterNetError() {
		dismissQDWaitDialog();
		Toast.makeText(this,getString(R.string.no_network),Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoginInvalidate() {
		GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
		showExitDialog();
	}


	/**
	 * 显示退出登录的对话框
	 */
	public void showExitDialog(){
		if(exitDialog!=null && !exitDialog.isShowing())
		{
			try {
				exitDialog.show();
			}catch (Exception e){
				Toast.makeText(this,"出错了",Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 消失退出登录的对话框
	 */
	public void dismissExitDialog(){
		if(exitDialog!=null && exitDialog.isShowing()){
			try{
				exitDialog.dismiss();
			}catch (Exception e){

			}finally{
				//TODO:退出
				SharedPreferencesUtils.clearLoginToken(this);
				UserUtils.clearUserInfo(this);
				ActivityUtils.goToActivity(this, LoginActivity.class);
				onBackPressed();
			}
		}
	}

}
