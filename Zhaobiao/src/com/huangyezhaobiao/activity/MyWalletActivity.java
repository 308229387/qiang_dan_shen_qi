package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;

/**
 * Created by shenzhixin on 2015/12/12.
 * 我的钱包界面
 */
public class MyWalletActivity extends QBBaseActivity implements View.OnClickListener {
    private View     back_layout;
    private TextView txt_head;
    private RelativeLayout rl_charge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initListener();

    }


    protected int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    public void initView() {
        layout_back_head = getView(R.id.layout_head);
        back_layout 	 = getView(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        txt_head    	 = getView(R.id.txt_head);
        txt_head.setText("我的钱包");
        tbl              = getView(R.id.tbl);
        rl_charge        = getView(R.id.rl_charge);

    }

    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
        rl_charge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.rl_charge:
                ActivityUtils.goToActivity(this,ConsumptionActivity.class);
                HYMob.getDataList(this, HYEventConstans.EVENT_ID_CONSUMPTION);
                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_WALLET, stop_time - resume_time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
