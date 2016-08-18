package wuba.zhaobiao.mine.model;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.ChildAccountAdapter;
import com.huangyezhaobiao.bean.ChildAccountBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.Utils;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.mine.activity.AccountManageActivity;
import wuba.zhaobiao.mine.activity.AddAccountActivity;
import wuba.zhaobiao.respons.AccountMaxRespons;

/**
 * Created by 58 on 2016/8/18.
 */
public class AccountManageModel  extends BaseModel implements View.OnClickListener{

    private AccountManageActivity context;

    private View layout_back_head;
    private View back_layout;
    private TextView txt_head;

    private TextView tv_edit;

    private ListView lv_sManage;
    private LinearLayout ll_add_child_account;
    private RelativeLayout rl_add_manage;

    private View divider2,divider3;

    public boolean flag = false;// false表示右上角显示"编辑"，true表示显示"完成"

    private ChildAccountAdapter adapter;

    private List<ChildAccountBean.data.bean> list = new ArrayList<>();

    public AccountManageModel(AccountManageActivity context){
        this.context = context;
    }

    //请求实体
    public void getChildAccountList() {
        OkHttpUtils.get("http://zhaobiao.58.com/api/suserlist")//
                .execute(new callback(context, true));
    }

    public void initHeader() {
        createHeader();
        initBack();
        createTitle();
    }

    private void createHeader(){
        layout_back_head = context.findViewById(R.id.layout_head);
    }

    private void initBack(){
        createBack();
        setBackListener();
    }

    private void createBack(){
        back_layout = context.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
    }

    private void setBackListener(){
        back_layout.setOnClickListener(this);
    }

    private void createTitle(){
        txt_head = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText("子账号管理");
    }

    public void initEdit(){
        createEdit();
        setEditListener();
    }

    private void createEdit(){
        tv_edit = (TextView) context.findViewById(R.id.tv_edit);
    }

    private void setEditListener(){
        tv_edit.setOnClickListener(this);
    }

    public void initAccountList(){
        createAccountList();
    }

    private void createAccountList(){
        lv_sManage = (ListView) context.findViewById(R.id.lv_sManage);
    }

    public void initAdd(){
        createAdd();
        setAddListener();
    }

    private void createAdd(){
        ll_add_child_account = (LinearLayout) context.findViewById(R.id.ll_add_child_account);
        rl_add_manage = (RelativeLayout) context.findViewById(R.id.rl_add_manage);
    }

    private void setAddListener(){
        rl_add_manage.setOnClickListener(this);
    }

    public void initDivider(){
        createDivider();
    }

    private void createDivider(){
        divider2 = context.findViewById(R.id.divider2);
        divider3 = context.findViewById(R.id.divider3);
    }

    public void setTopBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = Utils.getStatusBarHeight(context);
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
            if (layout_back_head != null) {
                layout_back_head.setPadding(0, height + more, 0, 0);
            }
        }
    }

    public void setTopBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context.getWindow().setBackgroundDrawable(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                back();
                break;
            case R.id.tv_edit:
                operateAccount();
                break;
            case R.id.rl_add_manage:
                getChildAccountMax();
                addClickedStatistics();
                break;
        }
    }

    private void back(){
        context. onBackPressed();
    }

    private void operateAccount(){
        if (flag) { //右上角显示为完成的界面
            edit();
            createAndSetAdapter();
            finishClickedStatistics();

        } else {  //右上角显示为编辑的界面
            complete();
            createAndSetAdapter();
            editClickedStatistics();
        }
    }

    private void edit(){
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
    }

    private void complete(){
        flag = !flag;
        tv_edit.setVisibility(View.VISIBLE);
        tv_edit.setText("完成");
        ll_add_child_account.setVisibility(View.GONE);
    }

    private void createAndSetAdapter(){
        adapter = new ChildAccountAdapter(context, list, flag);
        lv_sManage.setAdapter(adapter);
    }

    private void finishClickedStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_ACCOUNT_FINISH);
    }

    private void editClickedStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_ACCOUNT_EDIT);
    }

    //请求实体
    private void getChildAccountMax() {
        OkHttpUtils.get("http://zhaobiao.58.com/api/getaccountmax")//
                .execute(new AccountMaxCallback(context, true));
    }

    private void addClickedStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_ACCOUNT_ADD);
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
                    ToastUtils.showShort(context, context.getString(R.string.account_add_more), 3000);
                } else {
                    ActivityUtils.goToActivity(context, AddAccountActivity.class);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            if (!isToast) {
                ToastUtils.showToast(e.getMessage());
            }

        }
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans. PAGE_ACCOUNT_MANANGE, context.stop_time - context.resume_time);
    }


    //响应类
    private class callback extends DialogCallback<ChildAccountBean> {


        public callback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, ChildAccountBean childAccountBean, Request request, @Nullable Response response) {
            LogUtils.LogV("childAccount", "getList_success");
            getDataSuccess(childAccountBean);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {

            if (!isToast) {
                ToastUtils.showToast(e.getMessage());
            }
        }

    }

    private void getDataSuccess(ChildAccountBean childAccountBean){
        ChildAccountBean.data data =  childAccountBean.getData();
        if(data != null){
            list =data.getList();
            if (list != null && list.size() > 0) {
                hasData();
            } else {
                hasNoData();
            }
            createAndSetAdapter();
        }
    }


    private void hasData(){
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
    }

    private void hasNoData(){
        tv_edit.setVisibility(View.GONE);
        lv_sManage.setVisibility(View.GONE);
        divider2.setVisibility(View.GONE);
        ll_add_child_account.setVisibility(View.VISIBLE);
    }



}
