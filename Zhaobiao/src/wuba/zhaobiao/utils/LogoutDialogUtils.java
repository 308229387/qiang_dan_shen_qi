package wuba.zhaobiao.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import wuba.zhaobiao.common.activity.LoginActivity;

/**
 * Created by SongYongmeng on 2016/7/31.
 * 描    述：首页Activity里PPU过期和长时间未登陆时弹出的退出Dialog
 */
public class LogoutDialogUtils {

    private Activity context;
    protected ZhaoBiaoDialog LogoutDialog;
    private String msg;

    public LogoutDialogUtils(Activity context, String msg) {
        setInfo(context, msg);
    }

    private void setInfo(Activity context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    public void initAndShowDialog() {
        creatDialog(context);
        configDialog();
        showDialog();
    }

    private void creatDialog(Activity activity) {
        LogoutDialog = new ZhaoBiaoDialog(activity, "");
    }

    private void configDialog() {
        LogoutDialog.setCancelButtonGone();
        LogoutDialog.setCancelable(false);
        LogoutDialog.setOnDismissListener(new Dismiss());
        LogoutDialog.setOnDialogClickListener(new DialogClickListener());
        LogoutDialog.setMessage(msg);
    }

    private void showDialog() {
        LogoutDialog.show();
    }

    private void clearInfoAndService() {
        new LogoutClearInfoAndStopServiceUtils(context).clear();
    }

    private void dismiss() {
        LogoutDialog.dismiss();
    }


    private void goToLogin() {
        ActivityUtils.goToActivity(context, LoginActivity.class);
    }

    private class DialogClickListener implements ZhaoBiaoDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            dismiss();
            clearInfoAndService();
            goToLogin();
        }

        @Override
        public void onDialogCancelClick() {

        }
    }

    private class Dismiss implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface dialog) {
            LogoutDialog = null;
        }
    }

}
