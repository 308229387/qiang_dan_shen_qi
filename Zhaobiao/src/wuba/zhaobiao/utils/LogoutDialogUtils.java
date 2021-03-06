package wuba.zhaobiao.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.CommonBaseActivity;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.activity.LoginActivity;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.order.model.OrderModel;

/**
 * Created by SongYongmeng on 2016/7/31.
 * 描    述：首页Activity里PPU过期和长时间未登陆时弹出的退出Dialog
 */
public class LogoutDialogUtils {

    private Activity context;
    private ZhaoBiaoDialog LogoutDialog;
    private String msg;

    public LogoutDialogUtils(Activity context, String msg) {
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
        if(LogoutDialog == null ){
            LogoutDialog = new ZhaoBiaoDialog(activity, "");
        }
    }

    private void configSingleButtonDialog() {
        LogoutDialog.setCancelButtonGone();
        LogoutDialog.setCancelable(false);
        LogoutDialog.setOnDismissListener(new Dismiss());
        LogoutDialog.setOnDialogClickListener(new DialogClickListener());
        LogoutDialog.setMessage(msg);
    }

    private void configTwoButtonDialog() {
        LogoutDialog.setOnDismissListener(new Dismiss());
        LogoutDialog.setOnDialogClickListener(new DialogClickListener());
        LogoutDialog.setMessage(msg);
    }

    private void showDialog() {
        if(LogoutDialog != null){
            LogoutDialog.show();
        }

    }

    private void clearInfoAndService() {
        new LogoutClearInfoAndStopServiceUtils(context).clear();
    }

    private void dismiss() {
        if(LogoutDialog != null) {
            LogoutDialog.dismiss();
        }
    }


    private void goToLogin() {
        ActivityUtils.goToActivity(context, LoginActivity.class);
    }

    private void logout(){
        OkHttpUtils.get(Urls.LOGOUT)//
                .execute(new logoutCallback(context,true));
    }

    private  void  clearOrderCheckList(){
        OrderModel.CategoryCheckedId.clear();
        OrderModel.stateCheckedId.clear();
        OrderModel.isBidding = true;
        OrderModel.orderState ="0";
    }


    private class DialogClickListener implements ZhaoBiaoDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            dismiss();
            clearOrderCheckList();
            logout();
            clearInfoAndService();
            goToLogin();
        }

        @Override
        public void onDialogCancelClick() {
            dismiss();
        }
    }

    private class Dismiss implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface dialog) {
            LogoutDialog = null;
        }
    }


    private class logoutCallback extends DialogCallback<String>{

        public logoutCallback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            LogUtils.LogV("logout", "logout_success");
        }

    }

}
