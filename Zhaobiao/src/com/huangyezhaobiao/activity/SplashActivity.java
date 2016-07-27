package com.huangyezhaobiao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.CommonUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.VersionUtils;
import com.wuba.loginsdk.external.LoginClient;

import air.com.wuba.bangbang.common.impush.DeamonService;

/**
 * 引导界面，注册推送，等3秒进入相应界面，存储版本号
 */
public class SplashActivity extends Activity {
    private static final long DELAYED_TIMES = 3 * 1000;
    private SharedPreferences sp;
    private Handler handler = new Handler();
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        registPush();
        waitTime();
        setHeardColor();
    }

    private void registPush() {
        GePushProxy.initliazePush(this.getApplicationContext());
        Intent intent = new Intent(this, DeamonService.class);
        startService(intent);
    }

    private void waitTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToWhere();
            }
        }, DELAYED_TIMES);
    }

    private void setHeardColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().setBackgroundDrawable(null);
    }

    private void goToWhere() {
        String currentVersionName = getVersion();
        if (isFirstCome(currentVersionName)) {//进入引导界面
            ActivityUtils.goToActivity(context, GuideActivity.class);
        } else if (neverCome()) {//如果没有登录过
            ActivityUtils.goToActivity(context, BlankActivity.class);
        } else {//走主界面
            ActivityUtils.goToActivity(context, MainActivity.class);
        }
        sp.edit().putString(Constans.VERSION_NAME, currentVersionName).commit();
    }

    private String getVersion() {
        sp = getSharedPreferences(Constans.APP_SP, 0);
        String currentVersionName = "1.0.0";
        try {
            currentVersionName = VersionUtils.getVersionName(context);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionName;
    }

    private Boolean isFirstCome(String currentVersionName) {
        String mCurrentVersionName = currentVersionName.replace(".", "");
        String saveVersionName = sp.getString(Constans.VERSION_NAME,
                "1.0.0");
        Boolean isFirst = CommonUtils.compareTwoNumbersGuide(saveVersionName,
                mCurrentVersionName);
        return !isFirst;
    }

    private boolean neverCome() {
        return TextUtils.isEmpty(UserUtils.getUserId(this))
                || !UserUtils.isValidate(this)
                || TextUtils.isEmpty(LoginClient.doGetPPUOperate(BiddingApplication.getAppInstanceContext()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        BDMob.getBdMobInstance().onPauseActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BDMob.getBdMobInstance().onResumeActivity(BiddingApplication.getBiddingApplication());
        HYMob.getDataList(this, HYEventConstans.EVENT_ID_APP_OPEND);
    }

}
