package wuba.zhaobiao.grab.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.iview.SwitchButton;
import com.huangyezhaobiao.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.grab.adapter.FragmentAdapter;
import wuba.zhaobiao.grab.model.GrabAndBusinessModel;
import wuba.zhaobiao.message.fragment.MessageFragment;

/**
 * Created by SongYongmeng on 2016/9/3.
 * 描    述：抢单与商机的父布局Fragment，此类中，会有抢单播报按钮的逻辑
 */
public class GrabAndBusinessFragment extends BaseFragment<GrabAndBusinessModel> {
    private GrabAndBusinessFragment context = this;
    private View showLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SwitchButton switchButton;
    private GrabFragment grabFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showLayout =
                inflater.inflate(R.layout.fragment_grab_and_business, container, false);
        mTabLayout = (TabLayout) showLayout.findViewById(R.id.tabs);
        mViewPager = (ViewPager) showLayout.findViewById(R.id.viewpager);
        switchButton = (SwitchButton) showLayout.findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener(new SwitchButtonListener());
        creatFragment();
        initEvent();
        setupViewPager();
        return showLayout;
    }

    private void creatFragment() {
        grabFragment = new GrabFragment();
    }

    private void setupViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("抢单");
        titles.add("商机");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(grabFragment);
        fragments.add(new MessageFragment());
        FragmentAdapter adapter =
                new FragmentAdapter(getChildFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        mViewPager.addOnPageChangeListener(new viewPagerChangeListener());
    }

    private void initEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ("1".equals(SPUtils.getServiceState(context.getActivity()))) {
                    switchButton.setChecked(true);//选中服务模式
                } else {
                    switchButton.setChecked(false);//选中休息模式
                }
                switchButton.setOnCheckedChangeListener(new SwitchButtonListener());
            }
        }, 2000);
    }

    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {

    }

    @Override
    public GrabAndBusinessModel createModel() {
        return new GrabAndBusinessModel();
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
            if(position ==0)
                switchButton.setVisibility(View.VISIBLE);
            else
                switchButton.setVisibility(View.GONE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
