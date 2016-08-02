package com.huangyezhaobiao.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.result;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.callback.JsonCallback;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.StringUtils;
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

    private AccountHelpDialog helpDialog;
    private ZhaoBiaoDialog saveDialog;


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




    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
        iv_base_help.setOnClickListener(this);
        btn_save.setOnClickListener(this);
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
                String name = tv_user_content.getText().toString();
                if(TextUtils.isEmpty(name)){
                    ToastUtils.showToast("权限使用人不能为空");
                    return;
                }else if(!TextUtils.isEmpty(name) && !StringUtils.stringFilter(name)){
                    ToastUtils.showToast("权限使用人只允许输入文字或者字母");
                    tv_user_content.setSelection(name.length());//设置新的光标所在位置
                    return;
                }
                String phone = tv_phone_content.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    ToastUtils.showToast("使用人手机不能为空");
                    return;
                }else if(!TextUtils.isEmpty(phone) && !StringUtils.isMobileNO(phone)){
                    ToastUtils.showToast("请输入正确的手机号");
                    tv_phone_content.setSelection(phone.length());//设置新的光标所在位置
                    return;
                }
                StringBuilder builder = new StringBuilder();
                builder.append("1");
                if(cb_bidding.isChecked()){
                    builder.append("|").append("2");
                }
                if(cb_order.isChecked()){
                    builder.append("|").append("4");
                }

                addChildAccount(name, phone, builder.toString()); //增加子账号接口
                LogUtils.LogV("childAccount", "update_success_bidding---" + builder.toString());
                break;
        }
    }


    private void save(){
        String name = tv_user_content.getText().toString();
        if(TextUtils.isEmpty(name)){
            saveDialog.dismiss();
            ToastUtils.showToast("权限使用人不能为空");
            return;
        }else if(!TextUtils.isEmpty(name) && !StringUtils.stringFilter(name)){
            saveDialog.dismiss();
            ToastUtils.showToast("权限使用人只允许输入文字或者字母");
            tv_user_content.setSelection(name.length());//设置新的光标所在位置
            return;
        }
        String phone = tv_phone_content.getText().toString();
        if(TextUtils.isEmpty(phone)){
            saveDialog.dismiss();
            ToastUtils.showToast("使用人手机不能为空");
            return;
        }else if(!TextUtils.isEmpty(phone) && !StringUtils.isMobileNO(phone)){
            saveDialog.dismiss();
            ToastUtils.showToast("请输入正确的手机号");
            tv_phone_content.setSelection(phone.length());//设置新的光标所在位置
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("1");
        if(cb_bidding.isChecked()){
            builder.append("|").append("2");
        }
        if(cb_order.isChecked()){
            builder.append("|").append("4");
        }

        addChildAccount(name, phone, builder.toString()); //增加子账号接口
        LogUtils.LogV("childAccount", "update_success_bidding---" + builder.toString());
    }

    //请求实体
    private void addChildAccount(String name,String phone ,String authority) {
        OkHttpUtils.get("http://zhaobiao.58.com/api/suseradd")//
                .params("username", name)//
                .params("phone",phone)//
                .params("rbac",authority)
                .execute(new callback(AddAccountActivity.this,true));
    }
    //响应类
    private class callback extends DialogCallback<String> {

        public callback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            LogUtils.LogV("childAccount","add_success");
            finish();
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
        saveDialog= new ZhaoBiaoDialog(this,"是否保存添加的子账号?");
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
                save();
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


}
