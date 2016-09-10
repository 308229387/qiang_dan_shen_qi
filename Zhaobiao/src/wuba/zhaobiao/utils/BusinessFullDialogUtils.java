package wuba.zhaobiao.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.huangyezhaobiao.view.ZhaoBiaoDialog;

/**
 * Created by SongYongmeng on 2016/7/31.
 * 描    述：选中商品过多时的弹窗提示
 */
public class BusinessFullDialogUtils {

    private Activity context;
    private ZhaoBiaoDialog refreshDialog;
    private String msg;

    public BusinessFullDialogUtils(Activity context, String msg) {
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
        if(refreshDialog == null ){
            refreshDialog = new ZhaoBiaoDialog(activity, "");
        }
    }

    private void configSingleButtonDialog() {
        refreshDialog.setCancelButtonGone();
        refreshDialog.setCancelable(false);
        refreshDialog.setOnDismissListener(new Dismiss());
        refreshDialog.setOnDialogClickListener(new DialogClickListener());
        refreshDialog.setMessage(msg);
    }

    private void configTwoButtonDialog() {
        refreshDialog.setOnDismissListener(new Dismiss());
        refreshDialog.setOnDialogClickListener(new DialogClickListener());
        refreshDialog.setMessage(msg);
    }

    private void showDialog() {
        if(refreshDialog != null)
            refreshDialog.show();
    }

    private void dismiss() {
        if(refreshDialog != null) {
            refreshDialog.dismiss();
        }
    }

    private class Dismiss implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface dialog) {
            refreshDialog = null;
        }
    }

    private class DialogClickListener implements ZhaoBiaoDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            dismiss();
        }

        @Override
        public void onDialogCancelClick() {
            dismiss();
        }
    }


}
