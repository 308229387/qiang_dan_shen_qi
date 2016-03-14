package com.huangyezhaobiao.presenter;

import android.content.Context;
import android.content.Intent;

import com.huangyezhaobiao.activity.AutoSettingsActivity;

/**
 * Created by shenzhixin on 2015/11/21.
 */
public class MainPresenter {
    public void goToAutoSettings(Context context){
        Intent intent = AutoSettingsActivity.onNewIntent(context);
        context.startActivity(intent);
    }
}
