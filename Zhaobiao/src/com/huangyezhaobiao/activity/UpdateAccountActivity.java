package com.huangyezhaobiao.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.ChildAccountBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.callback.JsonCallback;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.StringUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.view.AccountHelpDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.lzy.okhttputils.OkHttpUtils;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

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

    private String id,name,phone,authority;

    private AccountHelpDialog helpDialog;
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
        if(!TextUtils.isEmpty( intent.getStringExtra(Constans.CHILD_ACCOUNT_ID))){
            id = intent.getStringExtra(Constans.CHILD_ACCOUNT_ID);
        }
        name = intent.getStringExtra(Constans.CHILD_ACCOUNT_NAME);
        if(!TextUtils.isEmpty(name)){
            et_update_user_content.setText(name);
        }
        phone = intent.getStringExtra(Constans.CHILD_ACCOUNT_PHONE);
        if(!TextUtils.isEmpty(phone)){
            et_update_phone_content.setText(phone);
        }
        authority = intent.getStringExtra(Constans.CHILD_ACCOUNT_AUTHORITY);
        LogUtils.LogV("childAccount","update" + authority);
        if(!TextUtils.isEmpty(authority)
                && TextUtils.equals("5",authority) || TextUtils.equals("7",authority)){
            cb_update_order.setChecked(true);
        }

        if(!TextUtils.isEmpty(authority)
                && TextUtils.equals("3",authority) || TextUtils.equals("7",authority)){
            cb_update_bidding.setChecked(true);
        }
    }



    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
        iv_update_base_help.setOnClickListener(this);
        btn_update_save.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.iv_update_base_help:
                initHelpDialog();
                HYMob.getDataList(this, HYEventConstans.EVENT_ACCOUNT_HELP);
                break;
            case R.id.btn_update_save:
                String name = et_update_user_content.getText().toString();

                if(TextUtils.isEmpty(name)){
                    ToastUtils.showToast("权限使用人不能为空");
                    return;
                }else if(!TextUtils.isEmpty(name) && !StringUtils.stringFilter(name)){
                    ToastUtils.showToast("权限使用人只允许输入文字或者字母");
                    et_update_user_content.setSelection(name.length());//设置新的光标所在位置
                    return;
                }
                StringBuilder builder= new StringBuilder();
                builder.append("1");
                if(cb_update_bidding.isChecked()){
                    builder.append("|").append("2");
                }
                if(cb_update_order.isChecked()){
                    builder.append("|").append("4");
                }

                updateChildAccount(id,name,builder.toString()); //更新子账号接口

                break;
        }
    }

    //请求实体
    private void updateChildAccount(String id, String name,String authority) {

        HYMob.getDataList(this, HYEventConstans.EVENT_UPDATE_ACCOUNT_SAVE);

        OkHttpUtils.get("http://zhaobiao.58.com/api/suserupdate")//
                .params("id",id)
                .params("username", name)
                .params("rbac", authority)
                .execute(new callback(UpdateAccountActivity.this,true));
    }
    //响应类
    private class callback extends DialogCallback<ChildAccountBean> {

        public callback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, ChildAccountBean childAccountBean, Request request, @Nullable Response response) {
            LogUtils.LogV("childAccount","update_success");
            finish();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            if (!isToast) {
                ToastUtils.showToast(e.getMessage());
            }
        }

    }

    @Override
    public void onBackPressed() {
        initSaveDialog();
    }

    private void initSaveDialog(){
        saveDialog= new ZhaoBiaoDialog(this,"是否确认返回?");
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
            }
        });
        saveDialog.show();

    }

    private void  initHelpDialog(){
        helpDialog = new AccountHelpDialog(this);
        helpDialog.setMessage(getString(R.string.account_help));
        helpDialog.setCancelButtonGone();
        helpDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                helpDialog = null;

            }
        });
        helpDialog.setOnDialogClickListener(new AccountHelpDialog.onDialogClickListener() {

            @Override
            public void onDialogOkClick() {
                helpDialog.dismiss();
            }

            @Override
            public void onDialogCancelClick() {

            }


        });
        helpDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_ACCOUNT_EDIT, stop_time - resume_time);
    }
}
