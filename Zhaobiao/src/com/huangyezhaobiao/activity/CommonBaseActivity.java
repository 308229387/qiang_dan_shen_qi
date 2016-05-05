package com.huangyezhaobiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huangye.commonlib.activity.BaseActivity;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.AccountExpireBean;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.UserPhoneBean;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.vm.BackToForeVM;
import com.huangyezhaobiao.vm.GlobalConfigVM;
import com.huangyezhaobiao.windowf.AppExitService;

/**
 * Created by 58 on 2016/2/24.
 */
public abstract class CommonBaseActivity extends BaseActivity{
    private BackToForeVM backToForeVM;
    private GlobalConfigVM globalConfigVM;
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
                SPUtils.saveKV(CommonBaseActivity.this,GlobalConfigBean.KEY_isIncrementalPull,isIncrementalPull);
                //用户手机号
                String status = globalConfigBean.getUserPhoneResult().getStatus();
                if(TextUtils.equals(UserPhoneBean.SUCCESS,status)){
                    String userPhone = globalConfigBean.getUserPhoneResult().getUserPhone();
                    SPUtils.saveKV(CommonBaseActivity.this,GlobalConfigBean.KEY_USERPHONE,userPhone);
                }
                //网灵通信息
                AccountExpireBean wltAlertResult = globalConfigBean.getWltAlertResult();
                String expireState = wltAlertResult.getExpireState();
                String msg = wltAlertResult.getMsg();
                SPUtils.saveKV(CommonBaseActivity.this,GlobalConfigBean.KEY_WLT_EXPIRE,expireState);
                SPUtils.saveKV(CommonBaseActivity.this,GlobalConfigBean.KEY_WLT_EXPIRE_MSG,msg);
                //更新时间戳
                Log.e("ashen","current:"+System.currentTimeMillis()+",userId:"+UserUtils.getUserId(CommonBaseActivity.this));
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
        },this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //封装了的百度统计
        BDMob.getBdMobInstance().onResumeActivity(this);
        if(SPUtils.fromBackground(this)){
            //从后台进来的,加接口
            Log.e("shenzhiixn", "fromBackground");
            SPUtils.toForeground(this);//现在应用到前台了
            backToForeVM.report();

            if(needAsync() && !TextUtils.isEmpty(UserUtils.getUserId(this))){
                globalConfigVM.refreshUsers();
            }
        }else{
            Log.e("shenzhiixn","not fromBackground");
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
    protected void onStop() {
        super.onStop();
        if(!BiddingApplication.getBiddingApplication().isAppOnForeground()){//到后台了
            Log.e("shenzhiixn","to background");
            SPUtils.toBackground(this);
           // openAppExitService();
        }else{
            Log.e("shenzhiixn","not to background");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //封装了的百度统计
        BDMob.getBdMobInstance().onPauseActivity(this);
    }


}
