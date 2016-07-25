package com.huangyezhaobiao.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.AboutActivity;
import com.huangyezhaobiao.activity.BidSuccessActivity;
import com.huangyezhaobiao.activity.HelpActivity;
import com.huangyezhaobiao.activity.MainActivity;
import com.huangyezhaobiao.activity.ManageActivity;
import com.huangyezhaobiao.activity.MyWalletActivity;
import com.huangyezhaobiao.activity.SettingsActivity;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.vm.YuEViewModel;

import java.util.Map;

/**
 * Created by 58 on 2016/6/17.
 */
public class PersonalCenterFragment extends BaseHomeFragment implements NetWorkVMCallBack,View.OnClickListener{
    private TextView tv_userName;
    private TextView tv_userCompany;

    private TextView tv_yue; //58余额
    private ImageView iv_refresh;// 刷新按钮

    private RelativeLayout mywallet;//我的钱包
    private RelativeLayout manage; //
    private RelativeLayout sliding_settings; //设置
    private RelativeLayout help;//帮助
    private RelativeLayout about; //关于
//    private RelativeLayout manage; //账号管理

    private YuEViewModel yuEViewModel;


    @Override
    public void OnFragmentSelectedChanged(boolean isSelected) {
        if(isSelected){

        }
    }

    @Override
    protected void loadDatas() {

    }

    @Override
    protected void loadMore() {

    }

    @Override
    public void onResume() {
        super.onResume();
        yuEViewModel.getBalance();
        HYMob.getDataList(getActivity(), HYEventConstans.INDICATOR_PERSONAL_PAGE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yuEViewModel = new YuEViewModel(this, getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        if(view == null){
            view = inflater.inflate(R.layout.navigation_header, null);
            tv_userName = (TextView) view.findViewById(R.id.tv_userName);
            tv_userCompany = (TextView) view.findViewById(R.id.tv_userCompany);
            iv_refresh = (ImageView) view.findViewById(R.id.iv_refresh);
            iv_refresh.setOnClickListener(this);
            tv_yue = (TextView) view.findViewById(R.id.tv_yue);
            mywallet = (RelativeLayout) view.findViewById(R.id.mywallet);
            mywallet.setOnClickListener(this);
            manage = (RelativeLayout) view.findViewById(R.id.manage);
            manage.setOnClickListener(this);
            sliding_settings = (RelativeLayout) view.findViewById(R.id.sliding_settings);
            sliding_settings.setOnClickListener(this);
            help = (RelativeLayout) view.findViewById(R.id.help);
            help.setOnClickListener(this);
            about = (RelativeLayout) view.findViewById(R.id.about);
            about.setOnClickListener(this);
//            manage = (RelativeLayout) view.findViewById(R.id.manage);
//            manage.setOnClickListener(this);

        }else{
            ((FrameLayout)view.getParent()).removeView(view);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_refresh: //点击了刷新余额按钮
                //获取余额
                BDMob.getBdMobInstance().onMobEvent(getActivity(), BDEventConstans.EVENT_ID_MANUAL_REFRESH_BALANCE);
                HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_MANUAL_REFRESH_BALANCE);
                yuEViewModel.getBalance();
                tv_yue.setText(R.string.fetching);
                break;
            case R.id.mywallet://点击了我的钱包
                ActivityUtils.goToActivity(getActivity(), MyWalletActivity.class);
                HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_MY_WALLET);
                break;
            case R.id.manage:
                ActivityUtils.goToActivity(getActivity(), ManageActivity.class);
                break;
            case R.id.sliding_settings://点击了设置
                Intent intent = SettingsActivity.onNewIntent(getActivity());
                startActivity(intent);
                HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_SETTING);
                break;
            case R.id.help:// 点击了帮助
                ActivityUtils.goToActivity(getActivity(), HelpActivity.class);
                HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_HELP);
                break;
            case R.id.about:// 点击了关于
                ActivityUtils.goToActivity(getActivity(), AboutActivity.class);
                HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_ABOUT);
                break;

        }

    }

    @Override
    public void onTitleBarClicked(TitleBarType type) {
    }


    @Override
    public void onLoadingStart() {
        startLoading();
    }

    @Override
    public void onLoadingSuccess(Object t) {
        if (t instanceof Map<?, ?>) {
            Map<String, String> maps = (Map<String, String>) t;
            String balance = maps.get("balance");
            String userPhone = maps.get("phone");
            String companyName = maps.get("companyName");
            String userName = maps.get("userName");

            SPUtils.saveKV(getActivity(), GlobalConfigBean.KEY_USERPHONE, userPhone);

            if (!TextUtils.isEmpty(balance)) {
                tv_yue.setText("￥"+balance);
                stopLoading();
            }
            if (!TextUtils.isEmpty(companyName)) {
                tv_userCompany.setText(companyName);
                stopLoading();
            }
//            if (!TextUtils.isEmpty(userName)) {
//                tv_userName.setText(userName);
//                stopLoading();
//            }
            tv_userName.setText("我");
        }
    }

    @Override
    public void onLoadingError(String msg) {
        if(getActivity() != null){
            stopLoading();
        }

        if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
            MainActivity ola = (MainActivity) getActivity();
            ola.onLoadingError(msg);
        }
//        if (!TextUtils.isEmpty(msg)) {
//            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onLoadingCancel() {
        stopLoading();
    }

    @Override
    public void onNoInterNetError() {
//        Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        stopLoading();
    }


    @Override
    public void onLoginInvalidate() {
        stopLoading();
        MainActivity ola = (MainActivity) getActivity();
        ola.onLoginInvalidate();
    }

    @Override
    public void onVersionBack(String version) {

    }



    @Override
    public void onStop() {
        super.onStop();
        HYMob.getBaseDataListForPage(getActivity(), HYEventConstans.PAGE_PERSONAL, stop_time - resume_time);
    }
}
