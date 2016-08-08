package wuba.zhaobiao.common.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.AutoSettingsActivity;
import com.huangyezhaobiao.activity.MobileBindChangeActivity;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.utils.Utils;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.mine.activity.SettingActivity;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.respons.UserInfoRespons;
import wuba.zhaobiao.utils.LogoutDialogUtils;

/**
 * Created by 58 on 2016/8/3.
 */
public class SettingModel extends BaseModel implements View.OnClickListener{

    private SettingActivity context;
    private View layout_back_head,back_layout;
    private TextView txt_head;
    private View rl_change_mobile_settings,rl_auto_settings;
    private TextView bind_mobile,tv_now_bind_mobile;
    private ImageView tv_arrow;
    private String mobile;
    private RelativeLayout rl_exit;

    public SettingModel(SettingActivity context){
        this.context = context;
    }

    public void initHeader() {
        createHeader();
        initBack();
        createTitle();
    }

    private void createHeader(){
        layout_back_head  = context.findViewById(R.id.layout_head);
    }

    private void initBack(){
        createBack();
        setBackListener();
    }

    private void createBack(){
        back_layout = context.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.VISIBLE);
    }

    private void setBackListener(){
        back_layout.setOnClickListener(this);
    }

    private void createTitle(){
        txt_head = (TextView) context.findViewById(R.id.txt_head);
        txt_head.setText("设置");
    }

    public void initMobileSetting(){
        createMobileSetting();
        judgeIsEnable();
        initNowBindMobile();
    }

    private void createMobileSetting(){
        rl_change_mobile_settings = context.findViewById(R.id.rl_change_mobile_settings);
    }

    private void judgeIsEnable(){
        bind_mobile = (TextView) context.findViewById(R.id.bind_mobile);
        tv_arrow = (ImageView) context.findViewById(R.id.tv_arrow);
        String isSon = UserUtils.getIsSon(context);
        if(!TextUtils.isEmpty(isSon) && TextUtils.equals("1",isSon)){
            bind_mobile.setText("绑定手机号");
            tv_arrow.setVisibility(View.INVISIBLE);
        }else{
            setMobileSettingListener();
            tv_arrow.setVisibility(View.VISIBLE);
        }
    }

    private void setMobileSettingListener(){
        rl_change_mobile_settings.setOnClickListener(this);
    }

    private void initNowBindMobile(){
        createNowBindMobile();
        setNowBindMobile();
    }

    private void createNowBindMobile(){
        tv_now_bind_mobile = (TextView) context.findViewById(R.id.tv_now_bind_mobile);
    }

    private void setNowBindMobile(){
        String mobile = SPUtils.getVByK(context, GlobalConfigBean.KEY_USERPHONE);
        tv_now_bind_mobile.setText("已绑定" + mobile);
    }

    public void initAutoSetting(){
        createAutoSetting();
        setAutoSettingListener();
        judgeIsSubAccount();
    }

    private void createAutoSetting(){
        rl_auto_settings  = context.findViewById(R.id.rl_auto_settings);
    }


    private void setAutoSettingListener(){
        rl_auto_settings.setOnClickListener(this);
    }

    private void judgeIsSubAccount(){
        String isSon = UserUtils.getIsSon(context);
        if(!TextUtils.isEmpty(isSon) && TextUtils.equals("1",isSon)){
            rl_auto_settings.setVisibility(View.GONE);
        }else{
            rl_auto_settings.setVisibility(View.VISIBLE);
        }
    }

    public void initLogout(){
        createLogout();
        setLogoutListener();
    }

    private void createLogout(){
        rl_exit = (RelativeLayout) context.findViewById(R.id.rl_exit);
    }

    private void setLogoutListener(){
        rl_exit.setOnClickListener(this);
    }

    public void setTopBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = Utils.getStatusBarHeight(context);
            int more = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
            if (layout_back_head != null) {
                layout_back_head.setPadding(0, height + more, 0, 0);
            }
        }
    }

    public void setTopBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context.getWindow().setBackgroundDrawable(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                back();
                break;
            case R.id.rl_change_mobile_settings://去手机绑定界面
                goToMobileChangePage();
                goToMobileChangePageStatistics();
                break;
            case R.id.rl_auto_settings://去自定义设置界面
                goToAutoSettingPage();
                goToAutoSettingPageStatistics();
                break;

            case R.id.rl_exit:// 退出
//                globalConfigVM.refreshUsers(); //进行数据同步化的操作;
                logoutStatistics();
                new LogoutDialogUtils(context, context.getString(R.string.logout_make_sure)).showTwoButtonDialog();
                break;
        }
    }

    private void back() {
        context.onBackPressed();
    }

    private void goToMobileChangePage(){
        Intent intent = MobileBindChangeActivity.onNewIntent(context,mobile);
        context.startActivity(intent);
    }

    private void goToMobileChangePageStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_BIND_MOBILE);
    }

    private void goToAutoSettingPage(){
        Intent intent = AutoSettingsActivity.onNewIntent(context);
        context.startActivity(intent);
    }

    private void goToAutoSettingPageStatistics(){
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_AUTO_SETTING);
    }

    private void logoutStatistics(){
        BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_LOGOUT);
        HYMob.getDataList(context, HYEventConstans.EVENT_ID_LOGOUT);
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context, HYEventConstans.PAGE_SETTING_LIST, context.stop_time - context.resume_time);
    }

    public void getUserInfo(){
        OkHttpUtils.get(Urls.USER_INFO)//
                .execute(new userInfoCallback(context, true));
    }


    private class userInfoCallback extends DialogCallback<UserInfoRespons>{

        public userInfoCallback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, UserInfoRespons userInfoRespons, Request request, @Nullable Response response) {
            String phone = userInfoRespons.getData().getPhone();
            SPUtils.saveKV(context, GlobalConfigBean.KEY_USERPHONE, phone);
            setBindMobile();

        }
        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            ToastUtils.showToast(e.getMessage());
        }

    }

    private void setBindMobile(){
        mobile = SPUtils.getVByK(context, GlobalConfigBean.KEY_USERPHONE);
        if(!TextUtils.isEmpty(mobile)){
            tv_now_bind_mobile.setText("已绑定" + mobile);
        }
    }

}
