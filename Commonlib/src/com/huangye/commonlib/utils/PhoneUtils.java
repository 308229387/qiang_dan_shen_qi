package com.huangye.commonlib.utils;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by shenzhixin on 2015/12/16.
 */
public class PhoneUtils {
    /**
     * 获得手机的imei号
     * @param context
     * @return
     */
    public static String getIMEI(Context context){
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        return imei;
    }


}
