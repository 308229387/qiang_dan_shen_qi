package wuba.zhaobiao.common.fragment;

import android.app.Fragment;
import android.os.Bundle;

import wuba.zhaobiao.common.model.BaseModel;

/**
 * Created by SongYongmeng on 2016/8/8.
 */
public abstract class BaseFragment<T extends BaseModel> extends Fragment {
    protected T model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initModel();
    }

    private void initModel() {
        model = createModel();
    }

    protected abstract T createModel();
}
