package com.huangyezhaobiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huangye.commonlib.activity.BaseActivity;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.vm.BackToForeVM;
import com.huangyezhaobiao.windowf.AppExitService;

/**
 * Created by 58 on 2016/2/24.
 */
public abstract class CommonBaseActivity extends BaseActivity{
    private BackToForeVM backToForeVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
          //  openAppExitService();
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
