package wuba.zhaobiao.common.model;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.MainActivity;
import com.huangyezhaobiao.activity.MobileValidateActivity;
import com.huangyezhaobiao.activity.SoftwareUsageActivity;
import com.huangyezhaobiao.callback.JsonCallback;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.PhoneUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.lzy.okhttputils.OkHttpUtils;
import com.wuba.loginsdk.external.LoginCallback;
import com.wuba.loginsdk.external.LoginClient;
import com.wuba.loginsdk.external.Request;
import com.wuba.loginsdk.external.SimpleLoginCallback;
import com.wuba.loginsdk.model.LoginSDKBean;
import com.wuba.loginsdk.utils.ToastUtils;

import okhttp3.Call;
import okhttp3.Response;
import wuba.zhaobiao.common.activity.LoginActivity;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.respons.LoginRespons;

/**
 * Created by SongYongmeng on 2016/7/28.
 */
public class LoginModel extends BaseModel {
    private LoginActivity context;
    private ZhaoBiaoDialog alertDialog;
    private String NOT_VALIDATED = "1";

    public LoginModel(LoginActivity context) {
        this.context = context;
    }

    public void registLoginSDK() {
        LoginClient.register(mLoginCallback);
    }

    private LoginCallback mLoginCallback = new loginCallBack();

    private void loginSuccess(String msg) {
        ToastUtils.showToast(context, msg);
        landed();
    }

    private void landed() {
        OkHttpUtils.post(Urls.LOGIN)
                .params("deviceId", PhoneUtils.getIMEI(context))
                .params("token", PhoneUtils.getIMEI(context))
                .execute(new localLoginRespons());
    }

    private boolean isLoginFail(@Nullable LoginSDKBean loginSDKBean) {
        return loginSDKBean != null && loginSDKBean.getCode() == LoginSDKBean.CODE_CANCEL_OPERATION;
    }

    public void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing() && !context.isFinishing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public void getBaiduStatisticsInfo() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_LOGIN, context.stop_time - context.resume_time);
    }


    public void unregisterLoginSDK() {
        LoginClient.unregister(mLoginCallback);
    }

    public void stopService() {
        context.stopService(new Intent(context, MyService.class));
    }

    private class loginCallBack extends SimpleLoginCallback {
        @Override
        public void onLogin58Finished(boolean isSuccess, String msg, @Nullable LoginSDKBean loginSDKBean) {
            super.onLogin58Finished(isSuccess, msg, loginSDKBean);
            if (isSuccess && loginSDKBean != null) {
                loginSuccess(msg);
            }

            if (isLoginFail(loginSDKBean)) {
                context.finish();
            }
        }
    }

    private void initDailog(String msg) {
        if (alertDialog == null) {
            alertDialog = new ZhaoBiaoDialog(context, "");
            alertDialog.setCancelButtonGone();
            alertDialog.setOnDialogClickListener(new grabDialog());
            alertDialog.setMessage(msg);
            alertDialog.show();
        }
    }

    public void configLandedParams() {
        Request request = new Request.Builder()
                .setOperate(Request.LOGIN)
                .setLogoResId(R.drawable.newlogin_logo)
                .setLoginProtocolActivity(SoftwareUsageActivity.class)//控制是否显示协议，协议页面是一个activity
                .setForgetPwdEnable(true)//是否显示忘记密码
                .setPhoneLoginEnable(false)//是否显示手机动态码登录入口
                .setRegistEnable(false)//是否显示注册入口
                .setSocialEntranceEnable(false)//设置页面是否显示三方登录
                .setCloseButtonEnable(false)//设置页面是否带关闭按钮
                .create();
        LoginClient.launch(context, request);
    }

    private void passpordLoginSuccess(LoginRespons loginRespons) {
        saveInfoAndStatistics(loginRespons);
        doValidatedPhoneNum(loginRespons);
        context.finish();
    }

    private void saveInfoAndStatistics(LoginRespons loginRespons) {
        String userName = LoginClient.doGetUnameOperate(context);
        String compnyName = loginRespons.getData().getCompanyName();
        String userId = loginRespons.getData().getUserId();
        UserUtils.saveUser(context, userId, compnyName, userName);
        UserUtils.setSessionTime(context, System.currentTimeMillis()); //存储登录成功时间
        UserUtils.saveNeedUpdate(context, false); //存储不强制更新的flag
        HYMob.getDataListByLoginSuccess(context, HYEventConstans.EVENT_ID_LOGIN, "1", userName);
        GePushProxy.bindPushAlias(context.getApplicationContext(), userId + "_" + PhoneUtils.getIMEI(context));
    }

    private void doValidatedPhoneNum(LoginRespons loginRespons) {
        String hasValidated = loginRespons.getData().getHasValidated();
        if (hasValidated.equals(NOT_VALIDATED)) {    //判断是否验证过手机 1没有验证过，0验证过
            ActivityUtils.goToActivity(context, MobileValidateActivity.class);
        } else {
            UserUtils.hasValidate(context.getApplicationContext());
            ActivityUtils.goToActivity(context, MainActivity.class);
        }
    }


    private class localLoginRespons extends JsonCallback<LoginRespons> {

        @Override
        public void onResponse(boolean isFromCache, LoginRespons loginRespons, okhttp3.Request request, @Nullable Response response) {
            passpordLoginSuccess(loginRespons);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            initDailog(e.getMessage());
        }
    }

    private class grabDialog implements ZhaoBiaoDialog.onDialogClickListener {
        @Override
        public void onDialogOkClick() {
            if (alertDialog != null && alertDialog.isShowing() && !context.isFinishing()) {
                alertDialog.dismiss();
                context.finish();
            }
        }

        @Override
        public void onDialogCancelClick() {
        }
    }

}
