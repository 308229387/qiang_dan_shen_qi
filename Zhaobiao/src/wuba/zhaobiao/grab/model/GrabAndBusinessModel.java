package wuba.zhaobiao.grab.model;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.iview.SwitchButton;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.grab.adapter.FragmentAdapter;
import wuba.zhaobiao.grab.fragment.BusinessOpportunityFragment;
import wuba.zhaobiao.grab.fragment.GrabAndBusinessFragment;
import wuba.zhaobiao.grab.fragment.GrabFragment;

/**
 * Created by SongYongmeng on 2016/9/3.
 */
public class GrabAndBusinessModel<T> extends BaseModel implements View.OnClickListener {
    private GrabAndBusinessFragment context;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SwitchButton switchButton;
    private View showLayout;
    private GrabFragment grabFragment;
    private BusinessOpportunityFragment businessOpportunityFragment;
    private ImageView refresh;

    public GrabAndBusinessModel(GrabAndBusinessFragment context) {
        this.context = context;
    }

    public void creatView(LayoutInflater inflater, ViewGroup container) {
        showLayout =
                inflater.inflate(R.layout.fragment_grab_and_business, container, false);
    }

    public void initView() {
        mTabLayout = (TabLayout) showLayout.findViewById(R.id.tabs);
        mViewPager = (ViewPager) showLayout.findViewById(R.id.viewpager);
        switchButton = (SwitchButton) showLayout.findViewById(R.id.switch_button);
        refresh = (ImageView) showLayout.findViewById(R.id.business_refresh);
    }

    public void creatFragment() {
        grabFragment = new GrabFragment();
        businessOpportunityFragment = new BusinessOpportunityFragment();
    }

    public void setSitchButtonInfo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dealWithSwichButton();
            }
        }, 500);
    }

    private void dealWithSwichButton() {
        setChoiseForSwitch();
        setListenerForSwitchButton();
    }

    private void setChoiseForSwitch() {
        if ("1".equals(SPUtils.getServiceState(context.getActivity())))
            switchButton.setChecked(true);
        else
            switchButton.setChecked(false);
    }

    public void setListenerForSwitchButton() {
        switchButton.setOnCheckedChangeListener(new SwitchButtonListener());
        refresh.setOnClickListener(this);
    }

    public void setupViewPager() {
        List<String> titles = creatTitleForTab();
        List<Fragment> fragments = getFragments();
        setUp(new FragmentAdapter(context.getChildFragmentManager(), fragments, titles));
    }

    @NonNull
    private List<String> creatTitleForTab() {
        List<String> titles = new ArrayList<>();
        titles.add("抢单");
        titles.add("商机");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        return titles;
    }

    @NonNull
    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(grabFragment);
        fragments.add(businessOpportunityFragment);
        return fragments;
    }

    private void setUp(FragmentAdapter adapter) {
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        mViewPager.addOnPageChangeListener(new viewPagerChangeListener());
    }

    public View getView() {
        return showLayout;
    }

    private void atGrab() {
        switchButton.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
    }

    private void atBusiness() {
        switchButton.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_refresh:
                ToastUtils.showToast("refresh");
                break;
            default:
                break;
        }

    }

    private class SwitchButtonListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            grabFragment.topSwitchChicked(isChecked);
        }
    }

    private class viewPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0)
                atGrab();
            else
                atBusiness();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
