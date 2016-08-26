package wuba.zhaobiao.common.model;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.callback.JsonCallback;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.PhoneUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.BaseRequest;
import com.wuba.loginsdk.external.LoginCallback;
import com.wuba.loginsdk.external.LoginClient;
import com.wuba.loginsdk.external.Request;
import com.wuba.loginsdk.external.SimpleLoginCallback;
import com.wuba.loginsdk.model.LoginSDKBean;

import okhttp3.Call;
import okhttp3.Response;
import wuba.zhaobiao.common.activity.HomePageActivity;
import wuba.zhaobiao.common.activity.LoginActivity;
import wuba.zhaobiao.common.activity.MobileValidateActivity;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.mine.activity.SoftwareProtocolActivity;
import wuba.zhaobiao.respons.LoginRespons;

/**
 * Created by SongYongmeng on 2016/7/28.
 */
public class LoginModel extends BaseModel {
    private LoginActivity context;
    private ZhaoBiaoDialog alertDialog;
    private LoginCallback mLoginCallback;

    private String NOT_VALIDATED = "1";

    public LoginModel(LoginActivity context) {
        this.context = context;
    }

    public void removeOtherActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BiddingApplication.getInstance().removeOtherActivity(context);
            }
        }, 1500);
    }

    public void createLoginCallback() {
        mLoginCallback = new loginCallBack();
    }

    public void setLoginSDKAndSaveInfo() {
        LoginClient.register(mLoginCallback);
    }

    private void loginSuccess(String msg) {
        landed();
    }

    private void landed() {
        OkHttpUtils.post(Urls.LOGIN)
                .params("deviceId", PhoneUtils.getIMEI(context))
                .params("token", PhoneUtils.getIMEI(context))
                .params("phone",LoginClient.doGetUserPhoneOperate(context))
                .execute(new localLoginRespons(context,false));
    }

    private void passportLoginSuccess(LoginRespons loginRespons) {
        saveInfoAndStatistics(loginRespons);
        validatedPhoneNumAfterGoToWhere(loginRespons);
        context.finish();
    }

    private void saveInfoAndStatistics(LoginRespons loginRespons) {
        String userName = LoginClient.doGetUnameOperate(context);
        String companyName = loginRespons.getData().getCompanyName();
        String userId = loginRespons.getData().getUserId();
        String suserId = loginRespons.getData().getSuserId();
        String isSon = loginRespons.getData().getIsSon();
        String rbac = loginRespons.getData().getRbac();
        UserUtils.saveAcocuntInfo(context, isSon, rbac);
        saveInfo(userName, companyName,userId,suserId);
    }

    private void saveInfo(String userName, String compnyName, String userId,String suserId) {
        saveLocalInfo();
        saveStatistics(userName, userId,suserId);
        UserUtils.saveUser(context, userId, compnyName, userName);
    }

    private void validatedPhoneNumAfterGoToWhere(LoginRespons loginRespons) {
        if (hasValidated(loginRespons)) {
            ActivityUtils.goToActivity(context, MobileValidateActivity.class);
        } else {
            UserUtils.hasValidate(context.getApplicationContext());
            ActivityUtils.goToActivity(context, HomePageActivity.class);
        }
    }

    //1:NOT_VALIDATED    0:HAS_VALIDATED
    private Boolean hasValidated(LoginRespons loginRespons) {
        Boolean check = loginRespons.getData().getHasValidated().equals(NOT_VALIDATED);
        return check;
    }

    private void initPassportErrorDialog(String msg) {
        if (alertDialog == null) {
            createAndConfigErrorDialog(msg);
        }
    }

    public void getBaiduStatisticsInfo() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_LOGIN, context.stop_time - context.resume_time);
    }

    public void unregisterLoginSDK() {
        LoginClient.unregister(mLoginCallback);
    }

    public void configLandedParams() {
        Request request = new Request.Builder()
                .setOperate(Request.LOGIN)
                .setLogoResId(R.drawable.newlogin_logo)
                .setLoginProtocolActivity(SoftwareProtocolActivity.class)//控制是否显示协议，协议页面是一个activity
                .setForgetPwdEnable(true)//是否显示忘记密码
                .setPhoneLoginEnable(true)//是否显示手机动态码登录入口
                .setRegistEnable(false)//是否显示注册入口
                .setSocialEntranceEnable(false)//设置页面是否显示三方登录
                .setCloseButtonEnable(false)//设置页面是否带关闭按钮
                .create();
        LoginClient.launch(context, request);
    }

    private void saveLocalInfo() {
        saveLandedTime();
        setNotForciblyUpdateFlag();
    }

    private void saveLandedTime() {
        UserUtils.setSessionTime(context, System.currentTimeMillis());
    }

    private void setNotForciblyUpdateFlag() {
        UserUtils.saveNeedUpdate(context, false);
    }

    private void saveStatistics(String userName, String userId,String suserId) {
        HYMob.getDataListByLoginSuccess(context, HYEventConstans.EVENT_ID_LOGIN, "1", userName);
        String isSon = UserUtils.getIsSon(context);
        if(!TextUtils.isEmpty(isSon) && TextUtils.equals("1",isSon)){
            GePushProxy.bindPushAlias(context.getApplicationContext(), suserId + "_" + PhoneUtils.getIMEI(context));
        }else{
            GePushProxy.bindPushAlias(context.getApplicationContext(), userId + "_" + PhoneUtils.getIMEI(context));
        }

    }

    private void createAndConfigErrorDialog(String msg) {
        createErrorDialog();
        setErrorDialogInfo(msg);
        errorDialogShow();
    }

    private void createErrorDialog() {
        alertDialog = new ZhaoBiaoDialog(context, "");
    }

    private void setErrorDialogInfo(String msg) {
        alertDialog.setCancelButtonGone();
        alertDialog.setOnDialogClickListener(new grabDialog());
        alertDialog.setMessage(msg);
    }

    private void errorDialogShow() {
        alertDialog.show();
    }

    private boolean isPassportLoginFail(@Nullable LoginSDKBean loginSDKBean) {
        return loginSDKBean != null && loginSDKBean.getCode() == LoginSDKBean.CODE_CANCEL_OPERATION;
    }

    private class loginCallBack extends SimpleLoginCallback {
        @Override
        public void onLogin58Finished(boolean isSuccess, String msg, @Nullable LoginSDKBean loginSDKBean) {
            super.onLogin58Finished(isSuccess, msg, loginSDKBean);
            if (isSuccess && loginSDKBean != null) {
                loginSuccess(msg);
            }

            if (isPassportLoginFail(loginSDKBean)) {
                context.finish();
            }
        }
    }

    private class localLoginRespons extends DialogCallback<LoginRespons> {

        public localLoginRespons(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onBefore(BaseRequest request) {

            request.headers("ppu", LoginClient.doGetPPUOperate(BiddingApplication.getAppInstanceContext()))//
                    .headers("userId", LoginClient.doGetUserIDOperate(BiddingApplication.getAppInstanceContext()))//
                    .headers("version", "6")//
                    .headers("platform", "1")//
                    .headers("UUID", com.huangye.commonlib.utils.PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()));
        }

        @Override
        public void onResponse(boolean isFromCache, LoginRespons loginRespons, okhttp3.Request request, @Nullable Response response) {
            passportLoginSuccess(loginRespons);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            if (e != null && e.getMessage().equals(NEED_DOWN_LINE)) {
                initPassportErrorDialog (context.getString(R.string.force_exit));
            } else if (e != null && e.getMessage().equals(CHILD_FUNCTION_BAN)) {
                initPassportErrorDialog(context.getString(R.string.child_function_ban));
            } else if (e != null && e.getMessage().equals(CHILD_HAS_UNBIND)) {
                initPassportErrorDialog(context.getString(R.string.child_has_unbind));
            } else if (e != null && e.getMessage().equals(PPU_EXPIRED)) {
                initPassportErrorDialog (context.getString(R.string.ppu_expired));
            }

            if( e != null)
            initPassportErrorDialog(e.getMessage());
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
