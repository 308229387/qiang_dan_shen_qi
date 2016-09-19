package wuba.zhaobiao.grab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.grab.model.BusinessOpportunityModel;

/**
 * Created by SongYongmeng on 2016/9/5.
 * 描    述：商机fragment
 */
public class BusinessOpportunityFragment extends BaseFragment<BusinessOpportunityModel> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initalizationLayout(inflater, container);
        initView();
        model.setCanotPullDown();
        model.creatDialog();
        model.setListener();
        model.initTimeData();
        model.getCityData();
        model.getMaskState();
        creatAdapter();
        setInfo();
        return model.getView();
    }

    private void initView() {
        model.initView();
    }

    private void creatAdapter() {
        model.creatAdapter();
    }

    private void setInfo() {
        model.setParamsForListView();
    }

    private void initalizationLayout(LayoutInflater inflater, ViewGroup container) {
        model.creatView(inflater, container);
    }

    @Override
    protected BusinessOpportunityModel createModel() {
        return new BusinessOpportunityModel(BusinessOpportunityFragment.this);
    }

    public void refreshList() {
        model.clickRefresh();
    }

    public void refresh() {
        model.refresh();
    }
}
