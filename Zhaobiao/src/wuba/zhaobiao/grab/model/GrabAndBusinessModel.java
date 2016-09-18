package wuba.zhaobiao.grab.model;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.iview.SwitchButton;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UserUtils;

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
    private String fragmenTag;
    List<String> titles = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();

    public GrabAndBusinessModel(GrabAndBusinessFragment context) {
        this.context = context;
    }

    public void creatView(LayoutInflater inflater, ViewGroup container) {
        showLayout =
                inflater.inflate(R.layout.fragment_grab_and_business, container, false);
    }

    public void initView() {
        fragmenTag = UserUtils.getHasaction(context.getActivity());

        switch (fragmenTag) {
            case "1":
                switchButton = (SwitchButton) showLayout.findViewById(R.id.switch_button);
                switchButton.setVisibility(View.VISIBLE);
                mTabLayout = (TabLayout) showLayout.findViewById(R.id.single_tabs);
                break;
            case "2":
                refresh = (ImageView) showLayout.findViewById(R.id.business_refresh);
                mTabLayout = (TabLayout) showLayout.findViewById(R.id.single_tabs);
                refresh.setVisibility(View.VISIBLE);
                refresh.setOnClickListener(this);
                break;
            case "3":
                mTabLayout = (TabLayout) showLayout.findViewById(R.id.tabs);
                switchButton = (SwitchButton) showLayout.findViewById(R.id.switch_button);
                switchButton.setVisibility(View.VISIBLE);
                refresh = (ImageView) showLayout.findViewById(R.id.business_refresh);
                refresh.setOnClickListener(this);
                break;
            default:
                mTabLayout = (TabLayout) showLayout.findViewById(R.id.tabs);
                switchButton = (SwitchButton) showLayout.findViewById(R.id.switch_button);
                break;
        }

        mTabLayout.setVisibility(View.VISIBLE);
        mViewPager = (ViewPager) showLayout.findViewById(R.id.viewpager);
    }

    public void creatFragment() {
        switch (fragmenTag) {
            case "1":
                grabFragment = new GrabFragment();
                titles.add("抢单");
                mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
                fragments.add(grabFragment);
                break;
            case "2":
                businessOpportunityFragment = new BusinessOpportunityFragment();
                titles.add("商机");
                mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
                fragments.add(businessOpportunityFragment);
                break;
            case "3":
                grabFragment = new GrabFragment();
                businessOpportunityFragment = new BusinessOpportunityFragment();
                titles.add("抢单");
                titles.add("商机");
                mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
                mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
                fragments.add(grabFragment);
                fragments.add(businessOpportunityFragment);
                break;
            default:
                break;
        }

    }

    public void selectChange() {
        try {
            if (UnreadUtils.isHasNewOrder(context.getActivity())) {
                UnreadUtils.clearNewOder(context.getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        EventAction action = new EventAction(EventType.EVENT_TAB_RESET);
        EventbusAgent.getInstance().post(action);
    }

    public void setSitchButtonInfo() {
        if (!fragmenTag.equals("2"))
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
    }

    public void setupViewPager() {
        setUp(new FragmentAdapter(context.getChildFragmentManager(), fragments, titles));
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
        grabFragment.registPush();
    }

    private void atBusiness() {
        switchButton.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);
        grabFragment.unregistNotificationListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_refresh:
                businessRefresh();
                break;
            default:
                break;
        }
    }

    public void businessRefresh() {
        businessOpportunityFragment.refreshList();
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
