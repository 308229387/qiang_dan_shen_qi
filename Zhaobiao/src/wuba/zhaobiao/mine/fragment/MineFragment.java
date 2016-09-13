package wuba.zhaobiao.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.mine.model.MineModel;

/**
 * Created by 58 on 2016/8/12.
 */
public class MineFragment extends BaseFragment<MineModel>{

    public long resume_time;
    public long stop_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init(inflater, container);
        return model.getView();
    }

    private void init(LayoutInflater inflater, ViewGroup container){
        model.createView(inflater, container);
        model.initView();
        model.initBalance();
        model.initMyWallet();
        model.initAccountManage();
        model.judgeIsSubAccount();
        model.initSetting();
        model.initHelp();
        model.initAbout();
    }


    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {
        if (isSelected && model != null){
            model.getUserInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resume_time = System.currentTimeMillis();
        model.initUserInfo();
        model.minePageClicked();
    }

    @Override
    public void onStop() {
        super.onStop();
        stop_time = System.currentTimeMillis();
        model.statisticsDeadTime();
    }

    @Override
    protected MineModel createModel() {
        return new MineModel(MineFragment.this);
    }
}
