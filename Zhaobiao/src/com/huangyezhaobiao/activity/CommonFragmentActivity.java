package com.huangyezhaobiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.AccountExpireBean;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.UserPhoneBean;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.BackToForeVM;
import com.huangyezhaobiao.vm.GlobalConfigVM;
import com.huangyezhaobiao.vm.LoginViewModel;
import com.huangyezhaobiao.vm.LogoutViewModel;
import com.huangyezhaobiao.windowf.AppExitService;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by 58 on 2016/2/24.
 */
public class CommonFragmentActivity extends FragmentActivity implements NetWorkVMCallBack{
    private BackToForeVM backToForeVM;
    protected GlobalConfigVM globalConfigVM;

    /**
     * 需要同步
     * @return
     */
    private boolean needAsync(){
        //当前时间戳
        long currentTimeLine  = System.currentTimeMillis();
        //从sp取时间戳
        long latTimeLine     = 0;
        try {
            latTimeLine = Long.valueOf(SPUtils.getVByK(CommonFragmentActivity.this,SPUtils.KEY_TIMELINE_GLOBAL));
        } catch (NumberFormatException e) {
            latTimeLine  = 0;
            e.printStackTrace();
        }
        return TimeUtils.beyond24Hour(currentTimeLine,latTimeLine);//没有在时间戳范围内

    }
    private NetWorkVMCallBack globalConfigCallBack = new NetWorkVMCallBack() {
        @Override
        public void onLoadingStart() {

        }

        @Override
        public void onLoadingSuccess(Object t) {
            //进行存储操作
            if(t instanceof GlobalConfigBean){
                GlobalConfigBean globalConfigBean = (GlobalConfigBean) t;
                Log.e("shenzhixinww","global:"+globalConfigBean.toString());
                //首先判断是不是需要增量拉取信息，存到SP中
                String isIncrementalPull = globalConfigBean.getIsIncrementalPull();
                SPUtils.saveKV(CommonFragmentActivity.this,GlobalConfigBean.KEY_isIncrementalPull,isIncrementalPull);
                //用户手机号
                String status = globalConfigBean.getUserPhoneResult().getStatus();
                if(TextUtils.equals(UserPhoneBean.SUCCESS,status)){
                    String userPhone = globalConfigBean.getUserPhoneResult().getUserPhone();
                    SPUtils.saveKV(CommonFragmentActivity.this,GlobalConfigBean.KEY_USERPHONE,userPhone);
                }
                //网灵通信息
                AccountExpireBean wltAlertResult = globalConfigBean.getWltAlertResult();
                String expireState = wltAlertResult.getExpireState();
                String msg = wltAlertResult.getMsg();
                SPUtils.saveKV(CommonFragmentActivity.this,GlobalConfigBean.KEY_WLT_EXPIRE,expireState);
                SPUtils.saveKV(CommonFragmentActivity.this,GlobalConfigBean.KEY_WLT_EXPIRE_MSG,msg);
                Log.e("ashen","current:"+System.currentTimeMillis()+",userId:"+UserUtils.getUserId(CommonFragmentActivity.this));
                //更新时间戳
                SPUtils.saveKV(CommonFragmentActivity.this,SPUtils.KEY_TIMELINE_GLOBAL,System.currentTimeMillis()+"");
            }
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

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVM();

    }

    private void initVM() {
        backToForeVM = new BackToForeVM(null,this);

        globalConfigVM = new GlobalConfigVM(globalConfigCallBack,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SPUtils.fromBackground(this)){
            //从后台进来的,加接口
            Log.e("shenzhiixn", "fromBackground");
            SPUtils.toForeground(this);//现在应用从后台到前台了
            backToForeVM.report();
            if(needAsync() && !TextUtils.isEmpty(UserUtils.getUserId(this))){
                globalConfigVM.refreshUsers();
            }
            dialog = new ZhaoBiaoDialog(this, "提示", "登录失败，您输入的账户名和密码不符!");
            dialog.setCancelButtonGone();
            dialog.setOnDialogClickListener(dialogClickListener);
            loginViewModel = new LoginViewModel(vmCallBack,this);
            Log.v("从后台进来的",UserUtils.getAccountName(this) + "===" +UserUtils.getAccountEncrypt(this));
            loginViewModel.login(UserUtils.getAccountName(this), UserUtils.getAccountEncrypt(this),true);
        }else{
            Log.e("shenzhiixn","not fromBackground");
        }
        BDMob.getBdMobInstance().onResumeActivity(this);
    }

    /** added by chenguangming start**/
    private LogoutViewModel lvm;
    private ZhaoBiaoDialog.onDialogClickListener dialogClickListener = new ZhaoBiaoDialog.onDialogClickListener() {
        @Override
        public void onDialogOkClick() {
            dialog.dismiss();
            /** 跳转到登录的界面*/
            lvm = new LogoutViewModel(vmCallBack, CommonFragmentActivity.this);
            lvm.logout();
            SharedPreferencesUtils.clearLoginToken(getApplicationContext());
            //退出时注销个推
            GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
            //退出时注销小米推送
            MiPushClient.unsetAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
            UserUtils.clearUserInfo(getApplicationContext());
            ActivityUtils.goToActivity(CommonFragmentActivity.this, LoginActivity.class);
            finish();
        }

        @Override
        public void onDialogCancelClick() {

        }
    };

    /**
     * created by chenguangming 自动登录
     *  */
    private LoginViewModel loginViewModel;
    private ZhaoBiaoDialog dialog;

    private NetWorkVMCallBack vmCallBack = new NetWorkVMCallBack() {
        @Override
        public void onLoadingStart() {

        }

        @Override
        public void onLoadingSuccess(Object t) {

        }

        @Override
        public void onLoadingError(String msg) {
            try {
                if(dialog!=null && !TextUtils.isEmpty(msg)){
                    dialog.setMessage(msg);
                    dialog.show();
                }
            } catch (Exception e) {
                Toast.makeText(CommonFragmentActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoadingCancel() {

        }

        @Override
        public void onNoInterNetError() {
            Toast.makeText(CommonFragmentActivity.this, getString(R.string.no_network),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoginInvalidate() {
            Toast.makeText(CommonFragmentActivity.this, getString(R.string.login_login_invalidate),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVersionBack(String version) {

        }
    };
    /** added by chenguangming end */

    @Override
    protected void onStop() {
        super.onStop();
        if(!BiddingApplication.getBiddingApplication().isAppOnForeground()){//到后台了
            Log.e("shenzhiixn","to background");
            SPUtils.toBackground(this);
            //openAppExitService();
        }else{
            Log.e("shenzhiixn","not to background");
        }
    }

    /**
     * 开启appexitService
     */
    protected void openAppExitService(){
        Intent intent = new Intent(this, AppExitService.class);
        startService(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
        BDMob.getBdMobInstance().onPauseActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

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
        Log.e("shenyy","version:"+version);

    }
}
