package wuba.zhaobiao.mine.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.mine.model.SettingModel;

/**
 * Created by 58 on 2016/8/3.
 */
public class SettingActivity extends BaseActivity<SettingModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    private void init(){
        setTopBarColor();
        initView();
    }

    private void setTopBarColor(){
        model.setTopBarColor();
    }

    private void initView(){
        model.initHeader();
        model.initMobileSetting();
        model.initAutoSetting();
        model.initLogout();
    }
    @Override
    protected void onResume() {
        super.onResume();
        model.setTopBarHeight();
        model.getUserInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.statisticsDeadTime();
    }

    @Override
    public SettingModel createModel() {
        return new SettingModel(SettingActivity.this);
    }
}
