package wuba.zhaobiao.common.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.model.SplashModel;


/**
 * Created by SongYongmeng on 2016/7/28.
 * 描    述：引导界面，注册推送，等3秒进入相应界面，存储版本号
 */
public class SplashActivity extends BaseActivity<SplashModel> {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        model.registPush();
        model.waitTimeAfterGoToWhere();
        model.setTobBarColor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        model.baiduStatisticsPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.baiduStatisticsStart();
    }

    @Override
    public SplashModel createModel() {
        return new SplashModel(SplashActivity.this);
    }

}
