package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.utils.ActivityUtils;

/**
 * Created by 58 on 2016/7/21.
 */
public class ManageActivity extends QBBaseActivity implements View.OnClickListener{

    private View back_layout;
    private TextView txt_head;
    private TextView tv_edit;
    private ListView lv_sManage;
    private RelativeLayout rl_add_manage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manange);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        back_layout               = getView(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        layout_back_head          = getView(R.id.layout_head);
        txt_head                  = getView(R.id.txt_head);
        txt_head.setText("子账号管理");
        tbl                       = getView(R.id.tbl);
        tv_edit = getView(R.id.tv_edit);
        tv_edit.setOnClickListener(this);
        lv_sManage = getView(R.id.lv_sManage);
        rl_add_manage = getView(R.id.rl_add_manage);
        rl_add_manage.setOnClickListener(this);
    }

    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        rl_add_manage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.tv_edit:
               break;
            case R.id.rl_add_manage:
                ActivityUtils.goToActivity(this, AddAccountActivity.class);
                break;
        }
    }
}
