package com.huangyezhaobiao.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.LoginBean;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.PhoneUtils;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.VersionUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.CheckLoginViewModel;
import com.huangyezhaobiao.vm.UpdateViewModel;
import com.wuba.loginsdk.external.LoginCallback;
import com.wuba.loginsdk.external.LoginClient;
import com.wuba.loginsdk.external.Request;
import com.wuba.loginsdk.external.SimpleLoginCallback;
import com.wuba.loginsdk.model.LoginSDKBean;
import com.wuba.loginsdk.utils.ToastUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

public class BlankActivity extends CommonBaseActivity {


    private ZhaoBiaoDialog alertDialog;

    private CheckLoginViewModel checkLoginViewModel;

    private UpdateViewModel updateViewModel;
    /**
     * 是否强制更新
     */
	private boolean forceUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        initDailog();
        //注册登录SDK
        LoginClient.register(mLoginCallback);
        onClickLogin();

        //关掉service
        stopService(new Intent(BlankActivity.this, MyService.class));
//        //关掉service
//        stopService(new Intent(BlankActivity.this, AlertService.class));
        updateViewModel = new UpdateViewModel(vmCallBack, this);

    }

    @Override
    protected void onResume() {
        super.onResume();

//        if(updateViewModel != null)
//            updateViewModel.checkVersion();
    }

    private NetWorkVMCallBack vmCallBack = new NetWorkVMCallBack() {
        @Override
        public void onLoadingStart() {


        }

        @Override
        public void onLoadingSuccess(Object t) {

        }

        @Override
        public void onLoadingError(String msg) {

        }

        @Override
        public void onLoadingCancel() {

        }

        @Override
        public void onNoInterNetError() {

        }

        @Override
        public void onLoginInvalidate() {

        }

        @Override
        public void onVersionBack(String version) {
            String versionCode = "";
            int currentVersion = -1; //当前版本号

            int versionNum = -1;
            //获取当前系统版本号
            try {
                currentVersion = Integer.parseInt(VersionUtils.getVersionCode(BlankActivity.this));
            } catch (Exception e) {

            }
            if (currentVersion == -1) return;


            //当前是MainActivity，获取服务器header返回的版本号
            if (version != null) {
                if (version.contains("F")) {
                    forceUpdate = true;
                }
                String[] fs = version.split("F");
                versionCode = fs[0];
                try {
                    versionNum = Integer.parseInt(versionCode);
                } catch (Exception e) {

                }
                if (versionNum == -1) {
                    return;
                }

                UpdateManager.getUpdateManager().isUpdateNow(BlankActivity.this, versionNum, currentVersion, URLConstans.DOWNLOAD_ZHAOBIAO_ADDRESS, forceUpdate);
            }
        }
    };

    /**
     * 调起登录服务，使用默认参数
     */
    public void onClickLogin() {
        Request request = new Request.Builder()
                .setOperate(Request.LOGIN)
                .setLogoResId(R.drawable.newlogin_logo)
				.setLoginProtocolActivity(SoftwareUsageActivity.class)//控制是否显示协议，协议页面是一个activity
                .setForgetPwdEnable(true)//是否显示忘记密码
                .setPhoneLoginEnable(false)//是否显示手机动态码登录入口
                .setRegistEnable(false)//是否显示注册入口
                .setSocialEntranceEnable(false)//设置页面是否显示三方登录
                .setCloseButtonEnable(false)//设置页面是否带关闭按钮
                .create();
        LoginClient.launch(this, request);
    }

    /**
     * 实现需要的接口，SimpleLoginCallback为默认的空实现版本
     */
    private LoginCallback mLoginCallback = new SimpleLoginCallback() {
        /**
         * 账号登录完成回调
         */
        @SuppressLint("SwitchIntDef")
        @Override
        public void onLogin58Finished(boolean isSuccess, String msg, @Nullable LoginSDKBean loginSDKBean) {
            super.onLogin58Finished(isSuccess, msg, loginSDKBean);
            if (isSuccess && loginSDKBean != null) {
                ToastUtils.showToast(BlankActivity.this, msg);
                checkLoginViewModel = new CheckLoginViewModel(BlankActivity.this, BlankActivity.this);
                checkLoginViewModel.login();
            }

            //需要单独处理返回按钮事件的可以判断code值
            if(loginSDKBean != null && loginSDKBean.getCode()==LoginSDKBean.CODE_CANCEL_OPERATION) {
                //CODE_CANCEL_OPERATION表示账号登录页面点击了返回按钮或返回键，取消了登录操作
                finish();
            }
        }
    };

    private void initDailog() {
        if (alertDialog == null) {
            alertDialog = new ZhaoBiaoDialog(this, "");
            alertDialog.setCancelButtonGone();
            alertDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
                @Override
                public void onDialogOkClick() {
                    if (alertDialog != null && alertDialog.isShowing() && !BlankActivity.this.isFinishing()) {
                        alertDialog.dismiss();
                        finish();
                    }
                }

                @Override
                public void onDialogCancelClick() {

                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing() && !BlankActivity.this.isFinishing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        LoginClient.unregister(mLoginCallback);

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onLoadingSuccess(Object t) {
//		stopLoading();
        if (t instanceof LoginBean) {
            LoginBean loginBean = (LoginBean) t;

            String companyName = loginBean.getCompanyName();

            int hasValidated = loginBean.getHasValidated();

            long userId = loginBean.getUserId();

            String userName = LoginClient.doGetUnameOperate(this);

            UserUtils.saveUser(this, userId + "", companyName, userName);

            HYMob.getDataListByLoginSuccess(this, HYEventConstans.EVENT_ID_LOGIN, "1", userName);

            // 用于测试，写死数据"24454277549825",实际用UserUtils.getUserId(LoginActivity.this)
            MiPushClient.setAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
            //个推注册别名
            boolean result = GePushProxy.bindPushAlias(getApplicationContext(), userId + "_" + PhoneUtils.getIMEI(this));
//            Toast.makeText(this, "注册别名结果:" + result, Toast.LENGTH_SHORT).show();


            if (hasValidated == 1) {    //判断是否验证过手机 1没有验证过，0验证过

                ActivityUtils.goToActivity(this, MobileValidateActivity.class);
            } else {
                UserUtils.hasValidate(getApplicationContext());

                ActivityUtils.goToActivity(this, MainActivity.class);
            }

            UserUtils.setSessionTime(this, System.currentTimeMillis()); //存储登录成功时间

            finish();

        }
    }

    @Override
    public void onLoadingError(String msg) {
        //TODO:判断一下是不是在当前界面
        try {

            HYMob.getDataListByLoginError(this, HYEventConstans.EVENT_ID_LOGIN, "0", msg);

            if (alertDialog != null && !TextUtils.isEmpty(msg)) {
                alertDialog.setMessage(msg);
                alertDialog.show();
            }
        } catch (Exception e) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(this, HYEventConstans.PAGE_LOGIN, stop_time - resume_time);

    }
}
