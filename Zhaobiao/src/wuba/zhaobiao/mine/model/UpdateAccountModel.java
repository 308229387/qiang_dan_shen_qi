package wuba.zhaobiao.mine.model;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.ChildAccountBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.StringUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.AccountHelpDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.mine.activity.UpdateAccountActivity;

/**
 * Created by 58 on 2016/8/18.
 */
public class UpdateAccountModel extends BaseModel implements View.OnClickListener{

    private UpdateAccountActivity context;
    private View layout_back_head;
    private View back_layout;
    private TextView txt_head;
    private EditText et_update_user_content,et_update_phone_content;
    private ImageView iv_update_base_help;
    private Button btn_update_save;

    private CheckBox cb_update_order,cb_update_bidding;

    private String id,name,phone,authority;

    private AccountHelpDialog helpDialog;
    private ZhaoBiaoDialog saveDialog;

    public UpdateAccountModel(UpdateAccountActivity context){
        this.context = context;
    }

    public void initHeader(){
        createHeader();
        initBack();
        createTitle();
    }

    private void createHeader(){
        layout_back_head  = context.findViewById(R.id.layout_head);
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
        txt_head   = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText("修改子账号");
    }

    public void initUpdateUser(){
        createUpdateUser();
    }

    private void createUpdateUser(){
        et_update_user_content = (EditText) context.findViewById(R.id.et_update_user_content);
    }

    public void initUpdatePhone(){
        createUpdatePhone();
    }

    private void createUpdatePhone(){
        et_update_phone_content = (EditText) context.findViewById(R.id.et_update_phone_content);
    }

    public void initBaseHelp(){
        createBaseHelp();
        setBaseHelpListener();
    }

    private void createBaseHelp(){
        iv_update_base_help = (ImageView) context.findViewById(R.id.iv_update_base_help);
    }

    private void setBaseHelpListener(){
        iv_update_base_help.setOnClickListener(this);
    }

    public void initUpdateSave(){
        createUpdateSave();
        setUpdateSaveListener();
    }

    private void createUpdateSave(){
        btn_update_save = (Button) context.findViewById(R.id.btn_update_save);
    }

    private void setUpdateSaveListener(){
        btn_update_save.setOnClickListener(this);
    }

    public  void initUpdateGrabAndOrder(){
        createUpdateGrab();
        createUpdateOrder();
    }

    private void createUpdateGrab(){
        cb_update_bidding = (CheckBox) context.findViewById(R.id.cb_update_bidding);
    }

    private void createUpdateOrder(){
        cb_update_order = (CheckBox) context.findViewById(R.id.cb_update_order);
    }

    public void initData(){
        initAccountId();
        initAccountName();
        initAccountPhone();
        initAccountAuthority();
    }

    private void initAccountId(){
        if(!TextUtils.isEmpty(context.getIntent().getStringExtra(Constans.CHILD_ACCOUNT_ID))){
            id = context.getIntent().getStringExtra(Constans.CHILD_ACCOUNT_ID);
        }
    }

    private void initAccountName(){
        name = context.getIntent().getStringExtra(Constans.CHILD_ACCOUNT_NAME);
        if(!TextUtils.isEmpty(name)){
            et_update_user_content.setText(name);
        }
    }

    private void initAccountPhone(){
        phone = context.getIntent().getStringExtra(Constans.CHILD_ACCOUNT_PHONE);
        if(!TextUtils.isEmpty(phone)){
            et_update_phone_content.setText(phone);
        }
    }

    private void initAccountAuthority(){
        authority = context.getIntent().getStringExtra(Constans.CHILD_ACCOUNT_AUTHORITY);
        LogUtils.LogV("childAccount", "update" + authority);
        if(!TextUtils.isEmpty(authority)
                && TextUtils.equals("5",authority) || TextUtils.equals("7",authority)){
            cb_update_order.setChecked(true);
        }

        if(!TextUtils.isEmpty(authority)
                && TextUtils.equals("3",authority) || TextUtils.equals("7",authority)){
            cb_update_bidding.setChecked(true);
        }
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
            case R.id.iv_update_base_help:
                initHelpDialog();
                baseHelpClickedStatistics();
                break;
            case R.id.btn_update_save:

                updateAccount();
                break;
        }
    }

    private void back(){
        context.onBackPressed();
    }

    private void  initHelpDialog(){
        helpDialog = new AccountHelpDialog(context);
        helpDialog.setMessage(context.getString(R.string.account_help));
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

    private void baseHelpClickedStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_ACCOUNT_HELP);
    }

    private void updateAccount(){
        String name = getUserName();
        judgeUserName(name);
        StringBuilder builder = getAuthority();
        updateChildAccount(id, name, builder.toString()); //更新子账号接口
        updateSaveClickedStatistics();
    }


    private String  getUserName(){
        return et_update_user_content.getText().toString();
    }

    private void judgeUserName(String name){
        if(TextUtils.isEmpty(name)){
            ToastUtils.showToast("权限使用人不能为空");
            return;
        }else if(!TextUtils.isEmpty(name) && !StringUtils.stringFilter(name)){
            ToastUtils.showToast("权限使用人只允许输入文字或者字母");
            et_update_user_content.setSelection(name.length());//设置新的光标所在位置
            return;
        }
    }

    private StringBuilder getAuthority(){
        StringBuilder builder= new StringBuilder();
        builder.append("1");
        if(cb_update_bidding.isChecked()){
            builder.append("|").append("2");
        }
        if(cb_update_order.isChecked()){
            builder.append("|").append("4");
        }
        return builder;
    }

    private void updateSaveClickedStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_UPDATE_ACCOUNT_SAVE);
    }

    //请求实体
    private void updateChildAccount(String id, String name,String authority) {

        OkHttpUtils.get("http://zhaobiao.58.com/api/suserupdate")//
                .params("id",id)
                .params("username", name)
                .params("rbac", authority)
                .execute(new callback(context, true));
    }
    //响应类
    private class callback extends DialogCallback<ChildAccountBean> {

        public callback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, ChildAccountBean childAccountBean, Request request, @Nullable Response response) {
            LogUtils.LogV("childAccount","update_success");
            context.finish();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {

            if (!isToast) {
                ToastUtils.showToast(e.getMessage());
            }
        }

    }

    public void initSaveDialog(){
        saveDialog= new ZhaoBiaoDialog(context,"是否确认返回?");
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
                context.finish();
            }

            @Override
            public void onDialogCancelClick() {
                saveDialog.dismiss();
            }
        });
        saveDialog.show();

    }

    public void statisticsDeadTime(){
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_ACCOUNT_EDIT, context.stop_time - context.resume_time);
    }
}
