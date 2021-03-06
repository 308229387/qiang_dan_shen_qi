package wuba.zhaobiao.common.model;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

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

import wuba.zhaobiao.common.activity.HomePageActivity;
import wuba.zhaobiao.common.activity.LoginActivity;
import wuba.zhaobiao.common.activity.SplashActivity;

/**
 * Created by SongYongmeng on 2016/7/28.
 */
public class SplashModel extends BaseModel {

    private SplashActivity context;
    private SharedPreferences sp;
    private static final long DELAYED_TIMES = 3 * 1000;
    private Handler handler = new Handler();

    public SplashModel(SplashActivity context) {
        this.context = context;
    }

    public void setTobBarColor() {
        setTopColor();
        setTopBackground();
    }

    private void setTopColor() {
        if (judgCanSupportChangeTopColor())
            setTopColorParams();
    }

    private boolean judgCanSupportChangeTopColor() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    private void setTopColorParams() {
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void setTopBackground() {
        context.getWindow().setBackgroundDrawable(null);
    }

    public void registPush() {
        GePushProxy.initliazePush(context.getApplicationContext());
    }

    public void getSP() {
        sp = context.getSharedPreferences(Constans.APP_SP, 0);
    }

    public void waitAfterSaveVerSionAndGo() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToWhere();
            }
        }, DELAYED_TIMES);
    }

    private void goToWhere() {
        String currentVersionName = getVersionName();
        judgeHowManyComeAfterGo(currentVersionName);
        saveVersionName(currentVersionName);
        finish();
    }

    private String getVersionName() {
        String currentVersionName = getCurrentVersionName();
        return currentVersionName;
    }

    private String getCurrentVersionName() {
        String currentVersionName = "1.0.0";
        try {
            currentVersionName = VersionUtils.getVersionName(context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionName;
    }

    private void judgeHowManyComeAfterGo(String currentVersionName) {
//        if (isFirstCome(currentVersionName)) {
//            ActivityUtils.goToActivity(context, GuideActivity.class);
//        } else
        if (isFirstCome(currentVersionName)||neverCome() ||isUpdate()) {
            ActivityUtils.goToActivity(context, LoginActivity.class);
        } else {
            ActivityUtils.goToActivity(context, HomePageActivity.class);
        }
    }

    private Boolean isFirstCome(String currentVersionName) {
        String mCurrentVersionName = currentVersionName.replace(".", "");
        String saveVersionName = sp.getString(Constans.VERSION_NAME,
                "1.0.0");
        Boolean isFirst = CommonUtils.compareTwoNumbersGuide(saveVersionName,
                mCurrentVersionName);
        return !isFirst;
    }

    private Boolean isUpdate() {
        int currentVersion = -1;
        try {
            currentVersion = Integer.parseInt(VersionUtils.getVersionCode(context));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersion >= 23;
    }

    private boolean neverCome() {
        return TextUtils.isEmpty(UserUtils.getUserId(context))
                || !UserUtils.isValidate(context)
                || TextUtils.isEmpty(LoginClient.doGetPPUOperate(BiddingApplication.getAppInstanceContext()))
                || TextUtils.isEmpty(UserUtils.getAppVersion(context));
    }

    private void saveVersionName(String currentVersionName) {
        sp.edit().putString(Constans.VERSION_NAME, currentVersionName).commit();
    }

    private void finish() {
        context.finish();
    }

    public void baiduStatisticsPause() {
        BDMob.getBdMobInstance().onPauseActivity(context);
    }

    public void baiduStatisticsStart() {
        BDMob.getBdMobInstance().onResumeActivity(BiddingApplication.getBiddingApplication());
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_APP_OPEND);
    }

}
