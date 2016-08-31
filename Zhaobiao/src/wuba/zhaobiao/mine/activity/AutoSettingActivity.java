package wuba.zhaobiao.mine.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.mine.model.AutoSettingModel;

/**
 * Created by 58 on 2016/8/11.
 */
public class AutoSettingActivity extends BaseActivity<AutoSettingModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_settings);
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
        model.initProgressBar();
        model.initNoInternetStatus();
        model.initWebViewContainer();
        model.initAutoSettingPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.setTopBarHeight();
    }

    @Override
    public void onBackPressed() {
        model.initSettingDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.statisticsDeadTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.detoryWebView();
    }

    @Override
    public AutoSettingModel createModel() {
        return new AutoSettingModel(AutoSettingActivity.this);
    }
}
