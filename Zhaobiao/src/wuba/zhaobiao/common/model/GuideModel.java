package wuba.zhaobiao.common.model;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.VersionUtils;

import java.util.ArrayList;
import java.util.List;

import wuba.zhaobiao.common.activity.GuideActivity;
import wuba.zhaobiao.common.activity.HomePageActivity;
import wuba.zhaobiao.common.activity.LoginActivity;

/**
 * Created by SongYongmeng on 2016/8/1.
 */
public class GuideModel extends BaseModel implements View.OnClickListener {

    private GuideActivity context;
    private ViewPager viewPager;
    private ImageView hint_indicator;
    private GuideAdapter adapter;
    private View view1, view2, view3;
    private ImageView ee, er, esan;
    private SharedPreferences sp;

    private float distance;
    private int currentVersion = -1;

    private List<View> views = new ArrayList<View>();
    private ImageView[] indicators = new ImageView[3];
    private int[] ids = {R.id.indicator1, R.id.indicator2, R.id.indicator3};

    public GuideModel(GuideActivity context) {
        this.context = context;
    }

    public void initViewPager() {
        creatViewPager();
        initPoint();
        initImage();
        initText();
    }

    private void creatViewPager() {
        viewPager = (ViewPager) context.findViewById(R.id.viewPager);
    }

    private void initPoint() {
        creatPoint();
        setPointDistance();
    }

    private void creatPoint() {
        hint_indicator = (ImageView) context.findViewById(R.id.hint_indicator);
    }

    private void setPointDistance() {
        hint_indicator.post(new GuideRuner());
    }

    private void initImage() {
        initailizationImage();
        initializationImageFather();
        initializationImageView();
        addImage();
    }

    private void initailizationImage() {
        for (int i = 0; i < indicators.length; i++)
            indicators[i] = (ImageView) context.findViewById(ids[i]);
    }

    private void initializationImageFather() {
        view1 = LayoutInflater.from(context).inflate(R.layout.guide1, null);
        view2 = LayoutInflater.from(context).inflate(R.layout.guide2, null);
        view3 = LayoutInflater.from(context).inflate(R.layout.guide3, null);
    }

    private void initializationImageView() {
        ee = (ImageView) view1.findViewById(R.id.ee);
        er = (ImageView) view2.findViewById(R.id.er);
        esan = (ImageView) view3.findViewById(R.id.e3);
    }

    private void addImage() {
        views.add(view1);
        views.add(view2);
        views.add(view3);
    }

    private void initText() {
        TextView rl = creatText();
        setTextListener(rl);
    }

    private TextView creatText() {
        return (TextView) view3.findViewById(R.id.rl_click);
    }

    private void setTextListener(TextView rl) {
        rl.setOnClickListener(this);
    }

    public void setTopBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context.getWindow().setBackgroundDrawable(null);
    }

    public void creatAdatper() {
        adapter = new GuideAdapter();
    }

    public void setAdapter() {
        viewPager.setAdapter(adapter);
    }

    public void getCurrentVersion() {
        try {
            currentVersion = Integer.parseInt(VersionUtils.getVersionCode(context));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        goToWhere();
        saveFirstComeFlag();
        context.finish();
    }

    private void saveFirstComeFlag() {
        sp.edit().putBoolean("isFirst", false).commit();
    }

    private void goToWhere() {
        if (isFistComeIn())
            ActivityUtils.goToActivity(context, LoginActivity.class);
        else
            ActivityUtils.goToActivity(context, HomePageActivity.class);
    }

    private boolean isFistComeIn() {
        return TextUtils.isEmpty(UserUtils.getUserId(context))
                || TextUtils.isEmpty(UserUtils.getAppVersion(context))
                || currentVersion >= 23;
    }

    public void initSP() {
        sp = context.getSharedPreferences(Constans.APP_SP, 0);
    }

    public void setListenerForViewPager() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    protected void scrollToAnim(int position, float positionOffset) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) hint_indicator
                .getLayoutParams();
        params.leftMargin = (int) ((distance * positionOffset) + position
                * distance);
        hint_indicator.setLayoutParams(params);
    }

    public void statisticsDetalTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_GUIDE, context.stop_time - context.resume_time);
    }

    public void statistics() {
        HYMob.getDataList(context, HYEventConstans.GUIDE_PAGE);
    }

    private class GuideRuner implements Runnable {
        @Override
        public void run() {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) hint_indicator
                    .getLayoutParams();
            int width = params.width;
            distance = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 22.5f, context.getResources()
                            .getDisplayMetrics());
        }
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

}
