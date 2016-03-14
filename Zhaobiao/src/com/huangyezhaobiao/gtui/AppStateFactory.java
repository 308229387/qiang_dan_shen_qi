package com.huangyezhaobiao.gtui;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.huangyezhaobiao.deal.IDealWithBean;
import com.huangyezhaobiao.deal.InAppDealWithBean;
import com.huangyezhaobiao.deal.LockDealWithBean;
import com.huangyezhaobiao.deal.OutAppDealWithBean;
import com.huangyezhaobiao.deal.PhoneCallingWithBean;
import com.huangyezhaobiao.utils.KeyguardUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.Utils;

/**
 * Created by 58 on 2016/1/7.
 * 工厂方法,根据不同的状态返回不同的deal的实例
 */
public class AppStateFactory {
    public static IDealWithBean getDealFromState(Context context){

        if(!isOpenWindow(context)){//通话中，不弹窗
            Log.e("shenzhixin","return phonecallingWithBean");
            return new PhoneCallingWithBean();
        }
        if(context!=null && !Utils.isForeground(context)){//后台
            return new OutAppDealWithBean();
        }
        if(context != null && KeyguardUtils.isLockScreen(context) && !KeyguardUtils.notLock && StateUtils.state == 1 && !KeyguardUtils.onLock){//锁屏,亮屏界面
            return new LockDealWithBean();
        }
        return new InAppDealWithBean();
    }

    private static boolean isOpenWindow(Context context) {
        //获得相应的系统服务
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (tm.getCallState()) {
            case TelephonyManager.CALL_STATE_IDLE://空闲状态
                return true;
            case TelephonyManager.CALL_STATE_RINGING://响铃状态
                return false;
            case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                return false;
        }
        return true;
    }
}
