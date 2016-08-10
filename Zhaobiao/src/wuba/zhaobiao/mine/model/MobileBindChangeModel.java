package wuba.zhaobiao.mine.model;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.constans.CommonValue;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.mine.activity.MobileBindChangeActivity;
import wuba.zhaobiao.respons.GetCodeRespons;
import wuba.zhaobiao.respons.UpdateMobileRespons;

/**
 * Created by 58 on 2016/8/9.
 */
public class MobileBindChangeModel extends BaseModel implements View.OnClickListener{

    private MobileBindChangeActivity context;

    private View layout_back_head;
    private LinearLayout back_layout;
    private TextView txt_head;
    private EditText et_now_bind_mobile, et_now_validate_code, et_new_validate_mobile;
    private Button btn_getCode, btn_submit;

    public static final String FAILURE = "1";//重复获取验证码
    public static final String SUCCESS = "0";//成功

    private String mobile;

    private boolean newPhoneSatisfy;
    private boolean validateCodeSatisfy;
    int time = 60;


    public MobileBindChangeModel(MobileBindChangeActivity context){
        this.context = context;
    }

    public void getMobile(){
        mobile = context.getIntent().getStringExtra(AppConstants.MOBILE);
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
        back_layout  = (LinearLayout) context.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
    }

    private void setBackListener(){
        back_layout.setOnClickListener(this);
    }

    private void createTitle(){
        txt_head  = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText(R.string.change_mobile_bind);
    }

    public  void  initNowBindMobile(){
        createNowBindMobile();
        setNowBindMobile();
    }

    private void createNowBindMobile(){
        et_now_bind_mobile  = (EditText) context.findViewById(R.id.now_bind_mobile);
    }

    private void setNowBindMobile() {
        if (TextUtils.isEmpty(mobile)) {
            mobile = SPUtils.getVByK(context, GlobalConfigBean.KEY_USERPHONE);
            et_now_bind_mobile.setText(mobile);
        } else {
            et_now_bind_mobile.setText(mobile);
        }
    }

    public void initNowValidateCode(){
        createNowValidateCode();
        setNowValidateCodeChangeListener();
    }

    private void createNowValidateCode(){
        et_now_validate_code   = (EditText) context.findViewById(R.id.now_validate_code);
    }

    private void setNowValidateCodeChangeListener(){
        et_now_validate_code.addTextChangedListener(codeChangeListener);
    }

    private TextWatcher codeChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            if (length > 0) {
                validateCodeSatisfy = true;
            } else {
                validateCodeSatisfy = false;
            }
            checkSubmitEnabled(validateCodeSatisfy, newPhoneSatisfy);
        }
    };


    public void initGetCode(){
        createGetCode();
        setGetCodeListener();
    }

    private void createGetCode(){
        btn_getCode = (Button) context.findViewById(R.id.btn_getCode);
    }

    private void setGetCodeListener(){
        btn_getCode.setOnClickListener(this);
    }


    public void initNewValidateMobile(){
        createNewValidateMobile();
        setNewValidateMobileChangeListener();
    }

    private void createNewValidateMobile(){
        et_new_validate_mobile = (EditText) context.findViewById(R.id.new_validate_mobile);
    }

    private void setNewValidateMobileChangeListener(){
        et_new_validate_mobile.addTextChangedListener(newMobileChangeListener);
    }

    private TextWatcher newMobileChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            if(length == CommonValue.PHONE_LENGTH){
                newPhoneSatisfy = true;
            }else{
                newPhoneSatisfy = false;
            }
            checkSubmitEnabled(validateCodeSatisfy,newPhoneSatisfy);
        }
    };

    private void checkSubmitEnabled(boolean validateCodeSatisfy, boolean newPhoneSatisfy) {
        if(validateCodeSatisfy && newPhoneSatisfy){
            btn_submit.setEnabled(true);
            btn_submit.setTextColor(Color.parseColor("#FFFFFF"));
            btn_submit.setBackgroundResource(R.drawable.submit_btn_bg);
        }else{
            btn_submit.setEnabled(false);
            btn_submit.setTextColor(Color.parseColor("#FDC9C0"));
            btn_submit.setBackgroundResource(R.drawable.submit_bg_gray);
        }
    }

    public void initSubmit(){
        createSubmit();
        setSubmitListener();
        setSubmitState();
    }

    private void createSubmit(){
        btn_submit= (Button) context.findViewById(R.id.btn_submit);
    }

     private void setSubmitListener(){
         btn_submit.setOnClickListener(this);
     }

    private void setSubmitState(){
        btn_submit.setEnabled(false);
        btn_submit.setTextColor(Color.parseColor("#FDC9C0"));
        btn_submit.setBackgroundResource(R.drawable.submit_bg_gray);
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
        switch (v.getId()){
            case R.id.back_layout://后退
                back();
                break;
            case R.id.btn_getCode://获取验证码
                getCodeStatistics();
                getCode(et_now_bind_mobile.getText().toString());
                break;
            case R.id.btn_submit://提交
                submitStatistics();
                submit();
                break;
        }
    }

    private void back() {
        context.onBackPressed();
    }

    private void getCodeStatistics(){
        BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_CHANGE_MOBILE_PAGE_GET_CODE);
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_CHANGE_MOBILE_PAGE_GET_CODE);
    }

    private  void getCode(String mobile){
        OkHttpUtils.get(Urls.GET_VALIDE_CODE)
                .params("mobile",mobile)
                .execute(new getCodeCallback(context, true));
    }

    private void submitStatistics(){
        BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_CHANGE_MOBILE_PAGE_SUBMIT);
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_CHANGE_MOBILE_PAGE_SUBMIT);
    }

    private void submit(){
        CompareTwoMobile();
        initConfirmChangeMobileDialog();
    }

    private void CompareTwoMobile(){
        if(TextUtils.equals(et_now_bind_mobile.getText().toString(),et_new_validate_mobile.getText().toString())){
            ToastUtils.makeImgAndTextToast(context, "两次修改的手机号不能一致", R.drawable.validate_error, 0).show();
            return ;
        }
    }

    private void initConfirmChangeMobileDialog(){
       final ZhaoBiaoDialog confirmChangeMobileDialog = new ZhaoBiaoDialog(context,"");
        confirmChangeMobileDialog.setMessage("确定要把手机号修改为:"+et_new_validate_mobile.getText().toString());
        confirmChangeMobileDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                confirmChangeMobileDialog.dismiss();
                updateMobile(et_now_bind_mobile.getText().toString(), et_new_validate_mobile.getText().toString(), et_now_validate_code.getText().toString());
            }
            @Override
            public void onDialogCancelClick() {
                confirmChangeMobileDialog.dismiss();
            }
        });
        confirmChangeMobileDialog.show();
    }

    private void updateMobile(String mobile,String newPhone,String code ){
        OkHttpUtils.get(Urls.UPDATE_MOBILE)
                .params("oldPhone", mobile)
                .params("mobile", newPhone)
                .params("code", code)
                .execute(new updateMobileCallback(context, true));
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_BIND_MOBILE, context.stop_time - context.resume_time);
    }

    private class getCodeCallback extends DialogCallback<GetCodeRespons>{

        public getCodeCallback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, GetCodeRespons getCodeRespons, Request request, @Nullable Response response) {
             String status = getCodeRespons.getData().getStatus();
            if(TextUtils.equals(status,SUCCESS)){
                ToastUtils.makeImgAndTextToast(context,"验证码已发送",R.drawable.validate_done, Toast.LENGTH_SHORT).show();
                startCountDown(); //  开始倒计时
            }else if(TextUtils.equals(status,FAILURE)){
                ToastUtils.makeImgAndTextToast(context,"获取验证码已达上限",R.drawable.validate_done,Toast.LENGTH_SHORT).show();
            }
            else{
                ToastUtils.makeImgAndTextToast(context, "获取验证码失败", R.drawable.validate_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }
    }

    private void startCountDown(){
        time = 60;
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            time--;
            if(time>0){
                btn_getCode.setClickable(false);
                btn_getCode.setText(time + "秒后重发");
                handler.sendEmptyMessageDelayed(0,1000*1);
            }else{
                btn_getCode.setClickable(true);
                btn_getCode.setText("重新发送");
            }

        }
    };


    private class updateMobileCallback extends DialogCallback<UpdateMobileRespons>{

        public updateMobileCallback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, UpdateMobileRespons updateMobileRespons, Request request, @Nullable Response response) {
            String status = updateMobileRespons.getData().getStatus();
            String message = updateMobileRespons.getData().getMsg();
            if(TextUtils.equals(status,SUCCESS)){//成功
                ToastUtils.makeImgAndTextToast(context,"更改成功",R.drawable.validate_done,Toast.LENGTH_SHORT).show();
                back();
            }else{
                ToastUtils.makeImgAndTextToast(context,message,R.drawable.validate_error,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }
    }

}
