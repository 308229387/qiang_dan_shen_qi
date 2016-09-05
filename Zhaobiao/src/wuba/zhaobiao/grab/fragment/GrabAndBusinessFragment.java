package wuba.zhaobiao.grab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.grab.model.GrabAndBusinessModel;

/**
 * Created by SongYongmeng on 2016/9/3.
 * 描    述：抢单与商机的父布局Fragment，此类中，会有抢单播报按钮的逻辑
 */
public class GrabAndBusinessFragment extends BaseFragment<GrabAndBusinessModel> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initalizationLayout(inflater, container);
        creatFragment();
        setSitchButtonInfo();
        setupViewPager();
        return model.getView();
    }

    private void initalizationLayout(LayoutInflater inflater, ViewGroup container) {
        model.creatView(inflater, container);
        model.initView();
    }

    private void creatFragment() {
        model.creatFragment();
    }

    private void setSitchButtonInfo() {
        model.setSitchButtonInfo();
    }

    private void setupViewPager() {
        model.setupViewPager();
    }

    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {

    }

    @Override
    public GrabAndBusinessModel createModel() {
        return new GrabAndBusinessModel(GrabAndBusinessFragment.this);
    }

}
