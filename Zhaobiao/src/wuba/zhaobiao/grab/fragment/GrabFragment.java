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
 */
public class GrabFragment extends BaseFragment<GrabModel> {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        model.creatView(inflater, container);
        model.initView();
        model.setParamsForListView();
        return model.getView();
    }

    @Override
    public void onResume() {
        super.onResume();
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
