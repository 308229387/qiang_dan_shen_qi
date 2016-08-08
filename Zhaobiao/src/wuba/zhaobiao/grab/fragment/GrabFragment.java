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
        return model.initView(inflater, container);
    }

    @Override
    public void onResume() {
        super.onResume();
        model.getData();
    }

    @Override
    protected GrabModel createModel() {
        return new GrabModel(GrabFragment.this);
    }
}
