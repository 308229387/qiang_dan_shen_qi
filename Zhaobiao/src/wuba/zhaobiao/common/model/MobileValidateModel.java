package wuba.zhaobiao.common.model;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.StringUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.activity.HomePageActivity;
import wuba.zhaobiao.common.activity.MobileValidateActivity;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.respons.GetCodeRespons;
import wuba.zhaobiao.respons.UpdateMobileRespons;

/**
 * Created by 58 on 2016/8/11.
 */
public class MobileValidateModel extends BaseModel implements View.OnClickListener{

    private MobileValidateActivity context;
    private View layout_back_head;
    private LinearLayout back_layout;
    private TextView txt_head;

    private EditText mobile,code;
    private Button getcode, commit;

    public static final String FAILURE = "1";//重复获取验证码
    public static final String SUCCESS = "0";//成功

    private int countDownTime;

    private Handler handler = new Handler();

    public MobileValidateModel(MobileValidateActivity context){
        this.context = context;
    }

    public void initHeader() {
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
        back_layout = (LinearLayout) context.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
    }

    private void setBackListener(){
        back_layout.setOnClickListener(this);
    }

    private void  createTitle(){
        txt_head = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText(R.string.mobile_validate);
    }

    public void initMobile(){
        createMobile();
    }

    private void createMobile(){
        mobile = (EditText) context.findViewById(R.id.validate_mobile);
    }

    public void initValidateCode(){
        createValiadteCode();
    }

    private void createValiadteCode(){
        code = (EditText)  context.findViewById(R.id.validate_code);
    }

    public void initGetCode(){
        createGetCode();
        setGetCodeListener();
    }

    private  void createGetCode(){
        getcode = (Button)  context.findViewById(R.id.validate_getcode);
    }

    private void setGetCodeListener(){
        getcode.setOnClickListener(this);
    }

    public void initCommit(){
        createCommit();
        setCommitListener();
    }

    private void createCommit(){
        commit = (Button)  context.findViewById(R.id.commit);
    }

    private void setCommitListener(){
        commit.setOnClickListener(this);
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
            case R.id.back_layout:
                back();
                break;
            case R.id.validate_getcode:
                getCodeStatistics();
                getValidateCode();
                break;
            case R.id.commit:
                commitStatistics();
                commit();
                break;

        }
    }

    private void back() {
        context.onBackPressed();
    }

    public void confirmBack(){
        final ZhaoBiaoDialog dialog = new ZhaoBiaoDialog(context,"确定要退出手机验证么");

        dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                dialog.dismiss();
                context.finish();
            }

            @Override
            public void onDialogCancelClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getCodeStatistics(){
        //获取验证码
        BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_MOBILE_BIND_PAGE_GETCODE);
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_MOBILE_BIND_PAGE_GETCODE);
    }

    private void getValidateCode(){
        String mobiletext = mobile.getText().toString();
        if (StringUtils.isMobile(mobiletext)) {
            getCode(mobiletext);
        } else {
            ToastUtils.makeImgAndTextToast(context, context.getString(R.string.input_correct_mobile), R.drawable.validate_error, Toast.LENGTH_SHORT).show();
        }
    }

    private  void getCode(String mobile){
        OkHttpUtils.get(Urls.GET_VALIDE_CODE)
                .params("mobile",mobile)
                .execute(new getCodeCallback(context, true));
    }

    private void countdown(){
        countDownTime = 60;
        handler.postDelayed(runnable, 0);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countDownTime--;
            ColorStateList color;
            if(countDownTime>=0){
                getcode.setClickable(false);
                getcode.setText(countDownTime+"s后重新发送");
                color = context.getResources().getColorStateList(R.color.whitedark);
                getcode.setTextColor(color);
                handler.postDelayed(this, 1000);
            }
            else{
                getcode.setClickable(true);
                color = context.getResources().getColorStateList(R.color.red);
                getcode.setTextColor(color);
                getcode.setText(R.string.get_validate);
                handler.removeCallbacks(runnable);
            }


        }
    };

    private void commitStatistics(){
        //点击了提交
        BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_MOBILE_BIND_PAGE_SUBMIT);

        HYMob.getDataList(context, HYEventConstans.EVENT_ID_MOBILE_BIND_PAGE_SUBMIT);
    }

    private void commit(){
        String mobiletext = mobile.getText().toString();
        String codetext = code.getText().toString();
        if (!StringUtils.isMobile(mobiletext)) {
            ToastUtils.makeImgAndTextToast(context, context.getString(R.string.input_correct_mobile), R.drawable.validate_error, Toast.LENGTH_SHORT).show();
        }
        else if(!StringUtils.isCode(codetext)){
            ToastUtils.makeImgAndTextToast(context, context.getString(R.string.input_correct_validate), R.drawable.validate_error, Toast.LENGTH_SHORT).show();
        }
        else{
            validate(mobiletext, codetext);
        }
    }

    private  void validate(String mobile,String code){
        OkHttpUtils.get(Urls.VALIDATE)
                .params("mobile",mobile)
                .params("code",code)
                .execute(new validateCallback(context, true));
    }

    private void validateSuccess(){
        goToHomePage();
        finishSelf();
        setValidate();
//        MiPushClient.setAlias(context, UserUtils.getUserId(context), null);
    }

    private void goToHomePage(){
        ActivityUtils.goToActivity(context, HomePageActivity.class);
    }

    private void finishSelf(){
        context.finish();
    }

    private void setValidate(){
        UserUtils.hasValidate(context);
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_FIRST_BIND_MOBILE, context.stop_time - context.resume_time);
    }

    private class getCodeCallback extends DialogCallback<GetCodeRespons> {

        public getCodeCallback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, GetCodeRespons getCodeRespons, Request request, @Nullable Response response) {
            String status = getCodeRespons.getData().getStatus();

            if(TextUtils.equals(status, SUCCESS)){
                ToastUtils.makeImgAndTextToast(context, context.getString(R.string.validate_code_already_send), R.drawable.validate_done, Toast.LENGTH_SHORT).show();
                countdown();
            }
            else if(TextUtils.equals(status,FAILURE)){
                ToastUtils.makeImgAndTextToast(context, context.getString(R.string.get_validate_exception_times), R.drawable.validate_done, Toast.LENGTH_SHORT).show();
            }
            else{
                ToastUtils.makeImgAndTextToast(context,context.getString(R.string.get_validate_exception), R.drawable.validate_done, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            if (!isToast) {
                ToastUtils.showToast(e.getMessage());
            }
        }
    }

    private class validateCallback extends DialogCallback<UpdateMobileRespons>{

        public validateCallback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, UpdateMobileRespons updateMobileRespons, Request request, @Nullable Response response) {
            String status = updateMobileRespons.getData().getStatus();
            String message = updateMobileRespons.getData().getMsg();

            if(TextUtils.equals(status,SUCCESS)){
                validateSuccess();
            } else{
                ToastUtils.makeImgAndTextToast(context, message, R.drawable.validate_error, 0).show();
            }

        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            if (!isToast) {
                ToastUtils.showToast(e.getMessage());
            }
        }
    }




}
