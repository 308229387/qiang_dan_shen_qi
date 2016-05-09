package com.huangyezhaobiao.log;

import android.app.Activity;
import android.app.Service;
import android.content.Context;

import com.huangye.commonlib.file.SqlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * com.huangyezhaobiao.log
 * Created by shenzhixin on 2016/5/9 14:07.
 * log的中转站，在这里面实现了
 */
public class LogInvocation{
    private static final int DEFAULT_LOG_COUNTS = 60;
    private static int logCount = 0;
    private static List<ILogExecutor> executorListener = new ArrayList<>();

    /**
     * 初始化logCount
     */
    public static void initDatas(Context context){
        //取出logCount的值
        List<LogBean> logBeen = readAllLogs(context);
        logCount = logBeen.size();
        notifyAllListeners(context);
    }

    /**
     * 注册
     * @param logExecutor
     */
    public static void registerExecutorListener(ILogExecutor logExecutor){
        executorListener.add(logExecutor);
    }


    public static void destroy(){
        executorListener.clear();
        logCount = 0;
    }



    public static void saveLog(Context context,LogBean logBean){
        checkContext(context);
        SqlUtils.getInstance(context).save(logBean,null);
        //logCount+1
        logCount++;
        //存入logCount
        notifyAllListeners(context);
    }

    private static void checkContext(Context context) {
        if(context instanceof Activity || context instanceof Service){
            throw new RuntimeException("要传入ApplicationContext!!!");
        }
    }


    /**
     * 读取所有的信息
     */
    private static List<LogBean> readAllLogs(Context context){
        checkContext(context);
        return SqlUtils.getInstance(context).getListPage(LogBean.class,"",1000,0,null);
    }


    /**
     * 唤醒所有用户
     */
    private static void notifyAllListeners(Context context){
        if(logCount>=DEFAULT_LOG_COUNTS) {
            int size = executorListener.size();
            for (int index = 0; index < size; index++) {
                ILogExecutor iLogExecutor = executorListener.get(index);
                if (iLogExecutor != null) {
                    iLogExecutor.upload(readAllLogs(context));
                }
            }
        }
    }



}
