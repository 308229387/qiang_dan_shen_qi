package com.huangyezhaobiao.presenter;

import android.content.Context;
import android.content.Intent;

import com.huangyezhaobiao.activity.AutoSettingsActivity;
import com.huangyezhaobiao.activity.MobileBindChangeActivity;

/**
 * Created by shenzhixin on 2015/11/12.
 */
public class SettingsPresenter {
    private Context context;
    public SettingsPresenter(Context context){
        this.context = context;
    }

    /**
     * 去手机绑定页面
     */
    public void goToMobileChangeActivity(String mobile){
        Intent intent = MobileBindChangeActivity.onNewIntent(context,mobile);
        context.startActivity(intent);
    }

    /**
     * 去自定义设置界面
     */
    public void goToAutoSettingsActivity(Context context){
        Intent intent = AutoSettingsActivity.onNewIntent(context);
        context.startActivity(intent);
    }
}
