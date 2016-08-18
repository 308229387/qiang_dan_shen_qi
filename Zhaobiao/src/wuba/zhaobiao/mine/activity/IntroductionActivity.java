package wuba.zhaobiao.mine.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.mine.model.IntroductionModel;

/**
 * Created by 58 on 2016/8/17.
 */
public class IntroductionActivity extends BaseActivity<IntroductionModel>{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_production);
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
        model.initIntorductionPage();
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
    protected void onDestroy() {
        super.onDestroy();
        model.destoryWebView();
    }

    @Override
    public IntroductionModel createModel() {
        return new IntroductionModel(IntroductionActivity.this);
    }
}
