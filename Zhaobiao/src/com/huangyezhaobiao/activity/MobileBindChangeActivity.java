package com.huangyezhaobiao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.MobileChangeBean;
import com.huangyezhaobiao.iview.MobileChangeIView;
import com.huangyezhaobiao.presenter.MobileBindChangePresenter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.MobileBindChangeViewModel;
import com.huangyezhaobiao.vm.ValidateViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenzhixin on 2015/11/10.
 *  手机绑定的修改页面
 *  流程是
 *          一进来获取当前绑定的手机号
 *                              获取成功:默认获取手机号验证码
 *                                                  获取成功:弹窗，60秒倒计时
 *                                                          输入新手机号码，点击提交
 *                                                                          提交成功---成功，返回
 *                                                                          提交失败---重新提交
 *
 *                                                  获取失败:弹窗，可以重新点击获取
 *
 *
 *                              获取失败:给出提示
 * 当两个手机号框都达到手机号的输入数字，并且验证码框有数字输入时，可以点击提交
 * 否则提交是不能够点击的
 */
public class MobileBindChangeActivity extends QBBaseActivity implements View.OnClickListener, NetWorkVMCallBack ,MobileChangeIView{
    private LinearLayout              back_layout;
    private TextView                  txt_head;
    private TextInputLayout           til_now_bind_mobile;
    private TextInputLayout           til_now_validate_code;
    private TextInputLayout           til_new_validate_mobile;
    private EditText                  et_now_bind_mobile;
    private EditText                  et_now_validate_code;
    private EditText                  et_new_validate_mobile;
    private Button                    btn_getCode;
    private Button                    btn_submit;
    private ZhaoBiaoDialog            changeMobileExitDialog;
    private ZhaoBiaoDialog            confirmChangeMobileDialog;
    private ValidateViewModel mobileChangeGetCodeVM;
    private MobileBindChangeViewModel mobileBindChangeViewModel;
  //  private MobileChangeGetMobileVM   mobileChangeGetMobileVM;
    private MobileBindChangePresenter mobileBindChangePresenter;
    public static final String FAILURE = "1";
    public static final String SUCCESS = "0";//成功
    public static final String MOBILE = "mobile";
    private String mobile;
    public static Intent onNewIntent(Context context,String mobile){
        Intent intent = new Intent(context,MobileBindChangeActivity.class);
        intent.putExtra(MOBILE,mobile);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobile = getIntent().getStringExtra(MOBILE);
        initViewModel();
        initView();
        initListener();
        onSubmitUnEnabled();
        if(TextUtils.isEmpty(mobile)) {
            mobile = SPUtils.getVByK(this, GlobalConfigBean.KEY_USERPHONE);
            et_now_bind_mobile.setText(mobile);
          //  mobileChangeGetMobileVM.getOriMobile();
        }else{
            et_now_bind_mobile.setText(mobile);
        }
    }

    private void initViewModel() {
        mobileChangeGetCodeVM     = new ValidateViewModel(this,this);
        mobileBindChangeViewModel = new MobileBindChangeViewModel(this,this);
       // mobileChangeGetMobileVM   = new MobileChangeGetMobileVM(this,this);
        mobileBindChangePresenter = new MobileBindChangePresenter(this,this);
    }

    private int getLayoutId(){
        return R.layout.activity_mobile_bind_change;
    }


    @Override
    public void initView() {
        setContentView(getLayoutId());
        layout_back_head        = getView(R.id.layout_head);
        back_layout             = getView(R.id.back_layout);
        txt_head                = getView(R.id.txt_head);
        til_now_bind_mobile     = getView(R.id.now_bind_mobile);
        til_new_validate_mobile = getView(R.id.new_validate_mobile);
        til_now_validate_code   = getView(R.id.now_validate_code);
        btn_getCode             = getView(R.id.btn_getCode);
        btn_submit              = getView(R.id.btn_submit);
        et_new_validate_mobile  = til_new_validate_mobile.getEditText();
        et_now_bind_mobile      = til_now_bind_mobile.getEditText();
        et_now_validate_code    = til_now_validate_code.getEditText();
        changeMobileExitDialog = new ZhaoBiaoDialog(this,"提示","确认要退出修改绑定手机号么?");
        confirmChangeMobileDialog = new ZhaoBiaoDialog(this,"提示","确认修改手机号么?");
        initDatas();

    }

    private void initDatas() {
        til_new_validate_mobile.setHint(getString(R.string.new_bind_mobile));
        til_now_bind_mobile.setHint(getString(R.string.now_bind_mobile));
        til_now_validate_code.setHint(getString(R.string.validate_number));
        txt_head.setText(R.string.change_mobile_bind);
    }

    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        et_now_validate_code.addTextChangedListener(mobileBindChangePresenter.getMobileCodeListener());
        et_new_validate_mobile.addTextChangedListener(mobileBindChangePresenter.getNewMobileTextWatcher());
        changeMobileExitDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                changeMobileExitDialog.dismiss();
                onBackPressed();
            }

            @Override
            public void onDialogCancelClick() {
                changeMobileExitDialog.dismiss();
            }
        });
        confirmChangeMobileDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                confirmChangeMobileDialog.dismiss();
                mobileBindChangeViewModel.submit(et_now_bind_mobile.getText().toString(), et_new_validate_mobile.getText().toString(), et_now_validate_code.getText().toString());

            }

            @Override
            public void onDialogCancelClick() {
                confirmChangeMobileDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout://后退
                changeMobileExitDialog.show();
                break;
            case R.id.btn_getCode://获取验证码
                BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_CHANGE_MOBILE_PAGE_GET_CODE);
                mobileChangeGetCodeVM.getCode(UserUtils.getUserId(this),et_now_bind_mobile.getText().toString(),true);
                break;
            case R.id.btn_submit://提交
                BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_CHANGE_MOBILE_PAGE_SUBMIT);
                //Toast.makeText(this,"submit",Toast.LENGTH_SHORT).show();
                if(TextUtils.equals(et_now_bind_mobile.getText().toString(),et_new_validate_mobile.getText().toString())){
                    ToastUtils.makeImgAndTextToast(this,"两次修改的手机号不能一致",R.drawable.validate_error,0).show();
                    return ;
                }
                confirmChangeMobileDialog.setMessage("确定要把手机号修改为:"+et_new_validate_mobile.getText().toString());
                confirmChangeMobileDialog.show();
                break;
        }
    }

    @Override
    public void onLoadingStart() {
        startLoading();
    }

    @Override
    public void onLoadingSuccess(Object t) {
        stopLoading();
        if(t instanceof MobileChangeBean){//获取初始的手机号
            MobileChangeBean mobileChangeBean = (MobileChangeBean) t;
            String status = mobileChangeBean.getStatus();
            if(TextUtils.equals(status,SUCCESS)){//获取初始手机号成功
                String mobile = mobileChangeBean.getMobile();
                //赋值后,去自动请求验证码
                et_now_bind_mobile.setText(mobile);

            }else {//获取初始手机号失败
               ToastUtils.makeImgAndTextToast(this,"获取初始手机号失败",R.drawable.validate_wrong,Toast.LENGTH_SHORT).show();
            }
        }
        //获取验证码
        if (t instanceof String){
            String status = (String)t;
            if(TextUtils.equals(status,SUCCESS)){
                ToastUtils.makeImgAndTextToast(this,"验证码已发送",R.drawable.validate_done,Toast.LENGTH_SHORT).show();
                //开始倒计时
               mobileBindChangePresenter.startCountDown();
            }
            else{
                ToastUtils.makeImgAndTextToast(this, status, R.drawable.validate_error, Toast.LENGTH_SHORT).show();
            }
        }
        //提交
        if(t instanceof Map){
            HashMap<String,String> maps = (HashMap<String, String>) t;
            String status = maps.get("status");
            String msg    = maps.get("msg");
            if(TextUtils.equals(status,SUCCESS)){//成功
                ToastUtils.makeImgAndTextToast(this,"更改成功",R.drawable.validate_done,Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else{
                ToastUtils.makeImgAndTextToast(this,msg,R.drawable.validate_error,Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onLoadingError(String msg) {
        stopLoading();
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingCancel() {
        stopLoading();
    }

    @Override
    public void onNoInterNetError() {
        Toast.makeText(this,"没有网络,请稍后重试",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginInvalidate() {
        callBack.onLoginInvalidate();
    }

    @Override
    public void onSubmitEnabled() {
        btn_submit.setEnabled(true);
        btn_submit.setBackgroundResource(R.drawable.submit_btn_bg);
    }

    @Override
    public void onSubmitUnEnabled() {
        btn_submit.setEnabled(false);
        btn_submit.setBackgroundResource(R.drawable.submit_bg_gray);
    }

    @Override
    public void validateNumberEnabled() {
        btn_getCode.setBackgroundResource(R.drawable.submit_btn_bg);
        btn_getCode.setTextColor(getResources().getColor(R.color.white));
        btn_getCode.setClickable(true);
        btn_getCode.setText("重新发送验证码");

    }

    @Override
    public void validateNumberUnEnabled(int time) {
        btn_getCode.setBackgroundResource(R.drawable.submit_bg_gray);
        btn_getCode.setTextColor(getResources().getColor(R.color.white));
        btn_getCode.setClickable(false);
        btn_getCode.setText(time+"秒后重发");
    }
}
