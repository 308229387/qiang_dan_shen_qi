package com.huangyezhaobiao.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.MobileChangeBean;
import com.huangyezhaobiao.constans.CommonValue;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.presenter.SettingsPresenter;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.LogoutViewModel;
import com.huangyezhaobiao.vm.YuEViewModel;
import com.wuba.loginsdk.external.LoginCallback;
import com.wuba.loginsdk.external.LoginClient;
import com.wuba.loginsdk.external.Request;
import com.wuba.loginsdk.external.SimpleLoginCallback;
import com.wuba.loginsdk.model.LoginSDKBean;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.Map;

/**
 * Created by shenzhixin on 2015/11/12.
 * 设置界面
 */
public class SettingsActivity extends QBBaseActivity implements View.OnClickListener, NetWorkVMCallBack {
    private View back_layout;
    private TextView txt_head;
    private View rl_change_mobile_settings;
    private View rl_auto_settings;
    private TextView tv_now_bind_mobile;
 //   private MobileChangeGetMobileVM mobileChangeGetMobileVM;
 	private YuEViewModel yuEViewModel;
    private SettingsPresenter       presenter;
    private String mobile;

    private RelativeLayout rl_exit;
    private ZhaoBiaoDialog confirmExitDialog; //退出对话框
    private LogoutViewModel lvm; //退出接口


//    public static Intent onNewIntent(Context context){
//        Intent intent = new Intent(context,SettingsActivity.class);
//        return intent;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    mobileChangeGetMobileVM = new MobileChangeGetMobileVM(this,this);
        presenter = new SettingsPresenter(this);
        setContentView(getLayoutId());
        initView();
        initListener();
        String mobile = SPUtils.getVByK(this, GlobalConfigBean.KEY_USERPHONE);
        tv_now_bind_mobile.setText("已绑定" + mobile);

        lvm = new LogoutViewModel(this, this);
    }



    @Override
    public void initView() {
        back_layout               = getView(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
        layout_back_head          = getView(R.id.layout_head);
        txt_head                  = getView(R.id.txt_head);
        txt_head.setText("设置");
        tbl                       = getView(R.id.tbl);
        rl_change_mobile_settings = getView(R.id.rl_change_mobile_settings);
        rl_auto_settings          = getView(R.id.rl_auto_settings);
        tv_now_bind_mobile        = getView(R.id.tv_now_bind_mobile);
        rl_exit = getView(R.id.rl_exit);


    }

    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
        rl_auto_settings.setOnClickListener(this);
        rl_change_mobile_settings.setOnClickListener(this);
        rl_exit.setOnClickListener(this);
    }


    private int getLayoutId(){
        return R.layout.activity_settings;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.rl_auto_settings://去自定义设置界面
                presenter.goToAutoSettingsActivity(this);
                HYMob.getDataList(SettingsActivity.this, HYEventConstans.EVENT_ID_AUTO_SETTING);
                break;
            case R.id.rl_change_mobile_settings://去手机绑定界面
                presenter.goToMobileChangeActivity(mobile);
                HYMob.getDataList(SettingsActivity.this, HYEventConstans.EVENT_ID_BIND_MOBILE);
                break;
            case R.id.rl_exit:// 退出
                //进行数据同步化的操作;
                globalConfigVM.refreshUsers();
                BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_LOGOUT);

                HYMob.getDataList(SettingsActivity.this, HYEventConstans.EVENT_ID_LOGOUT);

                confirmExitDialog = new ZhaoBiaoDialog(this,getString(R.string.logout_make_sure));
                confirmExitDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
                    @Override
                    public void onDialogOkClick() {
                        confirmExitDialog.dismiss();
                        lvm.logout();

                        SharedPreferencesUtils.clearLoginToken(getApplicationContext());
                        //退出时注销个推
                        GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
                        //退出时注销小米推送
                        MiPushClient.unsetAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
                        UserUtils.clearUserInfo(getApplicationContext());
                        Toast.makeText(SettingsActivity.this, getString(R.string.logout_now), Toast.LENGTH_SHORT).show();
                        MDUtils.myUserCenterMD(SettingsActivity.this, MDConstans.ACTION_EXIT);
                        LoginClient.doLogoutOperate(SettingsActivity.this);
                        ActivityUtils.goToActivity(SettingsActivity.this, BlankActivity.class);
                        close();


                    }

                    @Override
                    public void onDialogCancelClick() {
                        confirmExitDialog.dismiss();
                    }
                });
                confirmExitDialog.show();
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
        Log.v("SettingActivity","t onLoadingSuccess");
        if(t instanceof MobileChangeBean){//获取初始的手机号
            Log.v("SettingActivity","t instanceof MobileChangeBean");
            MobileChangeBean mobileChangeBean = (MobileChangeBean) t;
            String status = mobileChangeBean.getStatus();
            if(TextUtils.equals(status, CommonValue.SUCCESS)){//获取初始手机号成功
                Log.v("SettingActivity","CommonValue.SUCCESS");
                String mobile = mobileChangeBean.getMobile();
                //赋值后,去自动请求验证码
                tv_now_bind_mobile.setText("已绑定" + mobile);
                this.mobile = mobile;
            }else {//获取初始手机号失败
                ToastUtils.makeImgAndTextToast(this, "获取初始手机号失败", R.drawable.validate_wrong, Toast.LENGTH_SHORT).show();
            }
        }

        if (t instanceof Map<?, ?>) {
            Map<String, String> maps = (Map<String, String>) t;
            String balance = maps.get("balance");
            String userPhone = maps.get("phone");
            SPUtils.saveKV(SettingsActivity.this, GlobalConfigBean.KEY_USERPHONE, userPhone);
            Log.v("SettingActivity","saveKV　userPhone　＝　" +userPhone);
        }
        mobile = SPUtils.getVByK(this, GlobalConfigBean.KEY_USERPHONE);
        tv_now_bind_mobile.setText("已绑定" + mobile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        yuEViewModel = new YuEViewModel(this,this);
        yuEViewModel.getBalance();
    }

    @Override
    public void onLoadingError(String msg) {
        stopLoading();
        if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
            super.onLoadingError(msg);
        }else if(!TextUtils.isEmpty(msg)){
            Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadingCancel() {

    }

    @Override
    public void onNoInterNetError() {
        Toast.makeText(this,"并没有网络",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginInvalidate() {
        callBack.onLoginInvalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_SETTING_LIST, stop_time - resume_time);
    }
}
