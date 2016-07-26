package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;


/**
 * Created by 58 on 2016/7/21.
 */
public class AddAccountActivity extends QBBaseActivity implements View.OnClickListener {

    private View back_layout;
    private TextView txt_head;
    private TextView tv_user_content;
    private TextView tv_phone_content;
    private ImageView iv_base_help;
    private CheckBox cb_order,cb_bidding;
    private Button btn_save;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        back_layout               = getView(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        layout_back_head          = getView(R.id.layout_head);
        txt_head                  = getView(R.id.txt_head);
        txt_head.setText("添加子账号");
        tbl                       = getView(R.id.tbl);
        tv_user_content =  getView(R.id.tv_user_content);
        tv_phone_content = getView(R.id.tv_phone_content);
        iv_base_help = getView(R.id.iv_base_help);
        btn_save = getView(R.id.btn_save);
        cb_order = getView(R.id.cb_order);
        cb_bidding = getView(R.id.cb_bidding);


    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };



    @Override
    public void initListener() {
        iv_base_help.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        cb_order.setOnCheckedChangeListener(listener);
        cb_bidding.setOnCheckedChangeListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.iv_base_help:
                break;
            case R.id.btn_save:
                break;
        }
    }
}
