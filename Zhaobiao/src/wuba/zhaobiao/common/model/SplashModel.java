package wuba.zhaobiao.common.model;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import com.huangyezhaobiao.activity.GuideActivity;
import com.huangyezhaobiao.activity.MainActivity;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.CommonUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.VersionUtils;
import com.wuba.loginsdk.external.LoginClient;

import wuba.zhaobiao.common.activity.LoginActivity;
import wuba.zhaobiao.common.activity.SplashActivity;

/**
 * Created by SongYongmeng on 2016/7/28.
 */
public class SplashModel extends BaseModel {
    private SplashActivity context;
    private static final long DELAYED_TIMES = 3 * 1000;
    private SharedPreferences sp;
    private Handler handler = new Handler();

    public SplashModel(SplashActivity context) {
        this.context = context;
    }

    public void registPush() {
        GePushProxy.initliazePush(context.getApplicationContext());
    }

    public void setHeardColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        context.getWindow().setBackgroundDrawable(null);
    }

    public void waitTimeAfterGoToWhere() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToWhere();
            }
        }, DELAYED_TIMES);
    }

    private void goToWhere() {
        String currentVersionName = getVersion();
        if (isFirstCome(currentVersionName)) {
            ActivityUtils.goToActivity(context, GuideActivity.class);
        } else if (neverCome()) {
            ActivityUtils.goToActivity(context, LoginActivity.class);
        } else {
            ActivityUtils.goToActivity(context, MainActivity.class);
        }
        sp.edit().putString(Constans.VERSION_NAME, currentVersionName).commit();
    }

    private String getVersion() {
        sp = context.getSharedPreferences(Constans.APP_SP, 0);
        String currentVersionName = "1.0.0";
        try {
            currentVersionName = VersionUtils.getVersionName(context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionName;
    }

    private Boolean isFirstCome(String currentVersionName) {
        String mCurrentVersionName = currentVersionName.replace(".", "");
        String saveVersionName = sp.getString(Constans.VERSION_NAME,
                "1.0.0");
        Boolean isFirst = CommonUtils.compareTwoNumbersGuide(saveVersionName,
                mCurrentVersionName);
        return !isFirst;
    }

    private boolean neverCome() {
        return TextUtils.isEmpty(UserUtils.getUserId(context))
                || !UserUtils.isValidate(context)
                || TextUtils.isEmpty(LoginClient.doGetPPUOperate(BiddingApplication.getAppInstanceContext()));
    }

    public void baiduStatisticsStart() {
        BDMob.getBdMobInstance().onResumeActivity(BiddingApplication.getBiddingApplication());
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_APP_OPEND);
    }

    public void baiduStatisticsPause() {
        BDMob.getBdMobInstance().onPauseActivity(context);
    }
}
