package wuba.zhaobiao.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.huangyezhaobiao.view.ZhaoBiaoDialog;

/**
 * Created by SongYongmeng on 2016/7/31.
 * 描    述：商机清空时的弹窗
 */
public class BusinessClearDialogUtils {

    private Activity context;
    private ZhaoBiaoDialog clearDialog;
    private String msg;
    private ClearListener clearListener;


    public BusinessClearDialogUtils(Activity context, String msg) {
        setInfo(context, msg);
    }

    private void setInfo(Activity context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    public void showSingleButtonDialog() {
        createDialog(context);
        configSingleButtonDialog();
        showDialog();
    }

    public void showTwoButtonDialog() {
        createDialog(context);
        configTwoButtonDialog();
        showDialog();
    }

    private void createDialog(Activity activity) {
        if (clearDialog == null) {
            clearDialog = new ZhaoBiaoDialog(activity, "");
        }
    }

    private void configSingleButtonDialog() {
        clearDialog.setCancelButtonGone();
        clearDialog.setCancelable(false);

        clearDialog.setOnDismissListener(new Dismiss());
        clearDialog.setOnDialogClickListener(new DialogClickListener());
        clearDialog.setMessage(msg);
    }

    private void configTwoButtonDialog() {
        clearDialog.setOnDismissListener(new Dismiss());
        clearDialog.setOnDialogClickListener(new DialogClickListener());
        clearDialog.setMessage(msg);
        clearDialog.setNagativeText("取消");
        clearDialog.setPositiveText("确定");
    }

    private void showDialog() {
        if (clearDialog != null)
            clearDialog.show();
    }

    private void dismiss() {
        if (clearDialog != null) {
            clearDialog.dismiss();
        }
    }

    private class Dismiss implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface dialog) {
            clearDialog = null;
        }
    }

    private class DialogClickListener implements ZhaoBiaoDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            clearListener.clear();
            dismiss();
        }

        @Override
        public void onDialogCancelClick() {
            dismiss();
        }
    }

    public interface ClearListener {
        void clear();
    }

    public void setClearListener(ClearListener clearListener) {
        this.clearListener = clearListener;
    }
}
