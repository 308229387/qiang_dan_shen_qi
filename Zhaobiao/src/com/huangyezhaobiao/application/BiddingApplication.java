package com.huangyezhaobiao.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.huangye.commonlib.file.SqlUtils;
import com.huangye.commonlib.sql.SqlUpgradeCallback;
import com.huangye.commonlib.utils.UserConstans;
import com.huangyezhaobiao.bean.AppBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.log.LogHandler;
import com.huangyezhaobiao.notification.GePushNotify;
import com.huangyezhaobiao.notification.INotify;
import com.huangyezhaobiao.notification.MiPushNotify;
import com.huangyezhaobiao.notification.NotificationExecutor;
import com.huangyezhaobiao.push.BiddingMessageReceiver.PushHandler;
import com.huangyezhaobiao.receiver.NetworkChangedReceiver;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.voice.VoiceManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.table.TableUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * 1、为了打开客户端的日志，便于在开发过程中调试，需要自定义一个Application。
 * 并将自定义的application注册在AndroidManifest.xml文件中
 * 2、为了提高push的注册率，您可以在Application的onCreate中初始化push。你也可以根据需要，在其他地方初始化push。
 *
 * @author linyueyang
 */
public class BiddingApplication extends Application {
    private NotificationExecutor notificationExecutor;
    private NotificationExecutor getuiNotificationExecutor;
    private INotificationListener listener;// 透传消息的引用
    private NetworkChangedReceiver receiver;
    // user your appid the key.
    //public static final String APP_ID = "2882303761517351320";
    public static final String APP_ID = "2882303761517362207";
    // user your appid the key.
    //public static final String APP_KEY = "5471735123320";
    public static final String APP_KEY = "5321736232207";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo
    public static final String TAG = "com.huangyezhaobiao";

    private static PushHandler handler = null;

    private static LogHandler loghandler = null;
    private Timer mTimer;
    private VoiceManager manager;
    private static BiddingApplication app;
    private ImageLoader imageLoader;


    private void setApp(BiddingApplication context){
        app = context;
    }

    public void setCurrentNotificationListener(INotificationListener listener) {
        this.listener = listener;
    }

    public Activity activity;

    public void removeINotificationListener() {
        listener = null;
    }

    public INotificationListener getCurrentNotificationListener() {
        return listener;
    }

    public static Context getAppInstanceContext(){
        return app;
    }



    public static BiddingApplication getBiddingApplication(){
        return app;
    }
    /**
     * 停止文件操作
     */
    public void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }
    //在自己的Application中添加如下代码
    public static RefWatcher getRefWatcher(Context context) {
        BiddingApplication application = (BiddingApplication) context
                .getApplicationContext();
        return application.refWatcher;
    }

    //在自己的Application中添加如下代码
    private RefWatcher refWatcher;

    private void initNotificationExecutor(){
        notificationExecutor = new NotificationExecutor();
        INotify iNotify      = new MiPushNotify(getApplicationContext());
        notificationExecutor.setiNotify(iNotify);
    }


    private void initGeTuiNotificationExecutor(){
        getuiNotificationExecutor = new NotificationExecutor();
        INotify notify            = new GePushNotify(getApplicationContext());
        getuiNotificationExecutor.setiNotify(notify);
    }

    public NotificationExecutor getGeTuiNotification(){
        if(getuiNotificationExecutor==null)
        {
            initGeTuiNotificationExecutor();
        }
        return getuiNotificationExecutor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       setApp(BiddingApplication.this);
        refWatcher = LeakCanary.install(this);
        initNotificationExecutor();
        initGeTuiNotificationExecutor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        AppBean.getAppBean().setApp(this);
        UserConstans.setUserId(UserUtils.getUserId(this));
        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对'象参数中获取注册信息
        // 因为推送服务XMPushService在AndroidManifest.xml中设置为运行在另外一个进程，这导致本Application会被实例化两次，所以我们需要让应用的主进程初始化。
        if (shouldInit()) {
            //数据库的可视化工具
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());
           // MiPushClient.registerPush(this, APP_ID, APP_KEY); //for test

           /* // 上传日志定时任务
            mTimer = new Timer(true);
            TimerTask mTimerTask;
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    FileUtils.read(getApplicationContext());
                }
            };
            mTimer.schedule(mTimerTask, 5000, 1000 * 60 * 10);//10分钟传一次
            // 写日志线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (loghandler == null) {
                        Looper.prepare();
                        loghandler = new LogHandler(getApplicationContext());
                        Looper.loop();
                    }
                }
            }).start();*/
            Log.e("Thread", Thread.currentThread().getName());
            // 接入腾讯Bugly
            final Context appContext = this.getApplicationContext();
            final String appId = "900004313"; // 上Bugly(bugly.qq.com)注册产品获取的AppId
            final boolean isDebug = true; // true代表App处于调试阶段，false代表App发布阶段
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CrashReport.initCrashReport(appContext, appId, isDebug); // 初始化SDK
                    SqlUtils.initDB(getApplicationContext(), new SqlUpgradeCallback() {
                        @Override
                        public void onUpgrade(DbUtils dbUtils) {
                            updateDb(dbUtils,"PushToStorageBean");
                        }
                    });
                    initImageLoader();
                    manager = VoiceManager.getVoiceManagerInstance(getApplicationContext());
                    manager.initVoiceManager(getApplicationContext());//初始化科大讯飞
                }
            }).start();

        }


       /* // CrashReport.testJavaCrash ();
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }
            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);*/
        if (handler == null)
            handler = new PushHandler(getApplicationContext());

    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(
                ImageLoaderConfiguration.createDefault(getApplicationContext()));


    }

    public NotificationExecutor getNotificationExecutor(){
        if(notificationExecutor==null){
            initNotificationExecutor();
        }
        return notificationExecutor;
    }

    private static void updateDb(DbUtils db, String tableName) {
        try {
            Class<PushToStorageBean> c = (Class<PushToStorageBean>) Class.forName("com.huangyezhaobiao.bean.push." + tableName);// 把要使用的类加载到内存中,并且把有关这个类的所有信息都存放到对象c中
            LogUtils.LogE("shenzhixinDB","upgradeDB:"+db.tableIsExist(PushToStorageBean.class)+"..name:"+TableUtils.getTableName(PushToStorageBean.class));
            if (db.tableIsExist(PushToStorageBean.class)) {
                List<String> dbFildsList = new ArrayList<String>();
                String str = "select * from " + TableUtils.getTableName(PushToStorageBean.class);
                Cursor cursor = db.execQuery(str);
                int count = cursor.getColumnCount();
                for (int i = 0; i < count; i++) {
                    dbFildsList.add(cursor.getColumnName(i));
                }
                LogUtils.LogE("shenzhixinDB","upgradeDB: line 174");
                cursor.close();
                // 把属性的信息提取出来，并且存放到field类的对象中，因为每个field的对象只能存放一个属性的信息所以要用数组去接收
                Field f[] = c.getDeclaredFields();
                for (int i = 0; i < f.length; i++) {
                    String fildName = f[i].getName();
                    LogUtils.LogE("shenzhixinDB","upgradeDB: line 180:"+dbFildsList.contains(fildName));
                    if (fildName.equals("serialVersionUID")) {
                        continue;
                    }
                    String fildType = f[i].getType().toString();
                    tableName = TableUtils.getTableName(PushToStorageBean.class);
                    if (!dbFildsList.contains(fildName)) {
                        if (fildType.equals("class java.lang.String")) {
                            db.execNonQuery("alter table " + tableName + " add " + fildName + " TEXT ");
                        } else if (fildType.equals("int") || fildType.equals("long") || fildType.equals("boolean")) {
                            db.execNonQuery("alter table " + tableName + " add " + fildName + " INTEGER ");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 注册网络变化的receiver
     */
    public void registerNetStateListener() {
        receiver = new NetworkChangedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);

    }

    /**
     * 解绑网络变化的receiver
     */
    public void unRegisterNetStateListener() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

	/*private void XingeRegister() {
        // 信鸽注册
		// 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
		// 收到通知时，会调用本回调函数。
		// 相当于这个回调会拦截在信鸽的弹出通知之前被截取
		// 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等
		XGPushManager.setNotifactionCallback(new XGPushNotifactionCallback() {
			@Override
			public void handleNotify(XGNotifaction xGNotifaction) {
				Log.i("test", "处理信鸽通知：" + xGNotifaction);
				// 获取标签、内容、自定义内容
				String title = xGNotifaction.getTitle();
				String content = xGNotifaction.getContent();
				String customContent = xGNotifaction.getCustomContent();
				// 其它的处理
				// 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
				xGNotifaction.doNotify();
			}
		});
	}*/

    // 判断application是否已经被实例化
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public  boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    public static PushHandler getHandler() {
        return handler;
    }

    public static LogHandler getLogHandler() {
        return loghandler;
    }

   /* private  static class MyDemoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // baseActivity.showDialog();
        }

    }*/

}