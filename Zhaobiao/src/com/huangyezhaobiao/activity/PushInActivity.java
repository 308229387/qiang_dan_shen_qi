package com.huangyezhaobiao.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.huangye.commonlib.activity.BaseActivity;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.pop.PopBaseBean;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.view.QDWaitDialog;
import com.huangyezhaobiao.vm.KnockViewModel;
import com.huangyezhaobiao.voice.VoiceManager;

/**
 * Created by shenzhixin on 2015/11/25.
 * 应用内部推送到了的界面,不用dialog了,会内存泄露
 */
public class PushInActivity extends BaseActivity implements NetWorkVMCallBack, View.OnClickListener, INotificationListener {
    private String yuyin_op = MDConstans.ACTION_CLOSE_VOLUMN;
    private KnockViewModel kvm;
    private ImageView dialog_voice;//声音控制按钮
    private LinearLayout dialog_linear;//添加view的布局界面
    private Button dialog_knock;//抢单按钮
    private TextView dialog_fee;//费用价格
    private TextView dialog_discount_fee;//折扣价
    private Button dialog_next;//下一条
    private ImageView dialog_cancel;//取消按钮
    private TextView count;//显示的时间倒计时
    private int countDown;//剩余时间的倒计时
    private PopBaseBean bean;//推送的bean实体
    private VoiceManager voiceManager;
    private BiddingApplication app;
    private android.app.ProgressDialog qdDialog;

    private String data; //埋点打他数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = new VoiceManager.OnVoiceManagerPlayFinished() {
            @Override
            public void onPlayFinished() {
                if (countDown <= 0)
                    showNext();
            }
        };
        kvm = new KnockViewModel(this, this);
        setContentView(R.layout.dialog_orderpop);
        voiceManager = VoiceManager.getVoiceManagerInstance(getApplicationContext());
        voiceManager.setOnPlayFinishedListener(listener);
        initView();
        showFirst();
        show();
        BiddingApplication bapp = (BiddingApplication)getApplication();
        bapp.activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Bugtags.onResume(this);
        if (app == null) {
            app = (BiddingApplication) getApplication();
        }
        app.setCurrentNotificationListener(this);
        BDMob.getBdMobInstance().onResumeActivity(this);

    }


    @Override
    public void onBackPressed() {

    }

    //展示当前推送列表的第一条
    private void showFirst() {
        if (null != PushUtils.pushList && PushUtils.pushList.size() > 0) {
            bean = PushUtils.pushList.get(0);
            beanToView();
        } else {
            backToKeyguard();
        }
    }
    private  VoiceManager.OnVoiceManagerPlayFinished listener;

    private void show() {
        if (PushUtils.pushList.size() == 1) {
            voiceManager = VoiceManager.getVoiceManagerInstance(getApplicationContext());
            String du = bean.getVoice();
            LogUtils.LogE("ashenSize", "size:" + PushUtils.pushList.size() + ",voiceManager:" + (voiceManager == null) + ",voice：" + du);
            voiceManager.createOrdersDialog(du);
        } else if (PushUtils.pushList.size() == 2) {
            showNextButton();
        }
    }

    /**
     * 把bean转化成view
     */
    private void beanToView() {
        dialog_linear.removeAllViews();
        dialog_linear.addView(bean.initView(this));
        count = (TextView) findViewById(R.id.pop_count);
        countDown = 15;
        countdown();

        //如果原价和活动价一样,那么就显示活动价
        /**
         * 如果原价为空----
         */

        if(TextUtils.equals(bean.getOriginalFee(),bean.getFee())){//两个价格一样的
            dialog_fee.setText(bean.getFee());
            dialog_discount_fee.setVisibility(View.GONE);
        }else {
            //活动价
            dialog_discount_fee.setText(bean.getOriginalFee() + "元");
            dialog_discount_fee.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //活动价

            //原价
            dialog_fee.setText(bean.getFee());
            //原价
        }
    }


    /**
     * 进行倒计时的计算的函数
     */
    private void countdown() {
        handler.postDelayed(runnable, 1000);
        LogUtils.LogE("ashenAAA", "postDelayed");
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LogUtils.LogE("ashenAAA", "final countDown:" + countDown);
            countDown--;
            if (countDown >= 0) {
                LogUtils.LogE("ashenAAA", "countDown:" + countDown);
                count.setText("" + countDown);
                handler.postDelayed(this, 1000);
            } else {
                if (voiceManager.isSpeakFinish()) {
                    handler.removeCallbacks(runnable);
                    showNext();
                }
            }
        }
    };


    /**
     * 显示下一条
     */
    public void showNext() {
        if(PushUtils.pushList.size()>0) {
            PushUtils.pushList.remove(0);
        }
        showFirst();
        LogUtils.LogE("shenzhixin", "" + PushUtils.pushList.size());
        if (PushUtils.pushList.size() > 0) {
            if (PushUtils.pushList.size() <= 1)
                dialog_next.setVisibility(View.INVISIBLE);
            else{
                dialog_next.setVisibility(View.VISIBLE);
            }
            // 需要改成list的下一条
            voiceManager.addOrder(bean.getVoice());
            voiceManager.manaulToNextOrders();
        }

    }


    //展示下一个按钮
    public void showNextButton() {
        LogUtils.LogE("shenzhixin", "showNextButton");
        dialog_next.setVisibility(View.VISIBLE);
        dialog_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这时要把runnable去掉
                handler.removeCallbacks(runnable);
                showNext();
                MDUtils.pushWindowPageMD(PushInActivity.this, bean.toPushPassBean().getCateId() + "", bean.toPushPassBean().getBidId() + "",
                        MDConstans.ACTION_MANUAL_NEXT_ORDER);
            }
        });
    }

    /**
     * 初始化view
     */
    public void initView() {
        qdDialog      = new QDWaitDialog(this);
        dialog_cancel = (ImageView) findViewById(R.id.dialog_cancel);
        dialog_fee = (TextView) findViewById(R.id.dialog_fee);
        dialog_voice = (ImageView) findViewById(R.id.dialog_voice);
        dialog_linear = (LinearLayout) findViewById(R.id.dialog_linear);
        dialog_knock = (Button) findViewById(R.id.dialog_knock);
        dialog_next = (Button) findViewById(R.id.dialog_next);
        dialog_discount_fee = (TextView) findViewById(R.id.dialog_discount_fee);
        dialog_cancel.setOnClickListener(this);
        dialog_voice.setOnClickListener(this);
        dialog_knock.setOnClickListener(this);
        dialog_next.setOnClickListener(this);
    }

    @Override
    public void initListener() {

    }


    @Override
    public void onLoadingStart() {
    }

    @Override
    public void onLoadingSuccess(Object t) {
        qdDialog.dismiss();
        backToKeyguard();
        voiceManager.closeOrdersDialog();
        PushUtils.pushList.clear();
        if(t instanceof  Integer){
            int status = (int) t;
            // status = 3;//for test
            switch (status){
                case 1://标已被抢走
                    ToastUtils.makeImgAndTextToast(this,"已被抢走",R.drawable.validate_error,1).show();
                    backToKeyguard();
                    break;
                case 2://余额不足
                    ToastUtils.makeImgAndTextToast(this, "余额不足", R.drawable.validate_error, 1).show();
                    backToKeyguard();
                    break;
                case 3://抢单成功
                    ToastUtils.makeImgAndTextToast(this, "抢单成功", R.drawable.validate_error, 1).show();
                    MDUtils.pushWindowPageMD(this, bean.toPushPassBean().getCateId() + "", bean.toPushPassBean().getBidId() + "", MDConstans.ACTION_QIANG_DAN);
                    //跳到主界面
                    ActivityUtils.goToActivity(this, BidSuccessActivity.class);
                    break;
                case 4://您已抢过此单
                    ToastUtils.makeImgAndTextToast(this, "您以抢过此单", R.drawable.validate_error, 1).show();
                    backToKeyguard();
                    break;
                case 5://抢单失败
                    ToastUtils.makeImgAndTextToast(this, "抢单失败", R.drawable.validate_error, 1).show();
                    backToKeyguard();
                    break;
            }
        }
    }

    @Override
    public void onLoadingError(String msg) {
        Toast.makeText(this,"失败了",Toast.LENGTH_SHORT).show();
        if(qdDialog!=null && qdDialog.isShowing()) {
            qdDialog.dismiss();
        }
        backToKeyguard();
        if(voiceManager!=null) {
            voiceManager.closeOrdersDialog();
        }
        PushUtils.pushList.clear();
    }

    @Override
    public void onLoadingCancel() {

    }

    @Override
    public void onNoInterNetError() {
        qdDialog.dismiss();
        backToKeyguard();
        voiceManager.closeOrdersDialog();
        PushUtils.pushList.clear();
        Toast.makeText(this,"网络有问题",0).show();
    }

    @Override
    public void onLoginInvalidate() {

    }

    @Override
    public void onVersionBack(String version) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel://取消,回到锁屏界面
                BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_WINDOW_PAGE_CLOSE);

                HYMob.getDataList(this, HYEventConstans.EVENT_ID_WINDOW_PAGE_CLOSE);
                data= HYMob.dataBeanToJson(HYMob.dataList, "co", "sa", "cq");
                HYMob.createMap(this, data, "0") ; //0表示正常日志，1表示崩溃日志

                backToKeyguard();
                voiceManager.closeOrdersDialog();
                PushUtils.pushList.clear();
                MDUtils.pushWindowPageMD(this, bean.getCateId() + "", bean.toPushPassBean().getBidId() + "", MDConstans.ACTION_MANUAL_CLOSE);
                break;
            case R.id.dialog_voice://声音按钮，关闭或打开声音
                BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_WINDOW_PAGE_VOLUME);

                HYMob.getDataList(this, HYEventConstans.EVENT_ID_WINDOW_PAGE_VOLUME);
                data= HYMob.dataBeanToJson(HYMob.dataList, "co", "sa", "cq");
                HYMob.createMap(this, data, "0") ; //0表示正常日志，1表示崩溃日志

                voiceManager.clickVolumeButton();
                MDUtils.pushWindowPageMD(this, bean.getCateId() + "", bean.toPushStorageBean() + "", yuyin_op);
                if (MDConstans.ACTION_CLOSE_VOLUMN.equals(yuyin_op)) {
                    yuyin_op = MDConstans.ACTION_OPEN_VOLUMN;
                    dialog_voice.setImageResource(R.drawable.t_voice_close);

                } else {
                    yuyin_op = MDConstans.ACTION_CLOSE_VOLUMN;
                    dialog_voice.setImageResource(R.drawable.t_voice);
                }
                break;
            case R.id.dialog_knock://抢单按钮，抢单，然后进入首列表
                //弹窗点击抢单
                BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_WINDOW_PAGE_BIDDING);

                HYMob.getDataListByTanChuang(this, HYEventConstans.EVENT_ID_WINDOW_PAGE_BIDDING, String.valueOf(bean.toPushPassBean().getBidId()),"1","4");
                data= HYMob.dataBeanToJson(HYMob.dataList, "co","sl","lockScreenState","grabOrderStyle", "sa", "cq");
                HYMob.createMap(this, data, "0") ; //0表示正常日志，1表示崩溃日志

                kvm.knock(bean.toPushPassBean(),AppConstants.BIDSOURCE_WINDOW);
                voiceManager.clickQDButton();
                cancelAllWorks();
                MDUtils.pushWindowPageMD(this, bean.toPushPassBean().getCateId() + "", bean.toPushPassBean().getBidId() + "", MDConstans.ACTION_QIANG_DAN);
                qdDialog.show();
                break;
            case R.id.dialog_next://下一条
                BDMob.getBdMobInstance().onMobEvent(this, BDEventConstans.EVENT_ID_WINDOW_PAGE_NEXT);

                HYMob.getDataList(this, HYEventConstans.EVENT_ID_WINDOW_PAGE_NEXT);
                data= HYMob.dataBeanToJson(HYMob.dataList, "co", "sa", "cq");
                HYMob.createMap(this, data, "0") ; //0表示正常日志，1表示崩溃日志

                //这时要把runnable去掉
                handler.removeCallbacks(runnable);
                showNext();
                MDUtils.pushWindowPageMD(this, bean.toPushPassBean().getCateId() + "", bean.toPushPassBean().getBidId() + "",
                        MDConstans.ACTION_MANUAL_NEXT_ORDER);
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (app != null) {
            app.removeINotificationListener();
        }
        BDMob.getBdMobInstance().onPauseActivity(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 回到锁屏界面
     */
    private void backToKeyguard() {
        cancelAllWorks();
        finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAllWorks();
        voiceManager.setOnPlayFinishedListener(null);
        BiddingApplication bapp = (BiddingApplication) getApplication();
        bapp.activity = null;
        voiceManager = null;
        listener = null;
        PushUtils.pushList.clear();
    }

    /**
     * 取消所有操作
     */
    private void cancelAllWorks() {
        LogUtils.LogE("ashen", "hahaha onDestroy");
        handler.removeCallbacks(runnable);
        if (voiceManager != null) {
            voiceManager.closeOrdersDialog();
        }
        PushUtils.pushList.clear();
    }

    @Override
    public void onNotificationCome(PushBean pushBean) {
        LogUtils.LogE("shenzhixin", "notificationCome");
        if (null != pushBean && pushBean.getTag() == 100 && StateUtils.getState(this) == 1) {
            LogUtils.LogE("shenzhixin", "show");
            show();
        }
    }
}
