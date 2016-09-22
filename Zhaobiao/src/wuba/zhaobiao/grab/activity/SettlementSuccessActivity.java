package wuba.zhaobiao.grab.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.grab.model.SettlementSuccessModel;

/**
 * Created by SongYongmeng on 2016/9/13.
 */
public class SettlementSuccessActivity extends BaseActivity<SettlementSuccessModel> {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_success);
        init();
    }

    private void init() {
        initView();
        setListener();
        setTop();
        getInfoToIntent();
    }

    private void initView() {
        model.initView();
    }

    private void setListener() {
        model.setListener();
    }

    private void setTop() {
        model.setTopBarHeight();
        model.setTopBarColor();
        model.setTitle();
        model.setState();
    }

    private void getInfoToIntent() {
        model.getIntent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        resume_time = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop_time = System.currentTimeMillis();
        model.statisticsDeadTime();
    }

    @Override
    public SettlementSuccessModel createModel() {
        return new SettlementSuccessModel(SettlementSuccessActivity.this);
    }
}
