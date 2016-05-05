package com.huangyezhaobiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

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
public class CommonFragmentActivity extends FragmentActivity{
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVM();

    }

    private void initVM() {
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
        }else{
            Log.e("shenzhiixn","not fromBackground");
        }
        BDMob.getBdMobInstance().onResumeActivity(this);
    }


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
}
