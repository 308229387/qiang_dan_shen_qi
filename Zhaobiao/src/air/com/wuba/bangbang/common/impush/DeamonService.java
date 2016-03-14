package air.com.wuba.bangbang.common.impush;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.huangyezhaobiao.application.BiddingApplication;
import com.xiaomi.mipush.sdk.MiPushClient;



/**
 * Created by 58 on 2015/10/10.
 */


public class DeamonService extends Service{
    static {
        System.loadLibrary("bangbangdeamon");
    }
    private static final String TAG = "DeamonService";
    public native void initVoldProcess(String filepath);
    public native void makeKeyFile(String filepath);


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("szxhahaha", "startService");
       /* String cmdfilepath = getApplicationContext().getFilesDir().getPath() + "/cmdBidding";
        makeKeyFile(cmdfilepath);
        MiPushClient.registerPush(getApplicationContext(), BiddingApplication.APP_ID, BiddingApplication.APP_KEY);
        initVoldProcess(cmdfilepath);*/
        String cmdfilepath = getApplicationContext().getFilesDir().getPath() + "/cmdBidding";
        makeKeyFile(cmdfilepath);
        MiPushClient.registerPush(getApplicationContext(), BiddingApplication.APP_ID, BiddingApplication.APP_KEY);
        initVoldProcess(cmdfilepath);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("szxhahaha", "onStartCommand");
//        String cmdfilepath = getApplicationContext().getFilesDir().getPath() + "/cmd";
//        makeKeyFile(cmdfilepath);
//        MiPushClient.registerPush(getApplicationContext(), Config.XIAOMI_APP_ID, Config.XIAOMI_APP_KEY);
//        initVoldProcess(cmdfilepath);

          super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
