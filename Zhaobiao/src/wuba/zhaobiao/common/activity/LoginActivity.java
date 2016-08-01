package wuba.zhaobiao.common.activity;

import android.os.Bundle;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.application.BiddingApplication;

import wuba.zhaobiao.common.model.LoginModel;

/**
 * Created by SongYongmeng on 2016/7/28.
 * 描    述：登陆界面，注册SDK，设置个性化SDK，登陆PASSPORD然后储存信息，操作。此Activity按返回键需要直接退，
 * 所以会先删除其他的Activity
 */
public class LoginActivity extends BaseActivity<LoginModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        removeOtherActivity();
        init();
    }

    private void removeOtherActivity() {
        BiddingApplication.getInstance().removeOtherActivity(this);
    }

    private void init() {
        model.creatLoginCallback();
        model.setLoginSDKAndSaveInfo();
        model.configLandedParams();
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.getBaiduStatisticsInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.unregisterLoginSDK();
    }

    @Override
    public LoginModel createModel() {
        return new LoginModel(LoginActivity.this);
    }


}
