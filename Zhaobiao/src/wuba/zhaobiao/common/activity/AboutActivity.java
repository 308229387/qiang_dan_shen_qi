package wuba.zhaobiao.common.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.model.AboutModel;

/**
 * Created by 58 on 2016/8/3.
 */
public class AboutActivity  extends BaseActivity<AboutModel>{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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
        model.initHeaderView();
        model.initVersionName();
        model.initFunctionInfo();
        model.initCheckUpdate();
        model.initSoftwareUsage();
    }


    @Override
    protected void onResume() {
        super.onResume();
        model.setTopBarHeight();
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.statisticsDeadTime();
    }

    @Override
    public AboutModel createModel() {
        return new AboutModel(AboutActivity.this);
    }
}