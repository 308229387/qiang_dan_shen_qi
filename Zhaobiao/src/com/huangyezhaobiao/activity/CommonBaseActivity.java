package com.huangyezhaobiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huangye.commonlib.activity.BaseActivity;
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
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
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
public abstract class CommonBaseActivity extends BaseActivity implements NetWorkVMCallBack{
    private static final String TAG = "ActivityBackGround." + CommonBaseActivity.class.getName();
    private BackToForeVM backToForeVM;
    private GlobalConfigVM globalConfigVM;
//    private static final String TAG = CommonBaseActivity.class.getName();

    protected long resume_time,stop_time,destory_time;

    private NetWorkVMCallBack globalConfigCallBack = new NetWorkVMCallBack() {
        @Override
        public void onLoadingStart() {

        }

        @Override
        public void onLoadingSuccess(Object t) {
            //进行存储操作
            if(t instanceof GlobalConfigBean){
                GlobalConfigBean globalConfigBean = (GlobalConfigBean) t;
                Log.e(TAG,"global:"+globalConfigBean.toString());
                //首先判断是不是需要增量拉取信息，存到SP中
                String isIncrementalPull = globalConfigBean.getIsIncrementalPull();
                SPUtils.saveKV(CommonBaseActivity.this,GlobalConfigBean.KEY_isIncrementalPull,isIncrementalPull);
                //用户手机号
                String status = globalConfigBean.getUserPhoneResult().getStatus();
                if(TextUtils.equals(UserPhoneBean.SUCCESS,status)){
                    String userPhone = globalConfigBean.getUserPhoneResult().getUserPhone();
                    LogUtils.LogV("wjl", "CommonBaseActivity +userPhone:" + userPhone);
                    SPUtils.saveKV(CommonBaseActivity.this,GlobalConfigBean.KEY_USERPHONE,userPhone);
                }
                //网灵通信息
                AccountExpireBean wltAlertResult = globalConfigBean.getWltAlertResult();
                String expireState = wltAlertResult.getExpireState();
                String msg = wltAlertResult.getMsg();
                SPUtils.saveKV(CommonBaseActivity.this,GlobalConfigBean.KEY_WLT_EXPIRE,expireState);
                SPUtils.saveKV(CommonBaseActivity.this,GlobalConfigBean.KEY_WLT_EXPIRE_MSG,msg);
                //更新时间戳
                Log.e(TAG,"current:"+System.currentTimeMillis()+",userId:"+UserUtils.getUserId(CommonBaseActivity.this));
                SPUtils.saveKV(CommonBaseActivity.this,SPUtils.KEY_TIMELINE_GLOBAL,System.currentTimeMillis()+"");
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
            latTimeLine = Long.valueOf(SPUtils.getVByK(CommonBaseActivity.this,SPUtils.KEY_TIMELINE_GLOBAL));
        } catch (NumberFormatException e) {
            latTimeLine = 0;
            e.printStackTrace();
        }
        return TimeUtils.beyond24Hour(currentTimeLine,latTimeLine);//没有在时间戳范围内

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalConfigVM = new GlobalConfigVM(globalConfigCallBack,this);
        backToForeVM = new BackToForeVM(new NetWorkVMCallBack() {
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

            }
        },this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        resume_time = System.currentTimeMillis();

        //封装了的百度统计
        BDMob.getBdMobInstance().onResumeActivity(this);
        if(SPUtils.fromBackground(this)){
            //从后台进来的,加接口
            Log.e(TAG, "fromBackground");
            SPUtils.toForeground(this);//现在应用到前台了

            HYMob.getDataList(this, HYEventConstans.EVENT_ID_APP_OPEND);

            backToForeVM.report();
            if(needAsync() && !TextUtils.isEmpty(UserUtils.getUserId(this))){
                globalConfigVM.refreshUsers();
            }
            Log.v(TAG,"=>" + UserUtils.getAccountName(this));
            Log.v(TAG,">>" + UserUtils.getAccountEncrypt(this));
            if(!"".equals(UserUtils.getAccountName(this)) && !"".equals(UserUtils.getAccountEncrypt(this)) && UserUtils.isOutOfDate(this)) {
                dialog = new ZhaoBiaoDialog(this, "提示", "登录失败，您输入的账户名和密码不符!");
                dialog.setCancelButtonGone();
                dialog.setOnDialogClickListener(dialogClickListener);
                loginViewModel = new LoginViewModel(vmCallBack, this);
                loginViewModel.login(UserUtils.getAccountName(this), UserUtils.getAccountEncrypt(this), true);
            }
        }else{
            Log.e(TAG,"not fromBackground");

        }
    }

    /** added by chenguangming start**/
    private LogoutViewModel lvm;
    private ZhaoBiaoDialog.onDialogClickListener dialogClickListener = new ZhaoBiaoDialog.onDialogClickListener() {
        @Override
        public void onDialogOkClick() {
            dialog.dismiss();
            /** 跳转到登录的界面*/
            lvm = new LogoutViewModel(vmCallBack, CommonBaseActivity.this);
            lvm.logout();
            SharedPreferencesUtils.clearLoginToken(getApplicationContext());
            //退出时注销个推
            GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
            //退出时注销小米推送
            MiPushClient.unsetAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
            UserUtils.clearUserInfo(getApplicationContext());
            ActivityUtils.goToActivity(CommonBaseActivity.this, LoginActivity.class);
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
                Toast.makeText(CommonBaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoadingCancel() {

        }

        @Override
        public void onNoInterNetError() {
            Toast.makeText(CommonBaseActivity.this, getString(R.string.no_network),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoginInvalidate() {
            Toast.makeText(CommonBaseActivity.this, getString(R.string.login_login_invalidate),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVersionBack(String version) {

        }
    };
    /** added by chenguangming end */

    /**
     * 开启appexitService
     */
    protected void openAppExitService(){
        Intent intent = new Intent(this, AppExitService.class);
        startService(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();

        stop_time = System.currentTimeMillis();

        if(!BiddingApplication.getBiddingApplication().isAppOnForeground()){//到后台了
            Log.e(TAG,"to background");
            SPUtils.toBackground(this);
           // openAppExitService();
        }else{
            Log.e(TAG,"not to background");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //封装了的百度统计
        BDMob.getBdMobInstance().onPauseActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onVersionBack(String version) {

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
}
