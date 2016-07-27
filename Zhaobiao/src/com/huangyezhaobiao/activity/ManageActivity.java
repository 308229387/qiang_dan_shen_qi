package com.huangyezhaobiao.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.ChildAccountAdapter;
import com.huangyezhaobiao.bean.ChildAccountBean;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2016/7/21.
 */
public class ManageActivity extends QBBaseActivity implements View.OnClickListener{

    private View back_layout;
    private TextView txt_head;
    private LinearLayout ll_add_child_account;
    private TextView tv_edit;
    private ListView lv_sManage;
    private View divider2;

    private RelativeLayout rl_add_manage;
    public boolean flag = false;// false表示右上角显示"编辑"，true表示显示"完成"

    private ChildAccountAdapter adapter;

    private List<ChildAccountBean> list = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manange);
        initView();
        initListener();

        initData();

    }

    private void initData(){
        ChildAccountBean entity1 = new ChildAccountBean();
        entity1.setAccountName("销售");
        entity1.setAccountPhone("12323456789");
        list.add(entity1);
        ChildAccountBean entity2 = new ChildAccountBean();
        entity2.setAccountName("客服");
        entity2.setAccountPhone("12323456789");
        list.add(entity2);
        ChildAccountBean entity3 = new ChildAccountBean();
        entity3.setAccountName("设计");
        entity3.setAccountPhone("12323456789");
        list.add(entity3);

        if(list.size() > 0) {
            tv_edit.setVisibility(View.VISIBLE);
            tv_edit.setText("编辑");
            lv_sManage.setVisibility(View.VISIBLE);
        }else{
            tv_edit.setVisibility(View.GONE);
            lv_sManage.setVisibility(View.GONE);
        }
        ll_add_child_account.setVisibility(View.VISIBLE);

        adapter = new ChildAccountAdapter(this,list,flag);
        lv_sManage.setAdapter(adapter);
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
        divider2 = getView(R.id.divider2);
        ll_add_child_account = getView(R.id.ll_add_child_account);
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
                if(flag){ //右上角显示为完成的界面
                    flag = !flag;
                    if(list.size() > 0) {
                        tv_edit.setVisibility(View.VISIBLE);
                        tv_edit.setText("编辑");
                        lv_sManage.setVisibility(View.VISIBLE);
                        divider2.setVisibility(View.VISIBLE);
                    }else{
                        tv_edit.setVisibility(View.GONE);
                        lv_sManage.setVisibility(View.GONE);
                        divider2.setVisibility(View.GONE);
                    }
                    ll_add_child_account.setVisibility(View.VISIBLE);

                    adapter = new ChildAccountAdapter(this,list,flag);
                    lv_sManage.setAdapter(adapter);

                }else{  //右上角显示为编辑的界面
                    flag = !flag;
                    tv_edit.setVisibility(View.VISIBLE);
                    tv_edit.setText("完成");
                    ll_add_child_account.setVisibility(View.GONE);

                    adapter = new ChildAccountAdapter(this,list,flag);
                    lv_sManage.setAdapter(adapter);
                }
               break;
            case R.id.rl_add_manage:
                if(list.size() >= 3){
                    ToastUtils.showShort(this, getString(R.string.account_add_more), 3000);
                }else{
                    ActivityUtils.goToActivity(this, AddAccountActivity.class);
                }
                break;
        }
    }


}
