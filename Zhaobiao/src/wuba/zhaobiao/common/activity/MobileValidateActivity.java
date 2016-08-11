package wuba.zhaobiao.common.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.model.MobileValidateModel;

/**
 * Created by 58 on 2016/8/11.
 */
public class MobileValidateActivity extends BaseActivity<MobileValidateModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_valid);
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
        model.initMobile();
        model.initValidateCode();
        model.initGetCode();
        model.initCommit();
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
    public void onBackPressed() {
        model.confirmBack();
    }

    @Override
    public MobileValidateModel createModel() {
        return new MobileValidateModel(MobileValidateActivity.this);
    }
}
