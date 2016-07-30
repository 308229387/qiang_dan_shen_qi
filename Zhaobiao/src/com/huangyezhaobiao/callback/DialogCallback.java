package com.huangyezhaobiao.callback;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.wuba.loginsdk.external.LoginClient;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;
import wuba.zhaobiao.common.activity.LoginActivity;

/**
 * Created by SongYongmeng on 2016/7/29.
 * 描    述：对于网络请求是否需要弹出退出对话框
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private Activity activity;
    protected ZhaoBiaoDialog exitDialog;
    private String NEED_DOWN_LINE = "need_down_line";


    public DialogCallback(Activity activity, Class<T> clazz) {
        super(clazz);
        this.activity = activity;
    }

    public DialogCallback(Activity activity, Type type) {
        super(type);
        initDialog(activity);
    }

    private void initDialog(Activity activity) {
        initializationDialog(activity);
        setListener();
        setConfig();
        showDialog();
    }

    private void showDialog() {
        exitDialog.show();
    }

    private void setConfig() {
        exitDialog.setCancelButtonGone();
        exitDialog.setCancelable(false);
    }

    private void initializationDialog(Activity activity) {
        exitDialog = new ZhaoBiaoDialog(activity, activity.getString(R.string.force_exit));
    }

    private void setListener() {
        exitDialog.setOnDismissListener(new Dismiss());
        exitDialog.setOnDialogClickListener(new Dialogclick());
    }

    private void dismissExitDialog() {
        exitActivity();
        sortOutInfo();
    }

    private void sortOutInfo() {
        SharedPreferencesUtils.clearLoginToken(activity);
        UserUtils.clearUserInfo(activity);
    }

    private void exitActivity() {
        LoginClient.doLogoutOperate(activity);
        ActivityUtils.goToActivity(activity, LoginActivity.class);
    }

    @Override
    public void onAfter(boolean isFromCache, @Nullable T t, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onAfter(isFromCache, t, call, response, e);
        if (e != null && e.getMessage().equals(NEED_DOWN_LINE))
            initDialog(activity);
    }

    private class Dismiss implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface dialog) {
            exitDialog = null;
        }
    }

    private class Dialogclick implements ZhaoBiaoDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            dismissExitDialog();
        }

        @Override
        public void onDialogCancelClick() {

        }
    }
}

