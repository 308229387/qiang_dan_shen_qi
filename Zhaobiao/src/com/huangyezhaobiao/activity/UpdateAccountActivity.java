package com.huangyezhaobiao.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.view.AccountHelpDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

/**
 * Created by 58 on 2016/7/26.
 */
public class UpdateAccountActivity extends QBBaseActivity implements View.OnClickListener{
    private View back_layout;
    private TextView txt_head;
    private EditText et_update_user_content,et_update_phone_content;
    private ImageView iv_update_base_help;
    private CheckBox cb_update_order,cb_update_bidding;
    private Button btn_update_save;

    private String name,phone;

    private AccountHelpDialog helpDailog;
    private ZhaoBiaoDialog saveDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        back_layout               = getView(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        layout_back_head          = getView(R.id.layout_head);
        txt_head                  = getView(R.id.txt_head);
        txt_head.setText("修改子账号");
        tbl                       = getView(R.id.tbl);
        et_update_user_content =  getView(R.id.et_update_user_content);
        et_update_phone_content = getView(R.id.et_update_phone_content);
        iv_update_base_help = getView(R.id.iv_update_base_help);
        btn_update_save = getView(R.id.btn_update_save);
        cb_update_order = getView(R.id.cb_update_order);
        cb_update_bidding = getView(R.id.cb_update_bidding);
    }

    private void initData(){
        Intent intent = getIntent();
        name = intent.getStringExtra(Constans.CHILD_ACCOUNT_NAME);
        if(!TextUtils.isEmpty(name)){
            et_update_user_content.setText(name);
        }
        phone = intent.getStringExtra(Constans.CHILD_ACCOUNT_PHONE);
        if(!TextUtils.isEmpty(phone)){
            et_update_phone_content.setText(phone);
        }
    }


    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };

    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
        iv_update_base_help.setOnClickListener(this);
        btn_update_save.setOnClickListener(this);
        cb_update_order.setOnCheckedChangeListener(listener);
        cb_update_bidding.setOnCheckedChangeListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.iv_update_base_help:
                initHelpDialog();
                break;
            case R.id.btn_update_save:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        initSaveDialog();
    }

    private void initSaveDialog(){
        saveDialog= new ZhaoBiaoDialog(this,"是否保存添加的权限?");
        saveDialog.setNagativeText("不保存");
        saveDialog.setPositiveText("保存");
        saveDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                saveDialog = null;

            }
        });
        saveDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                saveDialog.dismiss();
                finish();
            }

            @Override
            public void onDialogCancelClick() {
                saveDialog.dismiss();
                finish();
            }
        });
        saveDialog.show();
    }

    private void  initHelpDialog(){
        helpDailog = new AccountHelpDialog(this);
        helpDailog.setMessage(getString(R.string.account_help));
        helpDailog.setCancelButtonGone();
        helpDailog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                helpDailog = null;

            }
        });
        helpDailog.setOnDialogClickListener(new AccountHelpDialog.onDialogClickListener() {

            @Override
            public void onDialogOkClick() {
                helpDailog.dismiss();
            }

            @Override
            public void onDialogCancelClick() {

            }


        });
        helpDailog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!this.isFinishing() && helpDailog != null && helpDailog.isShowing()) {
            helpDailog.dismiss();
            helpDailog = null;
        }
        if (!this.isFinishing() && saveDialog != null && saveDialog.isShowing()) {
            saveDialog.dismiss();
            saveDialog = null;
        }
    }


}
