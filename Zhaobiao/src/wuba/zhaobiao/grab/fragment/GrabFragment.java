package wuba.zhaobiao.grab.fragment;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.grab.model.GrabModel;

/**
 * Created by SongYongmeng on 2016/8/8.
 */
public class GrabFragment extends BaseFragment<GrabModel> {

    @Override
    protected GrabModel createModel() {
        return new GrabModel(GrabFragment.this);
    }
}
