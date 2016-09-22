package wuba.zhaobiao.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
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

    private long settlementDialog_Show_time,settlementDialog_dismiss_time ;

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
        settlementDialog_Show_time = System.currentTimeMillis();
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
            settlementDialog_dismiss_time = System.currentTimeMillis();
            HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_SETTLE_DIALOG, settlementDialog_dismiss_time - settlementDialog_Show_time);
        }
    }

    private class DialogClickListener implements BusinessSettlementDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            dismiss();
            settlement.settlementCheck();
            HYMob.getDataList(context, HYEventConstans.EVENT_SETTLE_DIALOG_CONFIRM);
        }

        @Override
        public void onDialogCancelClick() {
            dismiss();
            HYMob.getDataList(context, HYEventConstans.EVENT_SETTLE_DIALOG_CANCEL);
        }
    }

    public interface SettlementListener {
        void settlementCheck();
    }

    public void setSettlementListener(SettlementListener settlement) {
        this.settlement = settlement;
    }


}
