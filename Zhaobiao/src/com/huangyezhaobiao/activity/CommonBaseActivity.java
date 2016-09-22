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
public abstract class CommonBaseActivity extends BaseActivity implements NetWorkVMCallBack{
    private static final String TAG = "ActivityBackGround." + CommonBaseActivity.class.getName();
    private BackToForeVM backToForeVM;
    protected GlobalConfigVM globalConfigVM;
//    private static final String TAG = CommonBaseActivity.class.getName();

    protected long resume_time,stop_time;

    protected ZhaoBiaoDialog exitDialog;
    protected  ZhaoBiaoDialog LogoutDialog;

    private  final static String CLOSE_ACTIVITTY = "CLOSE_ACTIVITTY";

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
        registerReceiver(this.broadcastReceiver, filter); // 注册
    }

    protected void close() {
        Intent intent = new Intent();
        intent.setAction(CLOSE_ACTIVITTY); // 说明动作
        sendBroadcast(intent);// 该函数用于发送广播
//        finish();
    }

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

            String  time = SPUtils.getVByK(CommonBaseActivity.this, SPUtils.KEY_TIMELINE_GLOBAL);
            if(!TextUtils.isEmpty(time)){
                latTimeLine = Long.valueOf(time);
            }
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
      if(UserUtils.isHasSessionTime(CommonBaseActivity.this)){
          latTimeLine = UserUtils.getSessionTime(CommonBaseActivity.this);
      }
        } catch (Exception e) {
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
            Toast.makeText(CommonBaseActivity.this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
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
        globalConfigVM = new GlobalConfigVM(globalConfigCallBack,this);
        backToForeVM = new BackToForeVM(this,this);
        configExitDialog();
        initBroadcast();
    }

    @Override
    protected void onResume() {
        super.onResume();

        resume_time = System.currentTimeMillis();

        initLogoutDialog();


        //封装了的百度统计
        BDMob.getBdMobInstance().onResumeActivity(this);
        if(SPUtils.fromBackground(this)){
            //从后台进来的,加接口
            Log.e(TAG, "fromBackground");
            SPUtils.toForeground(this);//现在应用到前台了

            HYMob.getDataList(this, HYEventConstans.EVENT_ID_APP_OPEND);

            if(!TextUtils.isEmpty(UserUtils.getUserId(this))){
                backToForeVM.report();
                LogUtils.LogV("XXXXX","backToForeVM");
            }

            if( needAsync() && !TextUtils.isEmpty(UserUtils.getUserId(this))){
                globalConfigVM.refreshUsers();
                LogUtils.LogV("XXXXX", "globalConfigVM");
            }

            if(islogoutTime() && !TextUtils.isEmpty(UserUtils.getUserId(this))){
                if(LogoutDialog!=null){
                    LogoutDialog.setMessage("您已长时间无登录操作，请重新登录");
                    LogoutDialog.show();
                }
            }

        }else{
            Log.e(TAG,"not fromBackground");

        }
    }
    private LogoutViewModel lvm;
    public void initLogoutDialog(){
        LogoutDialog = new ZhaoBiaoDialog(this, "");
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
                if (LogoutDialog != null) {
                    LogoutDialog.dismiss();
                }
                /** 跳转到登录的界面*/
                lvm = new LogoutViewModel(vmCallBack, CommonBaseActivity.this);
                lvm.logout();
                SharedPreferencesUtils.clearLoginToken(getApplicationContext());
                //退出时注销个推
                GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
                //退出时注销小米推送
                MiPushClient.unsetAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()), null);
                UserUtils.clearUserInfo(getApplicationContext());

                LoginClient.doLogoutOperate(CommonBaseActivity.this);
                ActivityUtils.goToActivity(CommonBaseActivity.this, BlankActivity.class);
                close();
            }

            @Override
            public void onDialogCancelClick() {

            }
        });
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
            Toast.makeText(CommonBaseActivity.this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoginInvalidate() {

        }

        @Override
        public void onVersionBack(String version) {

        }

    };

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
        if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
            if(LogoutDialog!=null){
                LogoutDialog.setMessage("PPU过期，请重新登录");
                LogoutDialog.show();
            }
        }
    }

    @Override
    public void onLoadingCancel() {

    }

    @Override
    public void onNoInterNetError() {
    }

    @Override
    public void onLoginInvalidate() {
        GePushProxy.unBindPushAlias(getApplicationContext(), UserUtils.getUserId(getApplicationContext()));
        showExitDialog();
    }


    protected void configExitDialog(){
        exitDialog = new ZhaoBiaoDialog(this,
//                getString(R.string.sys_noti),
                getString(R.string.force_exit));
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
        exitDialog.setCancelButtonGone();
        exitDialog.setCancelable(false);
    }

    /**
     * 显示退出的对话框
     */
    protected void showExitDialog(){
        if(exitDialog!=null && !exitDialog.isShowing()){
            try {
                exitDialog.show();
            }catch (Exception e){
                Toast.makeText(this,getString(R.string.force_exit),Toast.LENGTH_SHORT).show();
                //TODO:退出登录
                LoginClient.doLogoutOperate(CommonBaseActivity.this);
                ActivityUtils.goToActivity(CommonBaseActivity.this, BlankActivity.class);

                //退出登录后的几件事
                /**
                 * 1.清除LoginToken
                 * 2.清除用户信息
                 */
                SharedPreferencesUtils.clearLoginToken(this);
                UserUtils.clearUserInfo(this);
              close();

            }

        }
    }

    /**
     * 显示退出的对话框
     */
    protected void dismissExitDialog(){
        if(exitDialog!=null && exitDialog.isShowing()){
            try {
                exitDialog.dismiss();
            }catch (Exception e){
                Toast.makeText(this,getString(R.string.force_exit),Toast.LENGTH_SHORT).show();
                //TODO:退出登录
            }finally {
                LoginClient.doLogoutOperate(CommonBaseActivity.this);
                ActivityUtils.goToActivity(CommonBaseActivity.this, BlankActivity.class);

                //退出登录后的几件事
                /**
                 * 1.清除LoginToken
                 * 2.清除用户信息
                 */
                SharedPreferencesUtils.clearLoginToken(this);
                UserUtils.clearUserInfo(this);
                close();
            }
        }
    }
}
