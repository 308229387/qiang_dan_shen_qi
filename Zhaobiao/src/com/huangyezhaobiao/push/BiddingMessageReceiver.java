package com.huangyezhaobiao.push;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.LockActivity;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.notification.NotificationExecutor;
import com.huangyezhaobiao.utils.CommonUtils;
import com.huangyezhaobiao.utils.KeyguardUtils;
import com.huangyezhaobiao.utils.LockUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UpdateManager;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 1、PushMessageReceiver是个抽象类，该类继承了BroadcastReceiver。
 * 2、需要将自定义的DemoMessageReceiver注册在AndroidManifest.xml文件中 <receiver
 * android:exported="true"
 * android:name="com.xiaomi.mipushdemo.DemoMessageReceiver"> <intent-filter>
 * <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" /> </intent-filter>
 * <intent-filter> <action android:name="com.xiaomi.mipush.ERROR" />
 * </intent-filter> <intent-filter>
 *  <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" /></intent-filter>
 *  </receiver>
 * 3、DemoMessageReceiver的onReceivePassThroughMessage方法用来接收服务器向客户端发送的透传消息
 * 4、DemoMessageReceiver的onNotificationMessageClicked方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发
 * 5、DemoMessageReceiver的onNotificationMessageArrived方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数
 * 6、DemoMessageReceiver的onCommandResult方法用来接收客户端向服务器发送命令后的响应结果
 * 7、DemoMessageReceiver的onReceiveRegisterResult方法用来接收客户端向服务器发送注册命令后的响应结果
 * 8、以上这些方法运行在非UI线程中
 *
 * @author linyueyang
 */
public class BiddingMessageReceiver extends PushMessageReceiver {

    @Override
    public void onReceivePassThroughMessage(final Context context, final MiPushMessage message){
        Log.v(BiddingApplication.TAG,
                "onReceivePassThroughMessage is called. " + message.toString());
        //LogUtils.LogE("ashenPush", "aaaa");
        CommonUtils.message = message.toString();
        final String log = context.getString(R.string.recv_passthrough_message, message.getContent());
        Message msg = Message.obtain();
        if (message.isNotified()) {
            msg.obj = log;
        }

        synchronized (BiddingMessageReceiver.class) {//加同步锁可以解决这个问题么？
            BiddingApplication app = (BiddingApplication) context.getApplicationContext();
            final NotificationExecutor notificationExecutor = app.getNotificationExecutor();
            LogUtils.LogE("shenzhixin", "isLock:" + KeyguardUtils.isLockScreen(context) + ".lock:" + KeyguardUtils.notLock + ",status:" + StateUtils.getState(context) + "listenr:" + (app.getCurrentNotificationListener() == null) + "on:" + KeyguardUtils.onLock);
            //加入如果应用在后台，就弹窗 2015.11.9
            if(context!=null && !Utils.isForeground(context)){
                if(KeyguardUtils.isLockScreen(context)){
                    Log.e("shenzhixinUI","lock");
                    LockUtils.needLock = true;
                }else{
                    Log.e("shenzhixinUI","unlock");
                    LockUtils.needLock = false;
                }
                PushHandler handler = BiddingApplication.getHandler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final PushBean bean = PushUtils.dealPushMessage(context, message);
                        if(bean==null){return ;}
                        if(bean.getTag()==100 ) {
                            if(SPUtils.getServiceState(context).equals("1")){
                                KeyguardUtils.goToKeyguardActivity(context, LockActivity.class);
                            }else{
                                PushUtils.pushList.clear();
                            }
                        }else{
                            //TODO:通知栏
                            notificationExecutor.onMessageArrived(message);
                        }
                    }
                });
                return;
            }
            //加入如果应用在后台，就弹窗end


            //shenzhixin 2015.8.27 add for screenLock notification
            if (context != null && KeyguardUtils.isLockScreen(context) && !KeyguardUtils.notLock && StateUtils.state == 1 && !KeyguardUtils.onLock) {//锁屏界面,亮屏界面，服务模式下
                Log.e("shenzhixinUI","lock");
                PushHandler handler = BiddingApplication.getHandler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final PushBean bean = PushUtils.dealPushMessage(context, message);
                        if(bean==null){
                            PushUtils.pushList.clear();
                            return ;
                        }

                        if(bean.getTag()==100 ) {
                            if(SPUtils.getServiceState(context).equals("1")) {
                                KeyguardUtils.goToKeyguardActivity(context, LockActivity.class);
                                LockUtils.needLock = true;
                            }else{
                                    PushUtils.pushList.clear();
                            }
                        }else{
                            //TODO:通知栏
                            notificationExecutor.onMessageArrived(message);
                        }
                    }
                });
                return;
            }
            LogUtils.LogE("shenzhixin", "isLock:" + KeyguardUtils.isLockScreen(context) + ".screen:" + KeyguardUtils.SCREEN_ON + ",app:" + (app == null));
            if (app != null) {
                final INotificationListener listener = app.getCurrentNotificationListener();
                if (listener != null) {
                    PushHandler handler = BiddingApplication.getHandler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final PushBean bean = PushUtils.dealPushMessage(context, message);
                            if(bean==null){
                                Log.e("shenzhixinUUU","bean is null") ;
                                return ;
                            }
                            LogUtils.LogE("ashenDialog", "bean:" + (bean == null) + ",neeupdate:" + UpdateManager.needUpdate);
                            if (!UpdateManager.needUpdate)//当要强制更新时是不能够传到Activity进行Ui的显示的，但是可以存入数据库，在dealPushMessage处已经存储了
                                listener.onNotificationCome(bean);
                        }
                    });
                } else {
                    PushHandler handler = BiddingApplication.getHandler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final PushBean bean = PushUtils.dealPushMessage(context, message);
                            if(bean==null){return ;}
                        }
                    });
                }
            }
            Message msgs = Message.obtain();
            if (message.isNotified()) {
                msg.obj = log;
            }
            BiddingApplication.getHandler().sendMessage(msgs);
        }
        //判断是不是运行
        	//1.true------view显示不通知
        	//2.如果不是----view显示，也要通知
       // BiddingApplication.getHandler().sendMessage(msg);
    }
    /**
     * 应用在后台的时候推送到的方法
     */
    @Override
    public void onNotificationMessageClicked(final Context context, final MiPushMessage message){
        LogUtils.LogE(BiddingApplication.TAG,
                "onNotificationMessageClicked is called. " + message.toString());
        String log = context.getString(R.string.click_notification_message, message.getContent());
        
		final BiddingApplication app = (BiddingApplication) context.getApplicationContext();
		if (app != null) {
			LogUtils.LogE("ashenPush", "log:" + log);
			PushHandler handler = BiddingApplication.getHandler();
			handler.post(new Runnable() {
				@Override
				public void run() {
					PushUtils.dealPushMessage(context, message);
					PushUtils.notify(context, message);

				}
			});
		}
        
        Message msg = Message.obtain();
        if (message.isNotified()) {
            msg.obj = log;
        }
        BiddingApplication.getHandler().sendMessage(msg);
    }

    @Override
    public void onReceiveMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceiveMessage(context, miPushMessage);
    }

    /**
     * 应用在前台的时候推送到的方法
     */
    @Override
    public void onNotificationMessageArrived(final Context context, final MiPushMessage message){
        Log.e("shenzhixin","onNotification come haha");
        String log = context.getString(R.string.arrive_notification_message, message.getContent());
        //shenzhixin 2015.8.27 add for screenLock notification end
        synchronized (BiddingMessageReceiver.class) {//加同步锁可以解决这个问题么？
            BiddingApplication app = (BiddingApplication) context.getApplicationContext();
            LogUtils.LogE("shenzhixin", "isLock:" + KeyguardUtils.isLockScreen(context) + ".lock:" + KeyguardUtils.notLock + ",status:" + StateUtils.getState(context) + "listenr:" + (app.getCurrentNotificationListener() == null) + "on:" + KeyguardUtils.onLock);

            //shenzhixin 2015.8.27 add for screenLock notification
            if (context != null && KeyguardUtils.isLockScreen(context) && !KeyguardUtils.notLock && StateUtils.state == 1 && !KeyguardUtils.onLock) {//锁屏界面,亮屏界面，服务模式下
                PushHandler handler = BiddingApplication.getHandler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final PushBean bean = PushUtils.dealPushMessage(context, message);
                        KeyguardUtils.goToKeyguardActivity(context, LockActivity.class);
                    }
                });
                return;
            }
            LogUtils.LogE("shenzhixin", "isLock:" + KeyguardUtils.isLockScreen(context) + ".screen:" + KeyguardUtils.SCREEN_ON + ",app:" + (app == null));
            if (app != null) {
                final INotificationListener listener = app.getCurrentNotificationListener();
                if (listener != null) {
                    PushHandler handler = BiddingApplication.getHandler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final PushBean bean = PushUtils.dealPushMessage(context, message);
                            LogUtils.LogE("ashenDialog", "bean:" + (bean == null) + ",neeupdate:" + UpdateManager.needUpdate);
                            if (!UpdateManager.needUpdate)//当要强制更新时是不能够传到Activity进行Ui的显示的，但是可以存入数据库，在dealPushMessage处已经存储了
                                listener.onNotificationCome(bean);
                        }
                    });
                } else {
                    PushHandler handler = BiddingApplication.getHandler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final PushBean bean = PushUtils.dealPushMessage(context, message);
                        }
                    });
                }
            }

            Message msg = Message.obtain();
            if (message.isNotified()) {
                msg.obj = log;
            }
            BiddingApplication.getHandler().sendMessage(msg);
        }
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Log.v("szxhahaha",
                "onCommandResult is called. " + message.toString());
        //Toast.makeText(context, "onCommandResult",Toast.LENGTH_SHORT).show();
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log = "";
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.register_success);
                Log.e("szxhahaha", "推送注册成功:" + message);
                EventAction action = new EventAction(EventType.REGISTER_SUCCESS,0);
                EventbusAgent.getInstance().post(action);
            } else {
                log = context.getString(R.string.register_fail);
                Log.e("szxhahaha","推送注册失败"+message);
                //TODO:继续注册
                EventAction action = new EventAction(EventType.REGISTER_FAILURE,0);
                EventbusAgent.getInstance().post(action);
                MiPushClient.registerPush(context, BiddingApplication.APP_ID, BiddingApplication.APP_KEY);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.set_alias_success, cmdArg1);
                Log.e("szxhahaha", "别名设置成功"+message);
                EventAction action = new EventAction(EventType.ALIAS_SET_SUCCESS,0);
                EventbusAgent.getInstance().post(action);
            } else {
                log = context.getString(R.string.set_alias_fail, message.getReason());
                Log.e("szxhahaha", "别名设置失败"+message);
                //继续设置
                MiPushClient.setAlias(context, UserUtils.getUserId(context), null);
                EventAction action = new EventAction(EventType.ALIAS_SET_FAILURE,0);
                EventbusAgent.getInstance().post(action);
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.unset_alias_success, cmdArg1);
            } else {
                log = context.getString(R.string.unset_alias_fail, message.getReason());
            }
        }  else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.set_account_success, cmdArg1);
            } else {
                log = context.getString(R.string.set_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.unset_account_success, cmdArg1);
            } else {
                log = context.getString(R.string.unset_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.subscribe_topic_success, cmdArg1);
            } else {
                log = context.getString(R.string.subscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.unsubscribe_topic_success, cmdArg1);
            } else {
                log = context.getString(R.string.unsubscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.set_accept_time_success, cmdArg1, cmdArg2);
            } else {
                log = context.getString(R.string.set_accept_time_fail, message.getReason());
            }
        } else {
            log = message.getReason();
        }
        //MainActivity.logList.add(0, getSimpleDate() + "    " + log);
        Message msg = Message.obtain();
        msg.obj = log;
        BiddingApplication.getHandler().sendMessage(msg);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message){
        Log.v(BiddingApplication.TAG,
                "onReceiveRegisterResult is called. " + message.toString());
        //Toast.makeText(context, "onReceiveRegisterResult",Toast.LENGTH_SHORT).show();
        String command = message.getCommand();
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.register_success);
            } else {
                log = context.getString(R.string.register_fail);
            }
        } else {
            log = message.getReason();
        }
        Message msg = Message.obtain();
        msg.obj = log;
        BiddingApplication.getHandler().sendMessage(msg);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getSimpleDate() {
        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
    }

    public static class PushHandler extends Handler {
        private Context context;
        public PushHandler(Context context) {
            this.context = context;
        }
        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
        }
    }
}
