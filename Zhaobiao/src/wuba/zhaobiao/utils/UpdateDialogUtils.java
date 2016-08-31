package wuba.zhaobiao.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

/**
 * Created by SongYongmeng on 2016/7/31.
 * 描    述：首页Activity里PPU过期和长时间未登陆时弹出的退出Dialog
 */
public class UpdateDialogUtils {

    private Activity context;
    protected ZhaoBiaoDialog updatetDialog;
    private String msg;

    public UpdateDialogUtils(Activity context, String msg) {
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
        updatetDialog = new ZhaoBiaoDialog(activity, "");
    }

    private void configSingleButtonDialog() {
        updatetDialog.setCancelButtonGone();
        updatetDialog.setCancelable(false);
        updatetDialog.setOnDismissListener(new Dismiss());
        updatetDialog.setOnDialogClickListener(new DialogClickListener());
        updatetDialog.setMessage(msg);
    }

    private void configTwoButtonDialog() {
        updatetDialog.setOnDismissListener(new Dismiss());
        updatetDialog.setOnDialogClickListener(new DialogClickListener());
        updatetDialog.setMessage(msg);
    }

    private void showDialog() {
        if(updatetDialog != null){
            updatetDialog.show();
        }

    }


    private void dismiss() {
        updatetDialog.dismiss();
    }



    private class DialogClickListener implements ZhaoBiaoDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            dismiss();
            SPUtils.saveAlreadyFirstUpdate(context, false);
        }

        @Override
        public void onDialogCancelClick() {
            dismiss();
        }
    }

    private class Dismiss implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface dialog) {
            updatetDialog = null;
            String isSet = SPUtils.getVByK(context, GlobalConfigBean.KEY_SETSTATE);
            if (!TextUtils.equals("1", isSet) && SPUtils.isAutoSetting(context)) {
                new AutoSettingDialogUtils(context, context.getString(R.string.auto_setting_message)).showTwoButtonDialog();
            }
        }
    }


}
