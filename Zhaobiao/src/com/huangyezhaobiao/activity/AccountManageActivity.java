package com.huangyezhaobiao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.ChildAccountAdapter;
import com.huangyezhaobiao.bean.ChildAccountBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.callback.JsonCallback;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.respons.AccountMaxRespons;
import wuba.zhaobiao.utils.LogoutDialogUtils;

/**
 * Created by 58 on 2016/7/21.
 */
public class AccountManageActivity extends QBBaseActivity implements View.OnClickListener {

    private View back_layout;
    private TextView txt_head;
    private LinearLayout ll_add_child_account;
    private TextView tv_edit;
    private ListView lv_sManage;
    private View divider2,divider3;

    private RelativeLayout rl_add_manage;
    public boolean flag = false;// false表示右上角显示"编辑"，true表示显示"完成"

    private ChildAccountAdapter adapter;

    private List<ChildAccountBean.data.bean> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manange);
        initView();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getChildAccountList();
    }


    //请求实体
    private void getChildAccountList() {
        OkHttpUtils.get("http://zhaobiao.58.com/api/suserlist")//
                .execute(new callback(AccountManageActivity.this,true));
    }

    //响应类
    private class callback extends DialogCallback<ChildAccountBean> {


        public callback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, ChildAccountBean childAccountBean, Request request, @Nullable Response response) {
            LogUtils.LogV("childAccount", "getList_success");
            ChildAccountBean.data data =  childAccountBean.getData();
            if(data != null){
                list =data.getList();
                if (list != null && list.size() > 0) {
                    tv_edit.setVisibility(View.VISIBLE);

                    if (flag) { //右上角显示为完成的界面
                        tv_edit.setText("完成");
                        ll_add_child_account.setVisibility(View.GONE);

                    }else{
                        tv_edit.setText("编辑");
                        ll_add_child_account.setVisibility(View.VISIBLE);
                    }
                    divider2.setVisibility(View.VISIBLE);
                    lv_sManage.setVisibility(View.VISIBLE);

                } else {
                    tv_edit.setVisibility(View.GONE);
                    lv_sManage.setVisibility(View.GONE);
                    divider2.setVisibility(View.GONE);
                    ll_add_child_account.setVisibility(View.VISIBLE);
                }

                adapter = new ChildAccountAdapter(AccountManageActivity.this, list, flag);
                lv_sManage.setAdapter(adapter);
            }

        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            if (!isToast  && e != null) {
                ToastUtils.showToast(e.getMessage());
            }
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable ChildAccountBean childAccountBean, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, childAccountBean, call, response, e);
            if (e != null && e.getMessage().equals(NEED_DOWN_LINE)) {
                new LogoutDialogUtils(AccountManageActivity.this, getString(R.string.force_exit)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(CHILD_FUNCTION_BAN)) {
                new LogoutDialogUtils(AccountManageActivity.this, getString(R.string.child_function_ban)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(CHILD_HAS_UNBIND)) {
                new LogoutDialogUtils(AccountManageActivity.this, getString(R.string.child_has_unbind)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(PPU_EXPIRED)) {
                new LogoutDialogUtils(AccountManageActivity.this, getString(R.string.ppu_expired)).showSingleButtonDialog();
            }
        }
    }

    @Override
    public void initView() {
        back_layout = getView(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        layout_back_head = getView(R.id.layout_head);
        txt_head = getView(R.id.txt_head);
        txt_head.setText("子账号管理");
        tbl = getView(R.id.tbl);
        tv_edit = getView(R.id.tv_edit);
        lv_sManage = getView(R.id.lv_sManage);
        rl_add_manage = getView(R.id.rl_add_manage);
        divider2 = getView(R.id.divider2);
        ll_add_child_account = getView(R.id.ll_add_child_account);
        divider3 = getView(R.id.divider3);
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
                if (flag) { //右上角显示为完成的界面
                    flag = !flag;
                    if (list != null && list.size() > 0) {
                        tv_edit.setVisibility(View.VISIBLE);
                        tv_edit.setText("编辑");
                        lv_sManage.setVisibility(View.VISIBLE);
                        divider2.setVisibility(View.VISIBLE);
                    } else {
                        tv_edit.setVisibility(View.GONE);
                        lv_sManage.setVisibility(View.GONE);
                        divider2.setVisibility(View.GONE);
                    }
                    ll_add_child_account.setVisibility(View.VISIBLE);

                    adapter = new ChildAccountAdapter(this, list, flag);
                    lv_sManage.setAdapter(adapter);

                    HYMob.getDataList(this, HYEventConstans.EVENT_ACCOUNT_FINISH);

                } else {  //右上角显示为编辑的界面
                    flag = !flag;
                    tv_edit.setVisibility(View.VISIBLE);
                    tv_edit.setText("完成");
                    ll_add_child_account.setVisibility(View.GONE);

                    adapter = new ChildAccountAdapter(this, list, flag);
                    lv_sManage.setAdapter(adapter);

                    HYMob.getDataList(this, HYEventConstans.EVENT_ACCOUNT_EDIT);
                }
                break;
            case R.id.rl_add_manage:
                getChildAccountMax();

                HYMob.getDataList(this, HYEventConstans.EVENT_ACCOUNT_ADD);
                break;
        }

    }

    //请求实体
    private void getChildAccountMax() {
        OkHttpUtils.get("http://zhaobiao.58.com/api/getaccountmax")//
                .execute(new AccountMaxCallback(AccountManageActivity.this, true));
    }

    //响应类
    private class AccountMaxCallback extends DialogCallback<AccountMaxRespons> {

        public AccountMaxCallback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, AccountMaxRespons accountMaxRespons, Request request, @Nullable Response response) {
            LogUtils.LogV("childAccount", "accountMax_success");
            String number = accountMaxRespons.getData().getAccountMaxSize();
            try {
                if (list != null && list.size() >= Integer.parseInt(number)) {
                    ToastUtils.showShort(AccountManageActivity.this, getString(R.string.account_add_more), 3000);
                } else {
                    ActivityUtils.goToActivity(AccountManageActivity.this, AddAccountActivity.class);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            if (!isToast  && e != null) {
                ToastUtils.showToast(e.getMessage());
            }

        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable AccountMaxRespons accountMaxRespons, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, accountMaxRespons, call, response, e);
            if (e != null && e.getMessage().equals(NEED_DOWN_LINE)) {
                new LogoutDialogUtils(AccountManageActivity.this, getString(R.string.force_exit)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(CHILD_FUNCTION_BAN)) {
                new LogoutDialogUtils(AccountManageActivity.this, getString(R.string.child_function_ban)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(CHILD_HAS_UNBIND)) {
                new LogoutDialogUtils(AccountManageActivity.this, getString(R.string.child_has_unbind)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(PPU_EXPIRED)) {
                new LogoutDialogUtils(AccountManageActivity.this, getString(R.string.ppu_expired)).showSingleButtonDialog();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_ACCOUNT_MANANGE, stop_time - resume_time);
    }
}
