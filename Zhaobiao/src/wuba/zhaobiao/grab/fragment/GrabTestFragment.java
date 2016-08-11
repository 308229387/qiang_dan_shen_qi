package wuba.zhaobiao.grab.fragment;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.grab.model.GrabModel;

/**
 * Created by SongYongmeng on 2016/8/8.
 * 描    述：抢单展示，红点检测。
 */
public class GrabTestFragment extends BaseFragment<GrabModel> {


    @Override
    public void onResume() {
        super.onResume();
//        model.getData();
    }

    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {
    }

    @Override
    protected GrabModel createModel() {
        return null;
    }
}
