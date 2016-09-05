package wuba.zhaobiao.common.fragment;

import android.os.Bundle;

import wuba.zhaobiao.common.model.BaseModel;

/**
 * Created by SongYongmeng on 2016/8/8.
 */
public abstract class BaseFragment<T extends BaseModel> extends android.support.v4.app.Fragment {
    protected T model;
    public static int current_index;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initModel();
    }

    private void initModel() {
        model = createModel();
    }

    public void OnFragmentSelectedChanged(boolean isSelected) {

    }


    protected abstract T createModel();
}
