package wuba.zhaobiao.common.fragment;

import com.huangyezhaobiao.enums.TitleBarType;

import wuba.zhaobiao.common.model.GrabModel;

/**
 * Created by SongYongmeng on 2016/8/2.
 */
public class GrabFragment extends BaseFragment<GrabModel> {
    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {

    }

    @Override
    public GrabModel createModel() {
        return new GrabModel(GrabFragment.this);
    }

    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    @Override
    public void onTitleBarClosedClicked() {

    }
}
