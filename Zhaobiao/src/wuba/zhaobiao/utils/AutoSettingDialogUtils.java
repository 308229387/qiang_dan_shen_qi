package wuba.zhaobiao.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import wuba.zhaobiao.mine.activity.AutoSettingActivity;

/**
 * Created by SongYongmeng on 2016/7/31.
 * 描    述：首页Activity里PPU过期和长时间未登陆时弹出的退出Dialog
 */
public class AutoSettingDialogUtils {

    private Activity context;
    protected ZhaoBiaoDialog autoSettingDialog;
    private String msg;

    public AutoSettingDialogUtils(Activity context, String msg) {
        setInfo(context, msg);
    }

    private void setInfo(Activity context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    public void showSingleButtonDialog() { //弹框只显示一个确定按钮
        createDialog(context);
        configSingleButtonDialog();
        showDialog();
    }

    public void showTwoButtonDialog(){ //弹框显示两个按钮--取消、确定
        createDialog(context);
        configTwoButtonDialog();
        showDialog();
    }

    private void createDialog(Activity activity) {
        autoSettingDialog = new ZhaoBiaoDialog(activity, "");
    }

    private void configSingleButtonDialog() {
        autoSettingDialog.setCancelButtonGone();
        autoSettingDialog.setCancelable(false);
        autoSettingDialog.setOnDismissListener(new Dismiss());
        autoSettingDialog.setOnDialogClickListener(new DialogClickListener());
        autoSettingDialog.setMessage(msg);
    }

    private void configTwoButtonDialog() {
        autoSettingDialog.setNagativeText("暂不设置");
        autoSettingDialog.setPositiveText("去设置");
        autoSettingDialog.setOnDismissListener(new Dismiss());
        autoSettingDialog.setOnDialogClickListener(new DialogClickListener());
        autoSettingDialog.setMessage(msg);
    }

    private void showDialog() {
        autoSettingDialog.show();
    }


    private void dismiss() {
        autoSettingDialog.dismiss();
    }



    private class DialogClickListener implements ZhaoBiaoDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            dismiss();
            ActivityUtils.goToActivity(context, AutoSettingActivity.class);
//            Intent intent = AutoSettingsActivity.onNewIntent(context);
//            context.startActivity(intent);
            SPUtils.saveAutoSetting(context);
        }

        @Override
        public void onDialogCancelClick() {
            dismiss();
            SPUtils.saveAutoSetting(context);
        }
    }

    private class Dismiss implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface dialog) {
            autoSettingDialog = null;
        }
    }


}
