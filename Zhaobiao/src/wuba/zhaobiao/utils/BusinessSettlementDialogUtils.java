package wuba.zhaobiao.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.huangyezhaobiao.view.BusinessSettlementDialog;

/**
 * Created by SongYongmeng on 2016/7/31.
 * 描    述：提示框模板，如果需要提示框直接复制这个就行，但不要改这个
 */
public class BusinessSettlementDialogUtils {

    private Activity context;
    private BusinessSettlementDialog settlementDialog;
    private String msg;
    private SettlementListener settlement;

    public BusinessSettlementDialogUtils(Activity context, String msg) {
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

    public void showTwoButtonDialog() { //弹框显示两个按钮--取消、确定
        createDialog(context);
        configTwoButtonDialog();
        showDialog();
    }

    private void createDialog(Activity activity) {
        if (settlementDialog == null)
            settlementDialog = new BusinessSettlementDialog(activity, "");
    }

    private void configSingleButtonDialog() {
        settlementDialog.setCancelButtonGone();
        settlementDialog.setCancelable(false);
        settlementDialog.setOnDismissListener(new Dismiss());
        settlementDialog.setOnDialogClickListener(new DialogClickListener());
        settlementDialog.setMessage(msg);
    }

    private void configTwoButtonDialog() {
        settlementDialog.setOnDismissListener(new Dismiss());
        settlementDialog.setOnDialogClickListener(new DialogClickListener());
        settlementDialog.setMessage(msg);
    }

    private void showDialog() {
        if (settlementDialog != null)
            settlementDialog.show();
    }

    private void dismiss() {
        if (settlementDialog != null)
            settlementDialog.dismiss();
    }

    public boolean getCheckState() {
        boolean temp = settlementDialog.getCheckBoxState();
        return temp;
    }

    private class Dismiss implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface dialog) {
            settlementDialog = null;
        }
    }

    private class DialogClickListener implements BusinessSettlementDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            dismiss();
            settlement.settlementCheck();
        }

        @Override
        public void onDialogCancelClick() {
            dismiss();
        }
    }

    public interface SettlementListener {
        void settlementCheck();
    }

    public void setSettlementListener(SettlementListener settlement) {
        this.settlement = settlement;
    }


}
