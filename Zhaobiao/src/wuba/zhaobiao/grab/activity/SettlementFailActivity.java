package wuba.zhaobiao.grab.activity;

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
        model.initView();
        model.setTopBarHeight();
        model.setTopBarColor();
        model.setListener();
    }

    @Override
    public SettlementFailModel createModel() {
        return new SettlementFailModel(SettlementFailActivity.this);
    }
}
