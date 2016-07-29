package wuba.zhaobiao.config;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huangyezhaobiao.activity.LockActivity;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.utils.KeyguardUtils;

import wuba.zhaobiao.common.activity.HomePageActivity;

/**
 * Created by SongYongmeng on 2016/7/29.
 */
public class ScreenReceiver extends BroadcastReceiver {
    private HomePageActivity activity;
    private KeyguardManager keyguardManager;
    private KeyguardManager.KeyguardLock keyguardLock;


    public ScreenReceiver(HomePageActivity activity) {
        this.activity = activity;
        init(activity);
    }

    private void init(HomePageActivity activity) {
        Application lock = activity.getApplication();
        String key = activity.KEYGUARD_SERVICE;
        keyguardManager = (KeyguardManager) lock.getSystemService(key);
        keyguardLock = keyguardManager.newKeyguardLock("");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) return;
        if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
            KeyguardUtils.SCREEN_ON = true;
            KeyguardUtils.need_lock = false;

        } else {
            KeyguardUtils.need_lock = true;
            KeyguardUtils.SCREEN_ON = false;
            KeyguardUtils.notLock = false;
            openKeyguard();
            closeKeyguard();
            BiddingApplication biddingApplication = (BiddingApplication) activity.getApplication();
            if (biddingApplication.activity instanceof LockActivity) {
                LockActivity lockActivity = (LockActivity) biddingApplication.activity;
                if (lockActivity != null) {
                    lockActivity.closeLock();
                }
            }
        }
    }

    private void openKeyguard() {
        keyguardLock.disableKeyguard();
    }

    private void closeKeyguard() {
        keyguardLock.reenableKeyguard();
    }
}
