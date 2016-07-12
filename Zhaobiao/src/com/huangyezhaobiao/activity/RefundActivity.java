package com.huangyezhaobiao.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.factory.RefundFragmentFactory;
import com.huangyezhaobiao.fragment.refund.RefundBaseFragment;
import com.huangyezhaobiao.gtui.GePushProxy;
import com.huangyezhaobiao.inter.INotificationListener;
import com.huangyezhaobiao.mediator.RefundMediator;
import com.huangyezhaobiao.netmodel.INetStateChangedListener;
import com.huangyezhaobiao.netmodel.NetStateManager;
import com.huangyezhaobiao.photomodule.CameraHelper;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.utils.StateUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.wuba.loginsdk.external.LoginClient;

import java.sql.Ref;

/**
 * Created by shenzhixin on 2015/12/9.
 * 退单界面
 */
public class RefundActivity extends CommonFragmentActivity implements View.OnClickListener, INetStateChangedListener, INotificationListener, TitleMessageBarLayout.OnTitleBarClickListener {
    public final static String KEY_REFUND_TYPE = "refund_type";
    public final static String KEY_ORDER_ID    = "orderId";

    private TitleMessageBarLayout tbl;
    private View          back_layout;//整个的titleBar
    private TextView              txt_head;
    private LinearLayout          real_back_layout;//后退按键
    private BiddingApplication app;

    FragmentManager     fragmentManager;
    FragmentTransaction fragmentTransaction;
    RefundBaseFragment refundBaseFragment = null;
    private String orderId;

    /**
     * RefundActivity需要的intent
     * @param context
     * @param refundType
     * @return
     */
    public static Intent onNewIntent(Context context,String refundType,String orderId){
        Intent intent = new Intent(context,RefundActivity.class);
        intent.putExtra(KEY_REFUND_TYPE, refundType);
        intent.putExtra(KEY_ORDER_ID, orderId);
        return intent ;
    }


    @Override
    protected void onResume() {
        super.onResume();
        app.setCurrentNotificationListener(this);
        NetStateManager.getNetStateManagerInstance()
                .setINetStateChangedListener(this);
        if (NetUtils.isNetworkConnected(this)) {
            NetConnected();
        } else {
            NetDisConnected();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        app.removeINotificationListener();
        NetStateManager.getNetStateManagerInstance()
                .removeINetStateChangedListener();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (BiddingApplication) getApplication();
        initEnvironment();
        fragmentManager     = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        setContentView(getLayoutId());
        initView();
        initListener();
        setStyleBar();
        getWindow().setBackgroundDrawable(null);
        configFragmentWithIntentType();
    }

    /**
     * 根据传过来的type,决定使用哪一个fragment,工厂模式来做
     */
    private void configFragmentWithIntentType() {
        orderId     = getIntent().getStringExtra(KEY_ORDER_ID);
        String type = getIntent().getStringExtra(KEY_REFUND_TYPE);
        Log.e("shenzhixinUUU", "go to result:" + type);
        try {
            refundBaseFragment = RefundFragmentFactory.createRefundFragment(type);
            refundBaseFragment.setOrderId(orderId);
            fragmentTransaction.add(R.id.refund_framelayout,refundBaseFragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initListener() {
        real_back_layout.setOnClickListener(this);
        tbl.setTitleBarListener(this);
    }

    private void setStyleBar() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
          //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int height = Utils.getStatusBarHeight(this);
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
            if (back_layout != null) {
                back_layout.setPadding(0, height + more, 0, 0);
            }
        }
    }

    private void initView() {
        tbl              = (TitleMessageBarLayout) findViewById(R.id.tbl);
        back_layout      = findViewById(R.id.layout_back);
        txt_head         = (TextView) findViewById(R.id.txt_head);
        txt_head.setText("退单申请");
        real_back_layout = (LinearLayout) findViewById(R.id.back_layout);
        real_back_layout.setVisibility(View.VISIBLE);

    }


    private int getLayoutId(){
        return R.layout.activity_refund;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_layout:
                onBackPressed();
                break;
        }
    }

    @Override
    public void NetConnected() {
        if (tbl != null && tbl.getType() == TitleBarType.NETWORK_ERROR) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tbl.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void NetDisConnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tbl != null) {
                    tbl.showNetError();
                    tbl.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onNotificationCome(PushBean pushBean) {
        if (null != pushBean) {
            int type = pushBean.getTag();
            if (type == 100 && StateUtils.getState(RefundActivity.this) == 1) {
                Intent intent = new Intent(this, PushInActivity.class);
                startActivity(intent);
            } else {
                tbl.setPushBean(pushBean);
                tbl.setVisibility(View.VISIBLE);
                PushUtils.pushList.clear();
            }
        }
    }
        /**
         * 进行一些界面环境的配置
         */
    private void initEnvironment() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    @Override
    public void onTitleBarClosedClicked() {
        tbl.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("shenzhixin","hahahhahahha229:"+requestCode);
    //    if (requestCode == CameraHelper.TAKE_CAMERA_REQUEST_CODE) {//拍照
            Log.e("shenzhixin","hahahhahahha231："+resultCode);
            if (resultCode == RESULT_OK) {
                refundBaseFragment.fillDatas();
            }else if(resultCode!=0){
                Toast.makeText(this,"拍照出问题了呢:"+resultCode,Toast.LENGTH_SHORT).show();
            }
       // }
    }


    @Override
    public void onLoginInvalidate() {
        super.onLoginInvalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefundMediator.checkedId.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(RefundActivity.this, HYEventConstans.PAGE_REFUND, stop_time - resume_time);
    }
}
