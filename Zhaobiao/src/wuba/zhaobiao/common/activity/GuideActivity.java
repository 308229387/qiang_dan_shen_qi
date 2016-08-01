package wuba.zhaobiao.common.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;

import wuba.zhaobiao.common.model.GuideModel;

/**
 * Created by SongYongmeng on 2016/8/1.
 */
public class GuideActivity extends BaseActivity<GuideModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        init();
    }

    private void init() {
        setTopBarColor();
        onfigViewPager();
        getCurrentVersion();
        getSP();
    }

    private void getCurrentVersion() {
        model.getCurrentVersion();
    }

    private void getSP() {
        model.initSP();
    }

    private void setTopBarColor() {
        model.setTopBarColor();
    }

    private void onfigViewPager() {
        model.initViewPager();
        model.setListenerForViewPager();
        model.creatAdatper();
        model.setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.statistics();
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.statisticsDetalTime();
    }

    @Override
    public GuideModel createModel() {
        return new GuideModel(GuideActivity.this);
    }
}
