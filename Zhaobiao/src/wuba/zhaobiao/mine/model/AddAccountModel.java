package wuba.zhaobiao.mine.model;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.huangyezhaobiao.callback.DialogCallback;
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
import wuba.zhaobiao.mine.activity.AddAccountActivity;

/**
 * Created by 58 on 2016/8/18.
 */
public class AddAccountModel extends BaseModel implements View.OnClickListener {

    private AddAccountActivity context;

    private View layout_back_head;
    private View back_layout;
    private TextView txt_head;

    private EditText tv_user_content;
    private EditText tv_phone_content;

    private ImageView iv_base_help;
    private Button btn_save;

    private CheckBox cb_order,cb_bidding;

    private AccountHelpDialog helpDialog;
    private ZhaoBiaoDialog saveDialog;


    public AddAccountModel(AddAccountActivity context){
        this.context = context;
    }

    public void initHeader(){
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
        txt_head.setText("添加子账号");
    }

    public void initUser(){
        createUser();
        setUserChangeListener();
    }

    private void createUser(){
        tv_user_content = (EditText) context.findViewById(R.id.tv_user_content);
    }

    private void setUserChangeListener(){
        tv_user_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).setHint("");
                }

            }
        });
    }
    public void initPhone(){
        createPhone();
        setPhoneChangeListener();
    }
    private void createPhone(){
        tv_phone_content = (EditText) context.findViewById(R.id.tv_phone_content);
    }

    private void setPhoneChangeListener(){
        tv_phone_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).setHint("");
                }
            }
        });
    }

    public void initBaseHelp(){
        createBaseHelp();
        setBaseHelpListener();
    }

    private void createBaseHelp(){
        iv_base_help = (ImageView) context.findViewById(R.id.iv_base_help);
    }

    private void setBaseHelpListener(){
        iv_base_help.setOnClickListener(this);
    }

    public void initSave(){
        createSave();
        setSaveListener();
    }

    private void createSave(){
        btn_save = (Button) context.findViewById(R.id.btn_save);
    }

    private void setSaveListener(){
        btn_save.setOnClickListener(this);
    }


    public  void initGrabAndOrder(){
        createGrab();
        createOrder();
    }

    private void createGrab(){
        cb_bidding = (CheckBox) context.findViewById(R.id.cb_bidding);
    }

    private void createOrder(){
        cb_order = (CheckBox) context.findViewById(R.id.cb_order);
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
            case R.id.iv_base_help:
                initHelpDialog();
                baseHelpClickedStatistics();
                break;
            case R.id.btn_save:
                saveAccount(false);

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

    private void saveAccount(boolean isDialog){
        String name = getUserName();
        judgeUserName(name, isDialog);
        String phone = getUserPhone();
        judgeUserPhone(phone, isDialog);
        StringBuilder builder = getAuthority();
        addChildAccount(name, phone, builder.toString()); //增加子账号接口
        LogUtils.LogV("childAccount", "update_success_bidding---" + builder.toString());
        addSaveClickedStatistics();

    }


    private String getUserName(){
        return tv_user_content.getText().toString();
    }

    private void judgeUserName(String name ,boolean isDialog){
        if(TextUtils.isEmpty(name)){
            if(isDialog){
                closeDialog();
            }
            ToastMessage("权限使用人不能为空");
            return;
        }else if(!TextUtils.isEmpty(name) && !StringUtils.stringFilter(name)){
            if(isDialog){
                closeDialog();
            }
            ToastMessage("权限使用人只允许输入文字或者字母");
            tv_user_content.setSelection(name.length());//设置新的光标所在位置
            return;
        }
    }

    private String getUserPhone(){
        return  tv_phone_content.getText().toString();
    }

    private void judgeUserPhone(String phone,boolean isDialog){
        if(TextUtils.isEmpty(phone)){
            ToastMessage("使用人手机不能为空");
            if(isDialog){
                closeDialog();
            }
            return;
        }else if(!TextUtils.isEmpty(phone) && !StringUtils.isMobileNO(phone)){
            ToastMessage("请输入正确的手机号");
            if(isDialog){
                closeDialog();
            }
            tv_phone_content.setSelection(phone.length());//设置新的光标所在位置
            return;
        }
    }

    private StringBuilder getAuthority(){
        StringBuilder builder = new StringBuilder();
        builder.append("1");
        if(cb_bidding.isChecked()){
            builder.append("|").append("2");
        }
        if(cb_order.isChecked()){
            builder.append("|").append("4");
        }
        return builder;
    }

    private void ToastMessage(String message){
        ToastUtils.showToast(message);
    }

    private void closeDialog(){
        saveDialog.dismiss();
    }

    //请求实体
    private void addChildAccount(String name,String phone ,String authority) {

        OkHttpUtils.get("http://zhaobiao.58.com/api/suseradd")//
                .params("username", name)//
                .params("phone",phone)//
                .params("rbac",authority)
                .execute(new callback(context, true));
    }

    private  void addSaveClickedStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_ADD_ACCOUNT_SAVE);
    }

    public void closePage(){
        if(!TextUtils.isEmpty(tv_user_content.getText().toString()) && !TextUtils.isEmpty(tv_phone_content.getText().toString())){
            initSaveDialog();
        }else{
            context.finish();
        }
    }

    private void initSaveDialog(){
        saveDialog= new ZhaoBiaoDialog(context,"是否保存添加的子账号?");
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
                saveAccount(true);
            }

            @Override
            public void onDialogCancelClick() {
                saveDialog.dismiss();
                context.finish();
            }
        });
        saveDialog.show();
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_ACCOUNT_ADD, context.stop_time - context.resume_time);
    }

    //响应类
    private class callback extends DialogCallback<String> {

        public callback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            LogUtils.LogV("childAccount","add_success");
            context.finish();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            if (!isToast) {
                ToastUtils.showToast(e.getMessage());
            }
        }

    }


}