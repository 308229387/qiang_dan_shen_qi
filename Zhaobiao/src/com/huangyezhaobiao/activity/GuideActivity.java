package com.huangyezhaobiao.activity;

		import android.annotation.SuppressLint;
		import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
		import android.support.annotation.Nullable;
		import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
		import android.widget.TextView;
		import android.widget.Toast;

		import com.huangyezhaobiao.R;
		import com.huangyezhaobiao.bean.LoginBean;
		import com.huangyezhaobiao.gtui.GePushProxy;
		import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
		import com.huangyezhaobiao.utils.HYEventConstans;
		import com.huangyezhaobiao.utils.HYMob;
		import com.huangyezhaobiao.utils.LogUtils;
		import com.huangyezhaobiao.utils.PhoneUtils;
		import com.huangyezhaobiao.utils.UserUtils;
		import com.huangyezhaobiao.utils.VersionUtils;
		import com.huangyezhaobiao.view.ZhaoBiaoDialog;
		import com.huangyezhaobiao.vm.CheckLoginViewModel;
		import com.wuba.loginsdk.external.LoginCallback;
		import com.wuba.loginsdk.external.LoginClient;
		import com.wuba.loginsdk.external.Request;
		import com.wuba.loginsdk.external.SimpleLoginCallback;
		import com.wuba.loginsdk.model.LoginSDKBean;
		import com.wuba.loginsdk.utils.ToastUtils;
		import com.xiaomi.mipush.sdk.MiPushClient;

		import java.util.ArrayList;
import java.util.List;

/**
 * 引导界面
 *
 * @author shenzhixin
 *
 */
public class GuideActivity extends CommonBaseActivity {
	private List<View> views = new ArrayList<View>();
	private ViewPager viewPager;
	private ImageView hint_indicator;
	private int width;
	private ImageView[] indicators = new ImageView[3];
	private int[] ids = { R.id.indicator1, R.id.indicator2, R.id.indicator3
//			R.id.indicator4
	};
	private float distance;
	private GuideAdapter adapter;
	private SharedPreferences sp;
	private int screenHeigh;
	private int screenWidth;
	private View view1;
	private View view2;
	private View view3;
//	private View view4;
	private ImageView ee,er,esan,esi;

	int currentVersion = -1; //当前版本号


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getScreenSize();
		initView();
		initListener();
		adapter = new GuideAdapter();
		viewPager.setAdapter(adapter);
		sp = getSharedPreferences(Constans.APP_SP, 0);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		getWindow().setBackgroundDrawable(null);

		//获取当前系统版本号
		try {
			currentVersion = Integer.parseInt(VersionUtils.getVersionCode(this));
		} catch (Exception e) {

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		HYMob.getDataList(this, HYEventConstans.GUIDE_PAGE);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_guide);
		viewPager = getView(R.id.viewPager);
		hint_indicator = getView(R.id.hint_indicator);
		hint_indicator.post(new Runnable() {
			@Override
			public void run() {
				LayoutParams params = (LayoutParams) hint_indicator
						.getLayoutParams();
				width = params.width;
				LogUtils.LogE("asas", "width:" + width);
				distance = TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 22.5f, getResources()
								.getDisplayMetrics());
			}
		});

		for (int i = 0; i < indicators.length; i++) {
			indicators[i] = getView(ids[i]);
		}
//		if (screenHeigh <= 480) {
//			view1 = LayoutInflater.from(this).inflate(R.layout.guide1_small,
//					null);
//			view2 = LayoutInflater.from(this).inflate(R.layout.guide2_small,
//					null);
//			view3 = LayoutInflater.from(this).inflate(R.layout.guide3_small,
//					null);
////			view4 = LayoutInflater.from(this).inflate(R.layout.guide4_small,
////					null);
//		} else {
			view1 = LayoutInflater.from(this).inflate(R.layout.guide1, null);
			view2 = LayoutInflater.from(this).inflate(R.layout.guide2, null);
			view3 = LayoutInflater.from(this).inflate(R.layout.guide3, null);
//			view4 = LayoutInflater.from(this).inflate(R.layout.guide4, null);
//		}
		ee   = (ImageView) view1.findViewById(R.id.ee);
		er   = (ImageView) view2.findViewById(R.id.er);
		esan = (ImageView) view3.findViewById(R.id.e3);
//		esi  = (ImageView) view4.findViewById(R.id.e4);
		views.add(view1);
		views.add(view2);
		views.add(view3);
//		views.add(view4);
		TextView rl = (TextView) view3.findViewById(R.id.rl_click);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(UserUtils.getUserId(GuideActivity.this))
						|| TextUtils.isEmpty(UserUtils.getAppVersion(GuideActivity.this))
						|| currentVersion >= 23) {// 如果没登录过

					ActivityUtils.goToActivity(GuideActivity.this, BlankActivity.class);

				} else {
					ActivityUtils.goToActivity(GuideActivity.this, MainActivity.class);
				}
				sp.edit().putBoolean("isFirst", false).commit();
				finish();
			}
		});
	}

	@Override
	public void initListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
									   int positionOffsetPixels) {
				scrollToAnim(position, positionOffset);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/**
	 * 移动动画
	 *
	 * @param position
	 * @param positionOffset
	 */
	protected void scrollToAnim(int position, float positionOffset) {
		LogUtils.LogE("ashenPager", "position:" + position + "offSet:" + positionOffset
				+ "distance:" + distance);
		LayoutParams params = (LayoutParams) hint_indicator
				.getLayoutParams();
		params.leftMargin = (int) ((distance * positionOffset) + position
				* distance);
		LogUtils.LogE("ashenPager", "leftMargin:" + params.leftMargin);
		hint_indicator.setLayoutParams(params);
	}

	private class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(views.get(position), 0);
			return views.get(position);
		}

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		//释放图片背景资源
		releaseSources();
	}

	private void releaseSources() {
		//释放背景和图片resources
		for(int index=0;index<indicators.length;index++){
			indicators[index].setImageResource(0);
		}
		view1.setBackgroundResource(0);
		view2.setBackgroundResource(0);
		view3.setBackgroundResource(0);
//		view4.setBackgroundResource(0);
		ee.setImageResource(0);
		er.setImageResource(0);
		esan.setImageResource(0);
//		esi.setImageResource(0);

	}

	public void getScreenSize() {
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeigh = dm.heightPixels;
		LogUtils.LogE("screenSize", "width:" + screenWidth + ",height:" + screenHeigh);
	}

	@Override
	protected void onStop() {
		super.onStop();
		HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_GUIDE, stop_time - resume_time);
	}
}
