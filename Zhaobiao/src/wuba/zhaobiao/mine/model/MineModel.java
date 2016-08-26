package wuba.zhaobiao.mine.model;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.GlobalConfigBean;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.SPUtils;
import com.huangyezhaobiao.utils.ToastUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.lzy.okhttputils.OkHttpUtils;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.config.Urls;
import wuba.zhaobiao.mine.activity.AboutActivity;
import wuba.zhaobiao.mine.activity.AccountManageActivity;
import wuba.zhaobiao.mine.activity.HelpActivity;
import wuba.zhaobiao.mine.activity.MyWalletActivity;
import wuba.zhaobiao.mine.activity.SettingActivity;
import wuba.zhaobiao.mine.fragment.MineFragment;
import wuba.zhaobiao.respons.UserInfoRespons;
import wuba.zhaobiao.utils.LogoutDialogUtils;

/**
 * Created by 58 on 2016/8/12.
 */
public class MineModel extends BaseModel implements View.OnClickListener{
    private MineFragment context;
    private View view;
    private TextView tv_userName;
    private TextView tv_userCompany;
    private TextView tv_yue; //58余额
    private ImageView iv_refresh;// 刷新按钮
    private RelativeLayout mywallet;//我的钱包
    private RelativeLayout manage; //账号管理
    private RelativeLayout sliding_settings; //设置
    private RelativeLayout help;//帮助
    private RelativeLayout about; //关于


    public MineModel(MineFragment context){
        this.context = context;
    }

    public void initUserInfo(){
        getUserInfo();
    }

    public void minePageClicked(){
        HYMob.getDataList(context.getActivity(), HYEventConstans.INDICATOR_PERSONAL_PAGE);
    }

    public void createView(LayoutInflater inflater, ViewGroup container){
        view = inflater.inflate(R.layout.fragment_mine, container, false);
    }

    public void initView(){
        initHeaderView();
    }

    private void initHeaderView(){
        tv_userName = (TextView) view.findViewById(R.id.tv_userName);
        tv_userCompany = (TextView) view.findViewById(R.id.tv_userCompany);
    }

    public void initBalance(){
       createBalanceView();
       setBalanceRefreshListener();
    }

    private void createBalanceView(){
        tv_yue = (TextView) view.findViewById(R.id.tv_yue);
        iv_refresh = (ImageView) view.findViewById(R.id.iv_refresh);
    }

    private void setBalanceRefreshListener(){
        iv_refresh.setOnClickListener(this);
    }

    public void initMyWallet(){
        createMyWallet();
        setMywalletListener();
    }

    private void createMyWallet(){
        mywallet = (RelativeLayout) view.findViewById(R.id.mywallet);
    }

    private void setMywalletListener(){
        mywallet.setOnClickListener(this);
    }

    public void initAccountManage(){
        createAccountManage();
        setAccountManageListener();
    }

    private void createAccountManage(){
        manage = (RelativeLayout) view.findViewById(R.id.manage);
    }

    private void setAccountManageListener(){
        manage.setOnClickListener(this);
    }


    public void judgeIsSubAccount(){
        String isSon = UserUtils.getIsSon(context.getActivity());
        if(!TextUtils.isEmpty(isSon) && TextUtils.equals("1",isSon)){
            manage.setVisibility(View.GONE);
        }else{
            manage.setVisibility(View.VISIBLE);
        }
    }

    public void initSetting(){
        createSetting();
        setSettingListener();
    }

    private void createSetting(){
        sliding_settings = (RelativeLayout) view.findViewById(R.id.sliding_settings);
    }

    private void setSettingListener(){
        sliding_settings.setOnClickListener(this);
    }

    public void initHelp(){
        createHelp();
        setHelpListener();
    }

    private void createHelp(){
        help = (RelativeLayout) view.findViewById(R.id.help);
    }

    private void setHelpListener(){
        help.setOnClickListener(this);
    }

    public void initAbout(){
        createAbout();
        setAboutListener();
    }

    private void createAbout(){
        about = (RelativeLayout) view.findViewById(R.id.about);
    }

    private void setAboutListener(){
        about.setOnClickListener(this);
    }


    public View getView() {
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_refresh: //点击了刷新余额按钮
                refreshBalanceClicked();
                getUserInfo();
                setBalanceText();
                break;
            case R.id.mywallet://点击了我的钱包
                goToWalletPageStatistics();
                goToWalletPage();
                break;
            case R.id.manage://点击了子账号管理
                goToAccountManageStatistics();
                goToAccountManage();
                break;
            case R.id.sliding_settings://点击了设置
                goToSettingPageStatistics();
                goToSettingPage();
                break;
            case R.id.help:// 点击了帮助
                goToHelpPageStatistics();
                goToHelpPage();
                break;
            case R.id.about:// 点击了关于
                goToAboutPageStatistics();
                goToAboutPage();
                break;

        }
    }

    private void refreshBalanceClicked(){
        //获取余额
        BDMob.getBdMobInstance().onMobEvent(context.getActivity(), BDEventConstans.EVENT_ID_MANUAL_REFRESH_BALANCE);
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_MANUAL_REFRESH_BALANCE);
    }

    private void getUserInfo(){
        OkHttpUtils.get(Urls.USER_INFO)//
                .execute(new userInfoCallback(context.getActivity(), true));
    }

    private void setBalanceText(){
        tv_yue.setText(R.string.fetching);
    }

    private void saveUserPhone(String phone){
        if(!TextUtils.isEmpty(phone)){
            SPUtils.saveKV(context.getActivity(), GlobalConfigBean.KEY_USERPHONE, phone);
        }
    }

    private void setUserInfo(String balance,String companyName,String userName){
        if (!TextUtils.isEmpty(balance)) {
            tv_yue.setText("￥" + balance);
        }
        if (!TextUtils.isEmpty(companyName)) {
            tv_userCompany.setText(companyName);
        }else if (!TextUtils.isEmpty(userName)) {
            tv_userName.setText(userName);
        }

        tv_userName.setText("我");
    }

    private void goToWalletPageStatistics(){
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_MY_WALLET);
    }

    private void goToWalletPage(){
        ActivityUtils.goToActivity(context.getActivity(), MyWalletActivity.class);
    }

    private void goToAccountManageStatistics(){
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_ACCOUNT);
    }

    private void goToAccountManage(){
        ActivityUtils.goToActivity(context.getActivity(), AccountManageActivity.class);
    }

    private void goToSettingPageStatistics(){
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_SETTING);
    }

    private void goToSettingPage(){
        ActivityUtils.goToActivity(context.getActivity(), SettingActivity.class);
    }

    private void goToHelpPageStatistics(){
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_HELP);
    }

    private void goToHelpPage(){
        ActivityUtils.goToActivity(context.getActivity(), HelpActivity.class);
    }

    private void goToAboutPageStatistics(){
        HYMob.getDataList(context.getActivity(), HYEventConstans.EVENT_ID_ABOUT);
    }

    private void goToAboutPage(){
        ActivityUtils.goToActivity(context.getActivity(), AboutActivity.class);
    }

    public void statisticsDeadTime() {
        HYMob.getBaseDataListForPage(context.getActivity(), HYEventConstans.PAGE_PERSONAL, context.stop_time - context.resume_time);
    }

    private class userInfoCallback extends DialogCallback<UserInfoRespons> {

        public userInfoCallback(Activity context, Boolean needProgress) {
            super(context, needProgress);
        }

        @Override
        public void onResponse(boolean isFromCache, UserInfoRespons userInfoRespons, Request request, @Nullable Response response) {
            String balance = userInfoRespons.getData().getBalance();
            String userPhone = userInfoRespons.getData().getPhone();
            String companyName = userInfoRespons.getData().getCompanyName();
            String userName = userInfoRespons.getData().getUserName();
            saveUserPhone(userPhone);
            setUserInfo(balance, companyName,userName);
        }
        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            if (!isToast && e != null) {
                ToastUtils.showToast(e.getMessage());
            }
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable UserInfoRespons userInfoRespons, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, userInfoRespons, call, response, e);
            if (e != null && e.getMessage().equals(NEED_DOWN_LINE)) {
                new LogoutDialogUtils(context.getActivity(), context.getString(R.string.force_exit)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(CHILD_FUNCTION_BAN)) {
                new LogoutDialogUtils(context.getActivity(), context.getString(R.string.child_function_ban)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(CHILD_HAS_UNBIND)) {
                new LogoutDialogUtils(context.getActivity(), context.getString(R.string.child_has_unbind)).showSingleButtonDialog();
            } else if (e != null && e.getMessage().equals(PPU_EXPIRED)) {
                new LogoutDialogUtils(context.getActivity(), context.getString(R.string.ppu_expired)).showSingleButtonDialog();
            }
        }
    }


}
