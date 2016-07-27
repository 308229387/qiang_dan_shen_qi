package com.huangyezhaobiao.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.result;
import com.huangyezhaobiao.callback.JsonCallback;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.view.AccountHelpDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpParams;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by 58 on 2016/7/21.
 */
public class AddAccountActivity extends QBBaseActivity implements View.OnClickListener {

    private View back_layout;
    private TextView txt_head;
    private EditText tv_user_content;
    private EditText tv_phone_content;
    private ImageView iv_base_help;
    private CheckBox cb_order,cb_bidding;
    private Button btn_save;

    private AccountHelpDialog helpDailog;
    private ZhaoBiaoDialog saveDialog;

    StringBuilder builder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        initView();
        initListener();

        builder= new StringBuilder();
        builder.append("1").append("|").append("3");
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
        back_layout.setOnClickListener(this);
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
                initHelpDialog();
                break;
            case R.id.btn_save:
                 get();
                break;
        }
    }

    //请求实体
    private void get() {
        OkHttpUtils.get("http://zhaobiao.58.com/api/suseradd")//
                .params("username", tv_user_content.getText().toString())//
                .params("phone", tv_phone_content.getText().toString())//
                .params("rbac",builder.toString())
                .execute(new Test());
    }
    //响应类
    private class Test extends JsonCallback<String> {

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {

            ToastUtils.showToast(11111111);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
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
