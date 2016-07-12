package com.huangyezhaobiao.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.BackToForeVM;
import com.huangyezhaobiao.vm.GlobalConfigVM;
import com.huangyezhaobiao.vm.LogoutViewModel;
import com.huangyezhaobiao.windowf.AppExitService;
import com.wuba.loginsdk.external.LoginCallback;
import com.wuba.loginsdk.external.LoginClient;
import com.wuba.loginsdk.external.Request;
import com.wuba.loginsdk.external.SimpleLoginCallback;
import com.wuba.loginsdk.model.LoginSDKBean;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by 58 on 2016/2/24.
 */
public class CommonFragmentActivity extends FragmentActivity implements NetWorkVMCallBack {

    private static final String TAG = "ActivityBackGround."+CommonFragmentActivity.class.getName();
    private BackToForeVM backToForeVM;
    protected GlobalConfigVM globalConfigVM;
//    private static final String TAG = CommonFragmentActivity.class.getName();

    protected long resume_time,stop_time;

    protected ZhaoBiaoDialog exitDialog; //当用户被挤掉时,显示这个对话框
    private ZhaoBiaoDialog LogoutDialog;

    protected final static String CLOSE_ACTIVITTY = "CLOSE_ACTIVITTY";


    // 写一个广播的内部类，当收到动作时，结束activity
    protected  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错
            ((Activity) context).finish();
        }

    };

    protected void initBroadcast(){
        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(CLOSE_ACTIVITTY);
        registerReceiver(broadcastReceiver, filter); // 注册
    }

    protected void close() {
        Intent intent = new Intent();
        intent.setAction(CLOSE_ACTIVITTY); // 说明动作
        sendBroadcast(intent);// 该函数用于发送广播
//        finish();
    }


    /**
     * 需要同步
     *
     * @return
     */
    private boolean needAsync() {
        //当前时间戳
        long currentTimeLine = System.currentTimeMillis();
        //从sp取时间戳
        long latTimeLine = 0;
        try {
            latTimeLine = Long.valueOf(SPUtils.getVByK(CommonFragmentActivity.this, SPUtils.KEY_TIMELINE_GLOBAL));
        } catch (NumberFormatException e) {
            latTimeLine = 0;
            e.printStackTrace();
        }
        return TimeUtils.beyond24Hour(currentTimeLine, latTimeLine);//没有在时间戳范围内

    }

    /**
     *
     * 需要退出重新登录
     * @return
     */
    private boolean islogoutTime(){
        //当前时间戳
        long currentTimeLine  = System.currentTimeMillis();
        //从sp取时间戳
        long latTimeLine     = 0;
        try {
            latTimeLine = Long.valueOf( UserUtils.getSessionTime(CommonFragmentActivity.this));
        } catch (NumberFormatException e) {
            latTimeLine = 0;
            e.printStackTrace();
        }
        return TimeUtils.beyond13Days(currentTimeLine, latTimeLine);//没有在时间戳范围内

    }


    private NetWorkVMCallBack globalConfigCallBack = new NetWorkVMCallBack() {
        @Override
        public void onLoadingStart() {

        }

        @Override
        public void onLoadingSuccess(Object t) {
            //进行存储操作
            if (t instanceof GlobalConfigBean) {
                GlobalConfigBean globalConfigBean = (GlobalConfigBean) t;
                Log.e(TAG, "global:" + globalConfigBean.toString());
                //首先判断是不是需要增量拉取信息，存到SP中
                String isIncrementalPull = globalConfigBean.getIsIncrementalPull();
                SPUtils.saveKV(CommonFragmentActivity.this, GlobalConfigBean.KEY_isIncrementalPull, isIncrementalPull);
                //用户手机号
                String status = globalConfigBean.getUserPhoneResult().getStatus();
                if (TextUtils.equals(UserPhoneBean.SUCCESS, status)) {
                    String userPhone = globalConfigBean.getUserPhoneResult().getUserPhone();
                    LogUtils.LogV("wjl","CommonFragmentActivity + userPhone:" + userPhone);
                    SPUtils.saveKV(CommonFragmentActivity.this, GlobalConfigBean.KEY_USERPHONE, userPhone);
                }
                //网灵通信息
                AccountExpireBean wltAlertResult = globalConfigBean.getWltAlertResult();
                String expireState = wltAlertResult.getExpireState();
                String msg = wltAlertResult.getMsg();
                SPUtils.saveKV(CommonFragmentActivity.this, GlobalConfigBean.KEY_WLT_EXPIRE, expireState);
                SPUtils.saveKV(CommonFragmentActivity.this, GlobalConfigBean.KEY_WLT_EXPIRE_MSG, msg);
                Log.e(TAG, "current:" + System.currentTimeMillis() + ",userId:" + UserUtils.getUserId(CommonFragmentActivity.this));
                //更新时间戳
                SPUtils.saveKV(CommonFragmentActivity.this, SPUtils.KEY_TIMELINE_GLOBAL, System.currentTimeMillis() + "");
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
        configExitDialog();
        initBroadcast();
    }

    private void initVM() {
        backToForeVM = new BackToForeVM(null, this);

        globalConfigVM = new GlobalConfigVM(globalConfigCallBack, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        resume_time = System.currentTimeMillis();

        initLogoutDialog();

        if (SPUtils.fromBackground(this)) {

            //从后台进来的,加接口
            Log.e(TAG, "fromBackground");
            SPUtils.toForeground(this);//现在应用从后台到前台了

            HYMob.getDataList(this, HYEventConstans.EVENT_ID_APP_OPEND);

            if(!TextUtils.isEmpty(UserUtils.getUserId(this))){
                backToForeVM.report();
            }

            if (needAsync() && !TextUtils.isEmpty(UserUtils.getUserId(this))) {
                globalConfigVM.refreshUsers();
            }

            if(islogoutTime() && !TextUtils.isEmpty(UserUtils.getUserId(this))){
                if(LogoutDialog!=null){
                    LogoutDialog.setMessage("您已长时间无登录操作，请重新登录");
                    LogoutDialog.show();
                }
            }


        } else {
            Log.e(TAG, "not fromBackground");
        }


        BDMob.getBdMobInstance().onResumeActivity(this);
    }
    private LogoutViewModel lvm;
    public void initLogoutDialog(){
        LogoutDialog = new ZhaoBiaoDialog(this,"");
        LogoutDialog.setCancelButtonGone();
        LogoutDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LogoutDialog = null;
            }
        });
        LogoutDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                LogoutDialog.dismiss();
                /** 跳转到登录的界面*/
                lvm = new LogoutViewModel(CommonFragmentActivity.this, CommonFragmentActivity.this);
                lvm.logout();
                SharedPreferencesUtils.clearLoginToken(getApplicationContext());
                //退出时注销个推
                GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
                //退出时注销小米推送
                MiPushClient.unsetAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
                UserUtils.clearUserInfo(getApplicationContext());

                LoginClient.doLogoutOperate(CommonFragmentActivity.this);
                ActivityUtils.goToActivity(CommonFragmentActivity.this, BlankActivity.class);
                close();
            }

            @Override
            public void onDialogCancelClick() {

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        stop_time = System.currentTimeMillis();

        if (!BiddingApplication.getBiddingApplication().isAppOnForeground()) {//到后台了
            Log.e(TAG, "to background");
            SPUtils.toBackground(this);
            //openAppExitService();
        } else {
            Log.e(TAG, "not to background");
        }

    }

    /**
     * 开启appexitService
     */
    protected void openAppExitService() {
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
        if (!this.isFinishing() && LogoutDialog != null && LogoutDialog.isShowing()) {
            LogoutDialog.dismiss();
            LogoutDialog = null;
        }

        if (!this.isFinishing() && exitDialog != null && exitDialog.isShowing()) {
            exitDialog.dismiss();
            exitDialog = null;
        }
    }

    @Override
    public void onLoadingStart() {

    }

    @Override
    public void onLoadingSuccess(Object t) {

    }

    @Override
    public void onLoadingError(String msg) {

        if( !TextUtils.isEmpty(msg) && msg.equals("PPU")){
            if(LogoutDialog!=null){
                LogoutDialog.setMessage("您已经长时间无登录操作，请重新登录");
                LogoutDialog.show();
            }
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
        GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
        showExitDialog();
    }

    @Override
    public void onVersionBack(String version) {
        Log.e("shenyy", "version:" + version);

    }


    /**
     * 配置退出的对话框
     */
    private void configExitDialog() {
        exitDialog = new ZhaoBiaoDialog(CommonFragmentActivity.this,
//                getString(R.string.sys_noti),
                getString(R.string.force_exit));
        exitDialog.setCancelButtonGone();
        exitDialog.setCancelable(false);
        exitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                exitDialog = null;
            }
        });
        exitDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                dismissExitDialog();
            }

            @Override
            public void onDialogCancelClick() {

            }
        });
    }

    /**
     * 显示退出的对话框
     */
    protected void showExitDialog() {
        if (exitDialog != null && !exitDialog.isShowing()) {
            try {
                exitDialog.show();
            } catch (Exception e) {
                Toast.makeText(CommonFragmentActivity.this, getString(R.string.force_exit), Toast.LENGTH_SHORT).show();
                //TODO:退出登录
                LoginClient.doLogoutOperate(CommonFragmentActivity.this);
                ActivityUtils.goToActivity(CommonFragmentActivity.this, BlankActivity.class);
                //退出登录后的几件事
                /**
                 * 1.清除LoginToken
                 * 2.清除用户信息
                 */
                SharedPreferencesUtils.clearLoginToken(CommonFragmentActivity.this);
                UserUtils.clearUserInfo(CommonFragmentActivity.this);
               close();
            }
        }
    }

    /**
     * 关闭退出的对话框
     */
    protected void dismissExitDialog() {
        if (exitDialog != null && exitDialog.isShowing()) {
            try {
                exitDialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(CommonFragmentActivity.this, getString(R.string.force_exit), Toast.LENGTH_SHORT).show();
                //TODO:退出登录
            } finally {
                LoginClient.doLogoutOperate(CommonFragmentActivity.this);
                ActivityUtils.goToActivity(CommonFragmentActivity.this, BlankActivity.class);

                //退出登录后的几件事
                /**
                 * 1.清除LoginToken
                 * 2.清除用户信息
                 */
                SharedPreferencesUtils.clearLoginToken(CommonFragmentActivity.this);
                UserUtils.clearUserInfo(CommonFragmentActivity.this);
                close();
            }
        }
    }
}
