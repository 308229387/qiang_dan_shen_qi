package com.huangyezhaobiao.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by 58 on 2016/5/16.
 */
public class LoggerUtils {

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Context context)
    {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取渠道号
     * @param context
     * @return
     */
    public static String getChannelId(Context context){
        String channel= "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
             channel=appInfo.metaData.getString("BaiduMobAd_CHANNEL");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
         return channel == null ? "-" : channel;
    }


    /**
     * 获取客户端版本
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context){
        String versionName= "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            versionName =packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName == null ? "-" : versionName;

    }

    /**
     * 获取屏幕分辨率
     * @param context
     * @return
     */
    public static String getScreenPixels(Context context){
        String screenPixels ="";
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        screenPixels = dm.widthPixels + "*" +dm.heightPixels;
       return screenPixels == null ? "-" : screenPixels;
    }

    /**
     * 获取设备号
     * @param context
     * @return
     */
    public static String getIMEI(Context context){
        String IMEINumber = "";
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        IMEINumber = TelephonyMgr.getDeviceId();
        return IMEINumber == null ? "-" :IMEINumber;
    }

    /**
     * 获取手机号
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context){
        String phoneNumber = "";
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        phoneNumber = TelephonyMgr.getLine1Number();
        return phoneNumber == null ? "-": phoneNumber;
    }

    /**
     * 获取手机型号
     * @return
     */
    public static String getPhoneType(){
        String phoneType= "";
        phoneType = android.os.Build.MODEL;
        return  phoneType == null ? "-": phoneType;
    }

    /**
     * 获取手机品牌
     * @return
     */
    public static String getPhoneBrand(){
        String phoneBrand ="";
        phoneBrand=android.os.Build.BRAND;
        return phoneBrand == null ? "-": phoneBrand;
    }

    /**
     * 获取手机MAC地址
     * 只有手机开启wifi才能获取到mac地址
     */
    public static String getMacAddress(Context context){
        String result = "";
        if(isNetworkAvailable(context)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            result = wifiInfo.getMacAddress();
            return result == null ? "-": result;
        }
        return "-";
    }

    /**
     * 获取手机网络类型
     * @return
     */
    public static String getNetworkType(Context context)
    {
        String strNetworkType = "";
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = "WIFI";
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                        {
                            strNetworkType = "3G";
                        }
                        else
                        {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

            }
        }


        return strNetworkType == null? "-":strNetworkType;
    }

}
