package wuba.zhaobiao.grab.activity;

import android.content.Intent;
import android.os.Bundle;

import com.huangyezhaobiao.R;

import wuba.zhaobiao.common.activity.BaseActivity;
import wuba.zhaobiao.grab.model.SettlementFailModel;

/**
 * Created by SongYongmeng on 2016/9/13.
 */
public class SettlementFailActivity extends BaseActivity<SettlementFailModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_failure);
        init();
    }

    private void init() {
        String failType=getFailType();
        model.initView(failType);
        model.setTopBarHeight();
        model.setTopBarColor();
        model.setListener();
    }

    private String getFailType() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getStringExtra("failType");
        } else {
            return "2";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume_time = System.currentTimeMillis();
    }

    @Override
    public void onStop() {
        super.onStop();
        stop_time = System.currentTimeMillis();
        model.statisticsDeadTime();
    }

    @Override
    public SettlementFailModel createModel() {
        return new SettlementFailModel(SettlementFailActivity.this);
    }
}
