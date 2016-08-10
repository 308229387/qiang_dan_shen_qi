package wuba.zhaobiao.grab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.grab.model.GrabModel;

/**
 * Created by SongYongmeng on 2016/8/8.
 * 描    述：抢单展示，红点检测。
 */
public class GrabFragment extends BaseFragment<GrabModel> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initalizationLayout(inflater, container);
        creatAdapter();
        setInfo();

        return model.getView();
    }

    private void initalizationLayout(LayoutInflater inflater, ViewGroup container) {
        model.creatView(inflater, container);
        model.initView();
        model.registMessageBar();
    }

    private void creatAdapter() {
        model.creatAdapter();
    }

    private void setInfo() {
        model.setInfoForTop();
        model.setParamsForListView();
        model.setCachRespons();
    }

    @Override
    public void onResume() {
        super.onResume();
        model.checkNet();
        model.getData();
    }

    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {
        if (isSelected && model != null)
            model.selectChange();
    }

    @Override
    protected GrabModel createModel() {
        return new GrabModel(GrabFragment.this);
    }
}
