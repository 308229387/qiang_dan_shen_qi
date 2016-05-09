package com.huangyezhaobiao.log;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * com.huangyezhaobiao.log
 * Created by shenzhixin on 2016/5/9 14:09.
 * Log上传，存储，取出的真正实现者
 */
public class LogExecutor implements ILogExecutor{
    @Override
    public void upload(List<LogBean> beans,final Context context) {
        Log.e("shenzhixin","upload");
        LogInvocation.startAgain(context);
        if(beans!=null) {
            Log.e("shenzhixin", "haha size:" + beans.size());

            //上传成功后,回调中如果成功就调用LogInvacator.startAgain();
        }
    }


}
