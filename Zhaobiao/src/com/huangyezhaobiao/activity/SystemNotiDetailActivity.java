package com.huangyezhaobiao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.SysListBean;

/**
 * Created by shenzhixin on 2015/12/16.
 * 系统详情页的native版本
 */
public class SystemNotiDetailActivity extends QBBaseActivity implements View.OnClickListener {
    SysListBean sysListBean;
    private View back_layout;
    private TextView txt_head;
    private TextView tv_time;
    private TextView tv_sys_title;
    private TextView tv_sys_content;
    public static Intent onNewIntent(Context context,SysListBean listBean){
        Intent intent = new Intent(context,SystemNotiDetailActivity.class);
        intent.putExtra("sysBean",listBean);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sysListBean = (SysListBean) getIntent().getSerializableExtra("sysBean");
        initView();
        initListener();
    }

    @Override
    public void initView() {
        setContentView(getLayoutId());
        layout_back_head = getView(R.id.layout_head);
        back_layout      = getView(R.id.back_layout);
        txt_head         = getView(R.id.txt_head);
        tv_time          = getView(R.id.tv_time);
        tv_sys_title     = getView(R.id.tv_sys_title);
        tv_sys_content   = getView(R.id.tv_sys_content);
        txt_head.setText("系统通知");
        if(sysListBean!=null) {
            tv_time.setText(sysListBean.getTime());
            tv_sys_content.setText(sysListBean.getContent());
            tv_sys_title.setText(sysListBean.getTitle());
        }
    }

    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
    }

    private int getLayoutId(){
        return R.layout.activity_system_noti_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;
        }
    }
}
