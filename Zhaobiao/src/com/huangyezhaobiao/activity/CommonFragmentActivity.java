package com.huangyezhaobiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.vm.BackToForeVM;
import com.huangyezhaobiao.windowf.AppExitService;

/**
 * Created by 58 on 2016/2/24.
 */
public class CommonFragmentActivity extends FragmentActivity{
    private BackToForeVM backToForeVM;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SPUtils.fromBackground(this)){
            //从后台进来的,加接口
            Log.e("shenzhiixn", "fromBackground");
            SPUtils.toForeground(this);//现在应用从后台到前台了
            backToForeVM.report();
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
           // openAppExitService();
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
