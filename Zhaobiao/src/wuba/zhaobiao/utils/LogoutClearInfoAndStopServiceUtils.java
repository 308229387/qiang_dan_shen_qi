package wuba.zhaobiao.utils;

import android.app.Activity;
import android.content.Intent;

import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.service.MyService;
import com.huangyezhaobiao.utils.UserUtils;
import com.wuba.loginsdk.external.LoginClient;

/**
 * Created by SongYongmeng on 2016/8/1.
 */
public class LogoutClearInfoAndStopServiceUtils {
    private final Activity context;

    public LogoutClearInfoAndStopServiceUtils(Activity context) {
        this.context = context;
    }

    public void clear() {
        clearInfo();
        unRigistGePush();
        setLogoutStateToPasspord();
        stopService();
    }

    private void clearInfo() {
        SharedPreferencesUtils.clearLoginToken(context.getApplicationContext());
        UserUtils.clearUserInfo(context.getApplicationContext());
    }

    private void unRigistGePush() { //退出时注销个推
        GePushProxy.unBindPushAlias(context.getApplicationContext(), UserUtils.getUserId(context.getApplicationContext()));
    }

    private void setLogoutStateToPasspord() {
        LoginClient.doLogoutOperate(context);
    }

    private void stopService() {
        context.stopService(new Intent(context, MyService.class));
    }
}
